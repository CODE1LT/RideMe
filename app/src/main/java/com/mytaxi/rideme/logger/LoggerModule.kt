package com.mytaxi.rideme.logger

import dagger.Module
import dagger.Provides

@Module
class LoggerModule {

    @Provides
    fun provideLogger(): Logger {
        return LoggerLocal()
    }
}