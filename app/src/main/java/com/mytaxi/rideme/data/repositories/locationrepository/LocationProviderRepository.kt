package com.mytaxi.rideme.data.repositories.locationrepository

import android.location.Location
import io.reactivex.Completable
import io.reactivex.Observable

interface LocationProviderRepository {
    fun getLocation(): Observable<Location>
    fun startLocationProvider(): Completable
    fun stopLocationProvider(): Completable
}