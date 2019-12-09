package com.mytaxi.rideme.data.stores.room

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import com.mytaxi.rideme.base.data.ReactiveStore
import polanski.option.Option
import javax.inject.Inject

open class RoomStore<Key, Value> @Inject constructor(
        private val extractKeyFromModel: Function<Value, Key>,
        private val dao: Dao<Key, Value>,
        private val allSubject: Subject<Option<List<Value>>>,
        private val subjectMap: HashMap<Key, Subject<Option<Value>>>) : ReactiveStore<Key, Value> {

    override fun storeSingular(value: Value): Completable {
        return Completable.create {
            val key = extractKeyFromModel.apply(value)
            dao.insert(value)
            publishAllSubject()
            getOrCreateSubjectForKey(key).onNext(getValue(key))
            it.onComplete()
        }
    }

    override fun storeAll(values: List<Value>): Completable {
        return Completable.create { completableEmitter ->
            values.forEach { dao.insert(it) }
            publishAllSubject()
            publishSingleSubjects()
            completableEmitter.onComplete()
        }
    }

    override fun getSingular(key: Key): Observable<Option<Value>> {
        return Observable.defer<Option<Value>> {
            getOrCreateSubjectForKey(key)
                    .startWith(getValue(key))
        }
                .observeOn(Schedulers.computation())
    }

    override fun getAll(): Observable<Option<List<Value>>> {
        return Observable.defer {
            allSubject.startWith(Maybe.just(dao.queryAll())
                    .filter(List<Value>::isNotEmpty)
                    .map { Option.ofObj(it) }
                    .blockingGet(Option.none()))
                    .subscribeOn(Schedulers.computation())
        }
                .observeOn(Schedulers.computation())
    }


    override fun clear(): Completable {
        return Completable.create {
            dao.clear()
            publishAllSubject()
            publishSingleSubjects()
            it.onComplete()
        }
    }

    override fun removeSingular(key: Key): Completable {
        return Completable.create {
            dao.removeSingle(key)
            publishAllSubject()
            getOrCreateSubjectForKey(key).onNext(getValue(key))
            it.onComplete()
        }
    }

    override fun replaceAll(values: List<Value>): Completable {
        return Completable.create {
            dao.clear()
            it.onComplete()
        }
                .concatWith(storeAll(values))
    }

    private fun getOrCreateSubjectForKey(key: Key): Subject<Option<Value>> {
        val subject = subjectMap[key]
        if (subject != null) {
            return subject
        }
        val processor = PublishSubject.create<Option<Value>>().toSerialized()
        synchronized(subjectMap) {
            subjectMap.put(key, processor)
        }
        return subjectMap[key] ?: processor
    }

    private fun getValue(key: Key): Option<Value> {
        return Maybe.fromCallable {
            dao.querySingle(key) != null
        }
                .filter { it }
                .map { Option.ofObj(dao.querySingle(key)) }
                .blockingGet(Option.none<Value>())
    }

    private fun publishSingleSubjects() {
        for (subject in subjectMap) {
            subject.value.onNext(Option.ofObj(dao.querySingle(subject.key)))
        }
    }

    private fun publishAllSubject() {
        allSubject.onNext(Option.ofObj(dao.queryAll()))
    }
}