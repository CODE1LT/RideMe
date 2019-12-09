package com.mytaxi.rideme.network.services.api.taxifleet

import io.reactivex.Single
import retrofit2.http.*

interface VehiclesListService {

    @GET("/")
    fun getVehiclesList(
        @Query("p1Lat") p1Lat: String,
        @Query("p1Lon") p1Lon: String,
        @Query("p2Lat") p2Lat: String,
        @Query("p2Lon") p2Lon: String
    ): Single<GetVehiclesListResponse>

}