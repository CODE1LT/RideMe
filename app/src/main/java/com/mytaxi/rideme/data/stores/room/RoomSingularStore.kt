package com.mytaxi.rideme.data.stores.room

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.subjects.Subject
import com.mytaxi.rideme.base.data.ReactiveSingularStore
import polanski.option.Option
import javax.inject.Inject

class RoomSingularStore<Value> @Inject constructor(
        private var mDao: DaoSingular<Value>,
        private var mSubject: Subject<Option<Value>>)
    : ReactiveSingularStore<Value> {

    override fun storeSingular(value: Value): Completable {
        return Completable.create {
            mDao.insert(value)
            publishSubject()
            it.onComplete()
        }
    }

    override fun getSingular(): Observable<Option<Value>> {
        return Observable.defer<Option<Value>> {
            mSubject.startWith(getValue())
        }
    }

    override fun clear(): Completable {
        return Completable.create {
            mDao.clear()
            publishSubject()
            it.onComplete()
        }
    }

    private fun getValue(): Option<Value> {
        return Maybe.fromCallable {
            mDao.query() != null
        }
                .filter { it }
                .map { mDao.query() }
                .map { Option.ofObj(it) }
                .blockingGet(Option.none<Value>())
    }

    private fun publishSubject() {
        mSubject.onNext(Option.ofObj(mDao.query()))
    }

}