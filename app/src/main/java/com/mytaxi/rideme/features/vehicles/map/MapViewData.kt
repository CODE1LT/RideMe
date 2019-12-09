package com.mytaxi.rideme.features.vehicles.map

import android.location.Location
import com.mytaxi.rideme.customviews.generalmessage.GeneralMessage
import com.mytaxi.rideme.data.repositories.taxifleetrepository.Poi

data class MapViewData(
    var poiList: List<Poi> = arrayListOf(),
    var location: Location = Location(""),
    var generalMessage: GeneralMessage = GeneralMessage()
)