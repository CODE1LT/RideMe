package com.mytaxi.rideme.exceptions

import dagger.Module
import dagger.Provides
import com.mytaxi.rideme.app.ApplicationScope

@Module
class ExceptionsPrinterModule {

    @Provides
    @ApplicationScope
    fun provideExceptionsPrinter(): ExceptionsPrinter {
        return ExceptionsPrinterLocal()
    }
}