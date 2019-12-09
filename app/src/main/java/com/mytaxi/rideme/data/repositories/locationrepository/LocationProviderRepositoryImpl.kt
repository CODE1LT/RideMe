package com.mytaxi.rideme.data.repositories.locationrepository

import android.location.Location
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import com.mytaxi.rideme.logger.Logger
import javax.inject.Inject

class LocationProviderRepositoryImpl @Inject constructor(
    private val logger: Logger,
    private val locationProvider: LocationProvider
): LocationProviderRepository, LocationProviderListener  {

    companion object {
        private const val TAG = "LocationProviderRepositoryImpl"
    }

    private val locationSubject = PublishSubject.create<Location>()

    override fun getLocation(): Observable<Location> {
        logger.d(TAG, "getLocation()")
        return locationSubject
    }

    override fun onNewLocation(location: Location) {
        locationSubject.onNext(location)
    }

    override fun startLocationProvider(): Completable {
        logger.d(TAG, "startLocationProvider()")
        locationProvider.setListener(this)
        locationProvider.start()
        return Completable.complete()
    }

    override fun stopLocationProvider(): Completable {
        logger.d(TAG, "stopLocationProvider()")
        locationProvider.stop()
        return Completable.complete()
    }

}