package com.mytaxi.rideme.features.vehicles.map

import androidx.lifecycle.ViewModel
import com.mytaxi.rideme.app.FragmentScope
import com.mytaxi.rideme.archextensions.ViewModelKey
import com.mytaxi.rideme.base.domain.ReactiveInteractor
import com.mytaxi.rideme.data.repositories.taxifleetrepository.Poi
import com.mytaxi.rideme.features.vehicles.list.MapBoundsData
import com.mytaxi.rideme.features.vehicles.list.GetTaxiFleetInteractor
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
abstract class MapModule {

    @Module
    companion object {
        const val GET_TAXI_FLEET_INTERACTOR = "GET_TAXI_FLEET_INTERACTOR"
    }

    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(MapViewModel::class)
    abstract fun provideMapViewModel(mapViewModel: MapViewModel)
            : ViewModel

    @Binds
    @FragmentScope
    @Named(GET_TAXI_FLEET_INTERACTOR)
    abstract fun provideGetTaxiFleetInteractor(getTaxiFleetInteractor: GetTaxiFleetInteractor):
            ReactiveInteractor.RetrieveStreamInteractorWithParams<@JvmSuppressWildcards MapBoundsData, List<Poi>>

}