package com.mytaxi.rideme.base.data

import io.reactivex.Completable
import io.reactivex.Observable
import polanski.option.Option

interface ReactiveStore<Key, Value> {
    /**
     * Stores single value to reactive store.
     * After calling storeSingular, getSingular with stored key and getAll should emit.
     * @param value Single value to store.
     * @return Completable
     */
    fun storeSingular(value: Value) : Completable

    /**
     * Stores list of values to reactive store.
     * After calling storeAll, all getSingular and getAll should emit.
     * @param values list of values to store.
     * @return Completable
     */
    fun storeAll(values: List<Value>) : Completable

    /**
     * Reaplaces all values in reactive store.
     * After calling replaceAll, all getSingular and getAll should emit.
     * @param values list of values to store.
     * @return Completable
     */
    fun replaceAll(values: List<Value>) : Completable

    /**
     * Returns an infinite stream of singular value.
     * @param key Key of object to retrieve.
     * @return observable of singular value.
     */
    fun getSingular(key: Key) : Observable<Option<Value>>

    /**
     * Returns an infinite stream of values list.
     * @return observable of values list.
     */
    fun getAll() : Observable<Option<List<Value>>>

    /**
     * Clears all values in reactive store.
     * After calling clear, all getSingular and getAll should emit.
     * @return Completable
     */
    fun clear() : Completable

    /**
     * Clears value in reactive store.
     * After calling clear, getSingular with stored key  and getAll should emit.
     * @return Completable
     */
    fun removeSingular(key: Key) : Completable
}