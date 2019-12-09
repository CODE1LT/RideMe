package com.mytaxi.rideme.network.monitor

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.mytaxi.rideme.logger.Logger
import java.lang.ref.WeakReference
import javax.inject.Inject


class NetworkMonitor @Inject constructor(
    context: Context,
    private val logger: Logger
) : NetworkCallback() {

    companion object {
        private const val TAG = "NetworkMonitor"
    }

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()
    private val networkMonitorListeners = ArrayList<WeakReference<NetworkMonitorListener>>()

    init {
        connectivityManager.registerNetworkCallback(networkRequest, this)
    }

    fun addListener(networkMonitorListener: NetworkMonitorListener) {
        logger.d(TAG, "addListener() $networkMonitorListener")
        val notAlreadyAdded = networkMonitorListeners.find {
            networkMonitorListener == it.get()
        } == null
        if (notAlreadyAdded) {
            networkMonitorListeners.add(WeakReference(networkMonitorListener))
            logger.d(TAG, "addListener() $networkMonitorListener added")
        } else {
            logger.d(TAG, "addListener() $networkMonitorListener already added")
        }
    }

    fun removeListener(networkMonitorListener: NetworkMonitorListener) {
        logger.d(TAG, "removeListener() $networkMonitorListener")
        val listener = networkMonitorListeners.find {
            networkMonitorListener == it.get()
        }
        listener?.let {
            networkMonitorListeners.remove(it)
            logger.d(TAG, "removeListener() $networkMonitorListener removed")
        } ?: logger.d(TAG, "removeListener() $networkMonitorListener not found")
    }

    override fun onAvailable(network: Network) {
        logger.d(TAG, "onAvailable()")
        networkMonitorListeners.forEach {
            it.get()?.onNetworkAvailable()
        }
    }

    override fun onLost(network: Network?) {
        logger.d(TAG, "onLost()")
        networkMonitorListeners.forEach {
            it.get()?.onNetworkUnavailable()
        }
    }
}