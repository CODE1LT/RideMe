package com.mytaxi.rideme.features.vehicles.list

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.mytaxi.rideme.R
import com.mytaxi.rideme.app.Application
import com.mytaxi.rideme.data.repositories.taxifleetrepository.Poi
import com.mytaxi.rideme.features.vehicles.selector.FragmentsListener
import com.mytaxi.rideme.logger.Logger
import com.mytaxi.rideme.utils.FetchAddressIntentService
import com.mytaxi.rideme.utils.FetchAddressIntentService.Companion.LOCATION_DATA_EXTRA
import com.mytaxi.rideme.utils.FetchAddressIntentService.Companion.RECEIVER
import com.mytaxi.rideme.utils.FetchAddressIntentService.Companion.RESULT_DATA_KEY
import com.mytaxi.rideme.utils.SotwFormatter
import kotlinx.android.synthetic.main.item_taxi.view.*
import javax.inject.Inject

class TaxiViewHolder @Inject constructor(taxiView: View,
                                         private val viewLiveData: MutableLiveData<ListViewData>) :
    RecyclerView.ViewHolder(taxiView), View.OnClickListener {

    companion object {
        private const val TAG = "ListViewHolder"
    }

    @Inject
    lateinit var context: Context
    @Inject
    lateinit var logger: Logger
    @Inject
    lateinit var sotwFormatter: SotwFormatter

    private val clTaxiCard = taxiView.i_taxi_cl
    private val tvAddress = taxiView.i_taxi_tv_address_text
    private val tvMovingTo = taxiView.i_taxi_tv_direction_text
    private val tvDistance = taxiView.i_taxi_tv_distance_text
    private val tvLatitude = taxiView.i_taxi_tv_latitude_text
    private val tvLongitude = taxiView.i_taxi_tv_longitude_text
    private lateinit var poi: Poi
    private var resultReceiver: AddressResultReceiver
    private lateinit var fragmentsListener: FragmentsListener

    init {
        Application.appComponent.inject(this)
        resultReceiver = AddressResultReceiver(Handler())
        val listener = viewLiveData.value?.fragmentsListener
        if (listener != null) {
            fragmentsListener = listener
        }
    }

    internal inner class AddressResultReceiver(handler: Handler) : ResultReceiver(handler) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            tvAddress.text = resultData?.getString(RESULT_DATA_KEY) ?: "-"
        }
    }

    fun bind(poi: Poi) {
        logger.d(TAG, "bind()")
        this.poi = poi
        tvAddress.text = "-"
        val heading = poi.heading?.toFloat()
        if (heading != null) {
            tvMovingTo.text = sotwFormatter.format(heading)
        }
        val latitude = poi.coordinate?.latitude
        val longitude = poi.coordinate?.longitude
        val targetLocation = Location("")
        val currentLocation = viewLiveData.value?.location
        if (latitude != null && longitude != null && currentLocation != null) {
            targetLocation.latitude = latitude
            targetLocation.longitude = longitude
            val distance = String.format("%.2f",(currentLocation.distanceTo(targetLocation)/1000))
            val distanceMeasure = context.getString(R.string.distance_measure_km)
            tvDistance.text = StringBuilder().append(distance).append(" ").append(distanceMeasure).toString()
        }
        tvLatitude.text = poi.coordinate?.latitude.toString()
        tvLongitude.text = poi.coordinate?.longitude.toString()
        startIntentService(targetLocation)
        clTaxiCard.setOnClickListener(this)
    }

    private fun startIntentService(lastLocation: Location) {
        val intent = Intent(context, FetchAddressIntentService::class.java).apply {
            putExtra(RECEIVER, resultReceiver)
            putExtra(LOCATION_DATA_EXTRA, lastLocation)
        }
        context.startService(intent)
    }


    override fun onClick(v: View) {
        logger.d(TAG, "onClick()")
        val fleetType = poi.fleetType
        val latitude = poi.coordinate?.latitude
        val longitude = poi.coordinate?.longitude
        if (fleetType != null && latitude != null && longitude != null) {
            val taxiData = TaxiData(fleetType, latitude, longitude)
            fragmentsListener.onTaxiCardClicked(taxiData)
        }
    }

}