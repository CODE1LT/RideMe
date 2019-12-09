package com.mytaxi.rideme.features.vehicles.map

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.mytaxi.rideme.R
import com.mytaxi.rideme.base.BaseViewModelFragment
import com.mytaxi.rideme.databinding.FragmentMapBinding
import com.mytaxi.rideme.features.vehicles.selector.SelectorActivity
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLngBounds
import com.mytaxi.rideme.features.vehicles.list.MapBoundsData

class MapFragment : BaseViewModelFragment(), OnMapReadyCallback,
    GoogleMap.OnCameraIdleListener {

    companion object {
        private const val TAG = "MapFragment"
        private const val TAXI = "TAXI"
    }

    private lateinit var mapViewModel: MapViewModel
    private lateinit var mapViewDataBinding: FragmentMapBinding
    private var bundle: Bundle? = null
    private var map: GoogleMap? = null
    //TODO just for test, Hamburg city area
    private val northWestEdge = LatLng(53.694865, 9.757589)
    private val southEastEdge = LatLng(53.394655, 10.099891)

    override fun getViewModelClass(): Class<*> = MapViewModel::class.java
    override fun getLayoutId(): Int = R.layout.fragment_map
    override fun dataBindingEnabled(): Boolean = true
    override fun getFragmentTag(): String = TAG

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.d(TAG, "onViewCreated()")
        initialize()
        getAndObserveViewLiveData()
    }

    private fun initialize() {
        logger.d(TAG, "initialize()")
        bundle = this.arguments
        mapViewModel = viewModel as MapViewModel
        mapViewDataBinding = viewDataBinding as FragmentMapBinding
        val map = (childFragmentManager.findFragmentById(R.id.f_map_map) as SupportMapFragment)
        map.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        logger.d(TAG, "onMapReady()")
        map = googleMap
        googleMap.setOnCameraIdleListener(this)
        googleMap.uiSettings.isZoomControlsEnabled = true
        showTaxiOnMap()
    }

    private fun showTaxiOnMap() {
        logger.d(TAG, "showTaxiOnMap()")
        if (bundle != null) {
            val taxiType = (bundle as Bundle).getString(SelectorActivity.FLEET_TYPE, "")
            val taxiLatitude = (bundle as Bundle).getDouble(SelectorActivity.LATITUDE, 0.0)
            val taxiLongitude = (bundle as Bundle).getDouble(SelectorActivity.LONGITUDE, 0.0)
            val position = LatLng(taxiLatitude, taxiLongitude)
            if (map != null) {
                if (taxiType == TAXI) {
                    (map as GoogleMap).addMarker(
                        MarkerOptions().position(position).icon(
                            BitmapDescriptorFactory.fromResource(R.drawable.taxi)))
                } else {
                    (map as GoogleMap).addMarker(
                        MarkerOptions().position(position).icon(
                            BitmapDescriptorFactory.fromResource(R.drawable.carpool)))
                }
                (map as GoogleMap).animateCamera(
                    CameraUpdateFactory.newLatLngZoom(position, 15.0f),
                    300,
                    null)
            }
        } else {
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(getHamburgBounds(), 0)
            (map as GoogleMap).animateCamera(cameraUpdate, 500, null)
        }
    }

    private fun getHamburgBounds(): LatLngBounds {
        val builder = LatLngBounds.Builder()
        builder.include(northWestEdge)
        builder.include(southEastEdge)
        return builder.build()
    }

    override fun onCameraIdle() {
        if (bundle == null) {
            map?.setPadding(100,100,100,100)
            val bounds = map?.projection?.visibleRegion?.latLngBounds
            map?.setPadding(0,0,0,0)
            val remappedBounds =
                MapBoundsData(
                    bounds?.northeast?.latitude.toString(), bounds?.northeast?.longitude.toString(),
                    bounds?.southwest?.latitude.toString(), bounds?.southwest?.longitude.toString())
            mapViewModel.getTaxiFleet(remappedBounds)
        }
    }

    private fun getAndObserveViewLiveData() {
        logger.d(TAG, "getAndObserveViewLiveData()")
        mapViewModel.getTaxiListViewLiveData().observe(this, Observer {
            map?.clear()
            it.poiList.forEach { poi ->
                val latitude = poi.coordinate?.latitude
                val longitude = poi.coordinate?.longitude
                if (latitude != null && longitude != null) {
                    val position = LatLng(latitude, longitude)
                    if (poi.fleetType == TAXI) {
                        (map as GoogleMap).addMarker(
                            MarkerOptions().position(position).icon(
                                BitmapDescriptorFactory.fromResource(R.drawable.taxi)))
                    } else {
                        (map as GoogleMap).addMarker(
                            MarkerOptions().position(position).icon(
                                BitmapDescriptorFactory.fromResource(R.drawable.carpool)))
                    }
                }
            }
        })
    }

}