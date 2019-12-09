package com.mytaxi.rideme.features.vehicles.list

import com.mytaxi.rideme.base.domain.ReactiveInteractor
import com.mytaxi.rideme.data.repositories.taxifleetrepository.Poi
import com.mytaxi.rideme.data.repositories.taxifleetrepository.TaxiFleetRepository
import com.mytaxi.rideme.logger.Logger
import io.reactivex.Observable
import javax.inject.Inject

class GetTaxiFleetInteractor @Inject constructor(
    private val logger: Logger,
    private val taxiFleetRepository: TaxiFleetRepository
) : ReactiveInteractor.RetrieveStreamInteractorWithParams<MapBoundsData, @JvmSuppressWildcards List<Poi>> {

    companion object {
        private const val TAG = "GetTaxiFleetInteractor"
    }

    override fun getStream(params: MapBoundsData): Observable<List<Poi>> {
        logger.d(TAG, "getStream()")
        return taxiFleetRepository.getTaxiFleet(params)
    }
}