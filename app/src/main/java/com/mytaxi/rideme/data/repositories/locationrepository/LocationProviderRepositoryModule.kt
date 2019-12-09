package com.mytaxi.rideme.data.repositories.locationrepository

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import com.mytaxi.rideme.app.ApplicationScope


@Module
abstract class LocationProviderRepositoryModule {

    @Module
    companion object {
        @Provides
        @ApplicationScope
        @JvmStatic
        fun provideLocationProvider(context: Context): LocationProvider {
            return LocationProvider(context)
        }
    }

    @Binds
    @ApplicationScope
    abstract fun provideLocationProviderRepository(locationProviderRepositoryImpl: LocationProviderRepositoryImpl): LocationProviderRepository

}