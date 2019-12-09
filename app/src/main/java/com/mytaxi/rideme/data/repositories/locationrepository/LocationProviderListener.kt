package com.mytaxi.rideme.data.repositories.locationrepository

import android.location.Location

interface LocationProviderListener {
    fun onNewLocation(location: Location)
}