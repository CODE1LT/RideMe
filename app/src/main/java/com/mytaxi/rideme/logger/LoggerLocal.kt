package com.mytaxi.rideme.logger

import android.util.Log
import com.mytaxi.rideme.BuildConfig

class LoggerLocal : Logger {

    override fun d(tag: String, msg: String) {
        if (BuildConfig.DEBUG_MODE) {
            Log.d(tag, msg)
        }
    }

    override fun e(tag: String, msg: String) {
        if (BuildConfig.DEBUG_MODE) {
            Log.e(tag, msg)
        }
    }
}