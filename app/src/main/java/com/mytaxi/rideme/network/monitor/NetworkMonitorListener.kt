package com.mytaxi.rideme.network.monitor

interface NetworkMonitorListener {

    fun onNetworkAvailable()

    fun onNetworkUnavailable()
}