package com.mytaxi.rideme.data.repositories.taxifleetrepository

import com.mytaxi.rideme.base.UnwrapOptionTransformer
import com.mytaxi.rideme.base.data.ReactiveStore
import com.mytaxi.rideme.features.vehicles.list.MapBoundsData
import com.mytaxi.rideme.logger.Logger
import com.mytaxi.rideme.network.services.api.taxifleet.GetVehiclesListResponse
import com.mytaxi.rideme.network.services.api.taxifleet.VehiclesListService
import com.mytaxi.rideme.utils.NetworkConnectionUtils
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TaxiFleetRepositoryImpl @Inject constructor(
    private val logger: Logger,
    private val getVehiclesListService: VehiclesListService,
    private val taxiFleetMapper: Function<GetVehiclesListResponse, List<Poi>>,
    private val taxiFleetStore: ReactiveStore<String, Poi>,
    private val networkConnectionUtils: NetworkConnectionUtils) : TaxiFleetRepository {

    companion object {
        private const val TAG = "TaxiFleetRepositoryImpl"
        private const val MIN_UPDATE_TIME = 0
    }

    private var updateTime = 0L

    override fun getTaxiFleet(params: MapBoundsData): Observable<List<Poi>> {
        logger.d(TAG, "getTaxiFleet()")
        return taxiFleetStore.getAll()
            .firstOrError()
            .toObservable()
            .flatMap {
                if (it.isNone || shouldDataBeUpdated()
                ) {
                    updateTime = System.currentTimeMillis()
                    fetchTaxiFleet(
                        params.p1Lat,
                        params.p1Lon,
                        params.p2Lat,
                        params.p2Lon)
                        .andThen(taxiFleetStore.getAll()).compose(UnwrapOptionTransformer.create())
                } else {
                    taxiFleetStore.getAll().compose(UnwrapOptionTransformer.create())
                }
            }
    }


    override fun fetchTaxiFleet(p1Lat: String,
                                p1Lon: String,
                                p2Lat: String,
                                p2Lon: String): Completable {
        logger.d(TAG, "fetchTaxiFleet(), p1Lat=$p1Lat, p1Lon=$p1Lon, p2Lat=$p2Lat, p2Lon=$p2Lon,")
        return getVehiclesListService.getVehiclesList(p1Lat, p1Lon, p2Lat, p2Lon)
            .onErrorResumeNext { Single.error(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { getVehiclesListServiceResponse ->
                taxiFleetMapper.apply(getVehiclesListServiceResponse)
            }
            .flatMapCompletable {
                taxiFleetStore.replaceAll(it)
            }
    }

    private fun shouldDataBeUpdated() =
        (System.currentTimeMillis() - updateTime) > MIN_UPDATE_TIME && networkConnectionUtils.isNetworkAvailable()

}