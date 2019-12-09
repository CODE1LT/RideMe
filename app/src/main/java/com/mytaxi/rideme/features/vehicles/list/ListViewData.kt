package com.mytaxi.rideme.features.vehicles.list

import android.location.Location
import com.mytaxi.rideme.customviews.generalmessage.GeneralMessage
import com.mytaxi.rideme.data.repositories.taxifleetrepository.Poi
import com.mytaxi.rideme.features.vehicles.selector.FragmentsListener

data class ListViewData(
    var poiList: List<Poi> = arrayListOf(),
    var location: Location = Location(""),
    var fragmentsListener: FragmentsListener,
    var generalMessage: GeneralMessage = GeneralMessage())