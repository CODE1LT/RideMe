package com.mytaxi.rideme.features.vehicles.list

import android.location.Location
import com.mytaxi.rideme.base.domain.ReactiveInteractor
import com.mytaxi.rideme.data.repositories.locationrepository.LocationProviderRepository
import com.mytaxi.rideme.logger.Logger
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class GetLocationProviderInteractor @Inject constructor(
    private val logger: Logger,
    private val locationProviderRepository: LocationProviderRepository
) : ReactiveInteractor.RetrieveStreamInteractor<Location> {

    companion object {
        private const val TAG = "GetLocationProviderInteractor"
    }

    override fun getStream(): Observable<Location> {
        logger.d(TAG, "getStream()")
        return locationProviderRepository.getLocation()
    }

    fun startLocationProvider(): Completable {
        logger.d(TAG, "startLocationProvider()")
        return locationProviderRepository.startLocationProvider()
    }

    fun stopLocationProvider(): Completable {
        logger.d(TAG, "stopLocationProvider()")
        return locationProviderRepository.stopLocationProvider()
    }

}