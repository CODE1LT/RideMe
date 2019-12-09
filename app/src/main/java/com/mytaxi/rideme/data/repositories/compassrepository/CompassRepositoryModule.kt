package com.mytaxi.rideme.data.repositories.compassrepository

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import com.mytaxi.rideme.app.ApplicationScope


@Module
abstract class CompassRepositoryModule {

    @Module
    companion object {
        @Provides
        @ApplicationScope
        @JvmStatic
        fun provideCompass(context: Context): Compass {
            return Compass(context)
        }
    }

    @Binds
    @ApplicationScope
    @Suppress("UNUSED")
    abstract fun provideCompassRepository(compassRepositoryImpl: CompassRepositoryImpl): CompassRepository


}