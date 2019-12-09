package com.mytaxi.rideme.features.vehicles.selector

interface GpsStatusListener {
    fun gpsStatus(isGPSEnable: Boolean)
}