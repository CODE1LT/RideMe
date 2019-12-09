package com.mytaxi.rideme.data.repositories.compassrepository

import io.reactivex.Completable
import io.reactivex.Observable


interface CompassRepository {
    fun getAzimuth(): Observable<Float>
    fun startCompass(): Completable
    fun stopCompass(): Completable
}