package com.mytaxi.rideme.data.repositories.taxifleetrepository

import com.mytaxi.rideme.features.vehicles.list.MapBoundsData
import io.reactivex.Completable
import io.reactivex.Observable

interface TaxiFleetRepository {
    fun getTaxiFleet(params: MapBoundsData): Observable<List<Poi>>
    fun fetchTaxiFleet(p1Lat: String, p1Lon: String, p2Lat: String, p2Lon: String): Completable
}