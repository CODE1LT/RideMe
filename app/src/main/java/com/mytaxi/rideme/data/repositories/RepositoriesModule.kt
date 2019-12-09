package com.mytaxi.rideme.data.repositories

import dagger.Module
import com.mytaxi.rideme.data.repositories.compassrepository.CompassRepositoryModule
import com.mytaxi.rideme.data.repositories.locationrepository.LocationProviderRepositoryModule
import com.mytaxi.rideme.data.repositories.taxifleetrepository.TaxiFleetModule

@Module(
    includes = [
        CompassRepositoryModule::class,
        LocationProviderRepositoryModule::class,
        TaxiFleetModule::class
    ]
)
class RepositoriesModule