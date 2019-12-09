package com.mytaxi.rideme.network.services.api.taxifleet

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetVehiclesListResponse(
    @Expose @SerializedName("poiList") val poiList: List<Poi>?
) {
    data class Poi(
        @Expose @SerializedName("id") val id: Int?,
        @Expose @SerializedName("fleetType") val fleetType: String?,
        @Expose @SerializedName("heading") val heading: Double?,
        @Expose @SerializedName("coordinate") val coordinate: Coordinate?
    ) {
        data class Coordinate(
            @Expose @SerializedName("latitude") val latitude: Double?,
            @Expose @SerializedName("longitude") val longitude: Double?
        )
    }
}