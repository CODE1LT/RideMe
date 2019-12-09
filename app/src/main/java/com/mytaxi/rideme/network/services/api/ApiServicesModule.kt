package com.mytaxi.rideme.network.services.api

import dagger.Module
import dagger.Provides
import com.mytaxi.rideme.app.ApplicationScope
import com.mytaxi.rideme.network.NetworkModule
import com.mytaxi.rideme.network.services.api.taxifleet.VehiclesListService
import retrofit2.Retrofit
import javax.inject.Named

@Module
class ApiServicesModule {

    //Comment when mocking the endpoint
    @Provides
    @ApplicationScope
    fun provideVehiclesListService(@Named(NetworkModule.RETROFIT_API_NO_AUTH) retrofit: Retrofit): VehiclesListService {
        return retrofit.create(VehiclesListService::class.java)
    }

    //Uncomment for mocking the endpoint

//    @Provides
//    @ApplicationScope
//    fun provideVehiclesListService(context: Context): VehiclesListService {
//        return MockVehiclesListService(context)
//    }

}
