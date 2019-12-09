package com.mytaxi.rideme.base.data

import io.reactivex.Completable
import io.reactivex.Observable
import polanski.option.Option

interface ReactiveSingularStore<Value> {
    /**
     * Stores single value to reactive store.
     * After calling storeSingular, getSingular should emit.
     * @param value Single value to store.
     * @return Completable
     */
    fun storeSingular(value: Value) : Completable

    /**
     * Returns an infinite stream of singular value.
     * @return observable of singular value.
     */
    fun getSingular() : Observable<Option<Value>>

    /**
     * Clears all values in reactive store.
     * After calling clear, getSingular should emit.
     * @return Completable
     */
    fun clear() : Completable
}