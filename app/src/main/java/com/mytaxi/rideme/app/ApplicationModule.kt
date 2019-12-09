package com.mytaxi.rideme.app

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module

@Suppress("UNUSED")
@Module
abstract class ApplicationModule {

    @Binds
    @ApplicationScope
    abstract fun provideContext(application: Application): Context
}