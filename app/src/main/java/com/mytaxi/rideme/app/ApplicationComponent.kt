package com.mytaxi.rideme.app

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import com.mytaxi.rideme.data.repositories.RepositoriesModule
import com.mytaxi.rideme.endpointsprovider.EndpointsProviderModule
import com.mytaxi.rideme.exceptions.ExceptionsPrinterModule
import com.mytaxi.rideme.data.repositories.compassrepository.Compass
import com.mytaxi.rideme.data.repositories.locationrepository.LocationProvider
import com.mytaxi.rideme.features.vehicles.list.PoolingViewHolder
import com.mytaxi.rideme.features.vehicles.list.TaxiListAdapter
import com.mytaxi.rideme.features.vehicles.list.TaxiViewHolder
import com.mytaxi.rideme.utils.FetchAddressIntentService
import com.mytaxi.rideme.logger.LoggerModule
import com.mytaxi.rideme.network.NetworkModule
import com.mytaxi.rideme.utils.GpsUtils
import com.mytaxi.rideme.utils.UtilsModule

@ApplicationScope
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        LoggerModule::class,
        ActivityBuilderModule::class,
        NetworkModule::class,
        EndpointsProviderModule::class,
        RepositoriesModule::class,
        UtilsModule::class,
        ExceptionsPrinterModule::class]
)
interface ApplicationComponent : AndroidInjector<DaggerApplication> {

    fun inject(app: com.mytaxi.rideme.app.Application)

    fun inject(compass: Compass)

    fun inject(gpsUtils: GpsUtils)

    fun inject(locationProvider: LocationProvider)

    fun inject(fetchAddressIntentService: FetchAddressIntentService)

    fun inject(taxiListAdapter: TaxiListAdapter)

    fun inject(taxiViewHolder: TaxiViewHolder)

    fun inject(poolingViewHolder: PoolingViewHolder)

    override fun inject(instance: DaggerApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }
}