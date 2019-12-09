package com.mytaxi.rideme.features.vehicles.selector

import com.mytaxi.rideme.app.FragmentScope
import com.mytaxi.rideme.features.vehicles.list.ListFragment
import com.mytaxi.rideme.features.vehicles.list.ListModule
import com.mytaxi.rideme.features.vehicles.map.MapFragment
import com.mytaxi.rideme.features.vehicles.map.MapModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class VehiclesFragmentsBuilderModule {

    @Suppress("UNUSED")
    @ContributesAndroidInjector(modules = [ListModule::class])
    @FragmentScope
    internal abstract fun bindListFragment(): ListFragment

    @Suppress("UNUSED")
    @ContributesAndroidInjector(modules = [MapModule::class])
    @FragmentScope
    internal abstract fun bindMapFragment(): MapFragment

}