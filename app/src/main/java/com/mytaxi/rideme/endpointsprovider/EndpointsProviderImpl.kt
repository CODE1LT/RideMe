package com.mytaxi.rideme.endpointsprovider

import javax.inject.Inject

class EndpointsProviderImpl @Inject constructor() : EndpointsProvider {

    override fun getBaseUrl(): String {
        return " https://fake-poi-api.mytaxi.com"
    }

}
