package com.mytaxi.rideme.features.vehicles.list

import android.location.Location
import androidx.lifecycle.ViewModel
import com.mytaxi.rideme.app.FragmentScope
import com.mytaxi.rideme.archextensions.ViewModelKey
import com.mytaxi.rideme.base.domain.ReactiveInteractor
import com.mytaxi.rideme.data.repositories.taxifleetrepository.Poi
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
abstract class ListModule {

    @Module
    companion object {
        const val GET_TAXI_FLEET_INTERACTOR = "GET_TAXI_FLEET_INTERACTOR"
    }

    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(ListViewModel::class)
    abstract fun provideListViewModel(listViewModel: ListViewModel)
            : ViewModel

    @Binds
    @FragmentScope
    @Named(GET_TAXI_FLEET_INTERACTOR)
    abstract fun provideGetTaxiFleetInteractor(getTaxiFleetInteractor: GetTaxiFleetInteractor):
            ReactiveInteractor.RetrieveStreamInteractorWithParams<@JvmSuppressWildcards MapBoundsData, List<Poi>>

    @Suppress("UNUSED")
    @Binds
    abstract fun provideLocationProviderInteractor (getLocationProviderInteractor: GetLocationProviderInteractor)
            : ReactiveInteractor.RetrieveStreamInteractor<Location>

}