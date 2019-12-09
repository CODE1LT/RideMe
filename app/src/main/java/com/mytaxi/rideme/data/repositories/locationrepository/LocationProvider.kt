package com.mytaxi.rideme.data.repositories.locationrepository

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.*
import com.mytaxi.rideme.app.Application
import com.mytaxi.rideme.logger.Logger
import javax.inject.Inject

class LocationProvider (private val context: Context) {

    companion object {
        private const val TAG = "LocationProvider"
    }

    @Inject
    lateinit var logger: Logger

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationRequest: LocationRequest? = null
    private lateinit var locationCallback: LocationCallback
    private var locationProviderListener: LocationProviderListener? = null

    init {
        Application.appComponent.inject(this)
        init()
    }

    fun setListener(locationProviderListener: LocationProviderListener) {
        this.locationProviderListener = locationProviderListener
    }

    fun init() {
        logger.d(TAG, "init()")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationRequest = createLocationRequest()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    if (location != null) {
                        locationProviderListener?.onNewLocation(location)
                    }
                }
            }
        }
    }

    private fun createLocationRequest(): LocationRequest? {
        logger.d(TAG, "createLocationRequest()")
        return LocationRequest.create()?.apply {
            interval = 5000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    @SuppressLint("MissingPermission")
    fun start() {
        logger.d(TAG, "start()")
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */)
    }

    fun stop() {
        logger.d(TAG, "stop()")
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


}