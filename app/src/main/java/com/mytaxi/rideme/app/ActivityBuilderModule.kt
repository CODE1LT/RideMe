package com.mytaxi.rideme.app

import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.mytaxi.rideme.features.vehicles.selector.SelectorActivity
import com.mytaxi.rideme.features.vehicles.selector.SelectorModule
import com.mytaxi.rideme.features.vehicles.selector.VehiclesFragmentsBuilderModule

@Module
@Suppress("unused")
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [SelectorModule::class, VehiclesFragmentsBuilderModule::class])
    @ActivityScope
    internal abstract fun bindSelectorActivity(): SelectorActivity
}
