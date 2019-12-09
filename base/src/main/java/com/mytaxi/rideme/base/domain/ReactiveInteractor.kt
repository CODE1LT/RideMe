package com.mytaxi.rideme.base.domain

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by kblazys on 2018-03-23.
 */
/**
 * Interactors represents use cases. Each use case, which uses data layer, MUST implement one of these interfaces.
 */

@Suppress("UNUSED")
interface ReactiveInteractor {
    /**
     * Returns an infinite stream of objects.
     * @param Params required parameters for retrieving.
     * @param Object type of object to be returned.
     */
    interface RetrieveStreamInteractorWithParams<in Params, Object> : ReactiveInteractor {
        fun getStream(params: Params): Observable<Object>
    }

    /**
     * Returns an infinite stream of objects.
     * @param Object type of object to be returned.
     */
    interface RetrieveStreamInteractor<Object> : ReactiveInteractor {
        fun getStream(): Observable<Object>
    }

    /**
     * Returns an Single of object.
     * @param Params required parameters for retrieving.
     * @param Object type of object to be returned.
     */
    interface RetrieveSingleInteractorWithParams<in Params, Object> : ReactiveInteractor {
        fun getSingle(params: Params): Single<Object>
    }

    /**
     * Returns an Single of object.
     * @param Object type of object to be returned.
     */
    interface RetrieveSingleInteractor<Object> : ReactiveInteractor {
        fun getSingle(): Single<Object>
    }

    /**
     * Synchronously returns an object.
     * @param Params required parameters for retrieving.
     * @param Object type of object to be returned.
     */
    interface SyncRetrieveInteractorWithParams<in Params, Object> : ReactiveInteractor {
        fun getObject(params: Params): Object
    }

    /**
     * Synchronously returns an object.
     * @param Object type of object to be returned.
     */
    interface SyncRetrieveInteractor<Object> : ReactiveInteractor {
        fun getObject(): Object
    }

    /**
     * Requests data layer for any operation
     * @param Params required parameters for request.
     */
    interface RequestInteractorWithParams<Params> : ReactiveInteractor {
        fun request(params: Params): Completable
    }

    /**
     * Requests data layer for any operation
     */
    interface RequestInteractor : ReactiveInteractor {
        fun request(): Completable
    }

    /**
     * Synchronously requests data layer for any operation
     * @param Params required parameters for request.
     */
    interface SyncRequestInteractorWithParams<Params> : ReactiveInteractor {
        fun request(params: Params)
    }

    /**
     * Synchronously Requests data layer for any operation
     */
    interface SyncRequestInteractor : ReactiveInteractor {
        fun request()
    }
}