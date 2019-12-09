package com.mytaxi.rideme.exceptions

import com.mytaxi.rideme.BuildConfig

class ExceptionsPrinterLocal : ExceptionsPrinter {
    override fun print(throwable: Throwable) {
        if (BuildConfig.DEBUG_MODE) {
            throwable.printStackTrace()
        }
    }
}