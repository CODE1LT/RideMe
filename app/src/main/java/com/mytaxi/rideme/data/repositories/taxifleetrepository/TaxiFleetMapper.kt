package com.mytaxi.rideme.data.repositories.taxifleetrepository

import com.mytaxi.rideme.IncorrectParameterException
import com.mytaxi.rideme.network.services.api.taxifleet.GetVehiclesListResponse
import io.reactivex.functions.Function
import javax.inject.Inject


class TaxiFleetMapper @Inject constructor() : Function<GetVehiclesListResponse, @JvmSuppressWildcards List<Poi>> {

    override fun apply(response: GetVehiclesListResponse): List<Poi> {
        try {
            return getPoiList(response.poiList as ArrayList<GetVehiclesListResponse.Poi>?)
        } catch (e: Exception) {
            throw IncorrectParameterException("$response, raw= ${e.message}")
        }
    }

    private fun getPoiList(poiResponse: ArrayList<GetVehiclesListResponse.Poi>?): ArrayList<Poi> {
        val poiList = ArrayList<Poi>()
        if (poiResponse != null) {
            for (i in poiResponse.indices) {
                val id = poiResponse[i].id
                if (id != null) {
                    val poi = Poi(
                        id.toString(),
                        poiResponse[i].fleetType,
                        poiResponse[i].heading,
                        getCoordinate(poiResponse[i].coordinate)
                    )
                    poiList.add(poi)
                }
            }
        }
        return poiList
    }

    private fun getCoordinate(coordinate: GetVehiclesListResponse.Poi.Coordinate?): Poi.Coordinate? {
        if (coordinate != null) {
            return Poi.Coordinate(
                coordinate.latitude,
                coordinate.longitude
            )
        }
        return Poi.Coordinate(0.0, 0.0)
    }

}