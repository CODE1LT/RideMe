package com.mytaxi.rideme.endpointsprovider

import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module
abstract class EndpointsProviderModule {

    companion object {
        const val ENDPOINTS_PROVIDER = "EndpointsProvider"
    }

    @Suppress("UNUSED")
    @Binds
    @Named(ENDPOINTS_PROVIDER)
    abstract fun provideEndpointsProviderImpl (endpointsProviderImpl: EndpointsProviderImpl): EndpointsProvider
}