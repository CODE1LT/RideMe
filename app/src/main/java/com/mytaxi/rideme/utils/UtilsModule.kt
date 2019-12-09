package com.mytaxi.rideme.utils

import android.content.Context
import dagger.Module
import dagger.Provides
import com.mytaxi.rideme.app.ApplicationScope

@Module
class UtilsModule {

    @Provides
    @ApplicationScope
    fun provideNetworkConnectionUtils(context: Context): NetworkConnectionUtils {
        return NetworkConnectionUtils(context)
    }

    @Provides
    @ApplicationScope
    fun providePermissionsUtils(): PermissionsUtils {
        return PermissionsUtils()
    }

    @Provides
    @ApplicationScope
    fun provideGpsUtils(): GpsUtils {
        return GpsUtils()
    }

    @Provides
    @ApplicationScope
    fun provideSotwFormatter(context: Context): SotwFormatter {
        return SotwFormatter(context)
    }
}