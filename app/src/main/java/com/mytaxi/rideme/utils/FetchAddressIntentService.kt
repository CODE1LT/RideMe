package com.mytaxi.rideme.utils

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import com.mytaxi.rideme.R
import com.mytaxi.rideme.app.Application
import com.mytaxi.rideme.logger.Logger
import java.io.IOException
import java.util.*
import javax.inject.Inject

class FetchAddressIntentService : IntentService("FetchAddressIntentService") {

    companion object {
        private const val TAG = "FetchAddressIntentService"
        const val SUCCESS_RESULT = 0
        const val FAILURE_RESULT = 1
        private const val PACKAGE_NAME = "com.mytaxi.rideme.features.compass"
        const val RECEIVER = "$PACKAGE_NAME.RECEIVER"
        const val RESULT_DATA_KEY = "$PACKAGE_NAME.RESULT_DATA_KEY"
        const val LOCATION_DATA_EXTRA = "$PACKAGE_NAME.LOCATION_DATA_EXTRA"
    }

    @Inject
    lateinit var logger: Logger

    init {
        Application.appComponent.inject(this)
    }

    private var receiver: ResultReceiver? = null

    override fun onHandleIntent(intent: Intent?) {
        var errorMessage = ""
        receiver = intent?.getParcelableExtra(RECEIVER)

        if (intent == null || receiver == null) {
            logger.e(TAG, getString(R.string.s_fetch_address_intent_no_intent_or_receiver))
            return
        }

        // Get the location passed to this service through an extra.
        val location = intent.getParcelableExtra(LOCATION_DATA_EXTRA) as Location
        if (location == null) {
            errorMessage = getString(R.string.s_fetch_address_intent_no_location_data)
            logger.e(TAG, errorMessage)
            deliverResultToReceiver(FAILURE_RESULT, errorMessage)
            return
        }

        val geocoder = Geocoder(this, Locale.getDefault())
        var addresses: List<Address> = emptyList()

        try {
            addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                // In this sample, we get just a single address.
                1)
        } catch (ioException: IOException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.s_fetch_address_intent_not_available)
            logger.e(TAG, "$errorMessage Error code: $ioException")
        } catch (illegalArgumentException: IllegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.s_fetch_address_intent_invalid_lat_long)
            logger.e(
                TAG, "$errorMessage. Latitude = $location.latitude , " +
                    "Longitude =  $location.longitude" + ". Error code: " + illegalArgumentException)
        }

        // Handle case where no address was found.
        if (addresses.isEmpty()) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.s_fetch_address_intent_no_address_found)
                logger.e(TAG, errorMessage)
            }
            deliverResultToReceiver(FAILURE_RESULT, errorMessage)
        } else {
            val address = addresses[0]
            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            val addressFragments = with(address) {
                (0..maxAddressLineIndex).map { getAddressLine(it) }
            }
            logger.d(TAG, getString(R.string.s_fetch_address_intent_address_found))
            deliverResultToReceiver(
                SUCCESS_RESULT,
                addressFragments.joinToString(separator = "\n"))
        }

    }

    private fun deliverResultToReceiver(resultCode: Int, message: String) {
        val bundle = Bundle().apply { putString(RESULT_DATA_KEY, message) }
        receiver?.send(resultCode, bundle)
    }

}