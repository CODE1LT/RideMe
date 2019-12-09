package com.mytaxi.rideme.data.repositories.taxifleetrepository

import android.content.Context
import androidx.room.Room
import com.mytaxi.rideme.app.ApplicationScope
import com.mytaxi.rideme.base.data.ReactiveStore
import com.mytaxi.rideme.data.stores.room.Dao
import com.mytaxi.rideme.data.stores.room.RoomStore
import com.mytaxi.rideme.network.services.api.taxifleet.GetVehiclesListResponse
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.functions.Function
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import polanski.option.Option

@Module
abstract class TaxiFleetModule {

    @Binds
    @ApplicationScope
    abstract fun provideRepository(taxiFleetRepositoryImpl: TaxiFleetRepositoryImpl): TaxiFleetRepository

    @Binds
    @ApplicationScope
    abstract fun provideStore(roomStore: RoomStore<String, Poi>): ReactiveStore<String, Poi>

    @Binds
    @ApplicationScope
    abstract fun provideMapper(taxiFleetMapper: TaxiFleetMapper): Function<GetVehiclesListResponse, @JvmSuppressWildcards List<Poi>>

    @Module
    companion object {

        @Provides
        @JvmStatic
        @ApplicationScope
        fun provideExtractKeyFromFunction(): Function<Poi, String> {
            return Function { it.id }
        }

        @Provides
        @JvmStatic
        @ApplicationScope
        fun provideAllSubject(): Subject<Option<List<Poi>>> {
            return PublishSubject.create<Option<List<Poi>>>()
        }


        @Provides
        @JvmStatic
        @ApplicationScope
        fun provideSubjectMap(): HashMap<String, Subject<Option<Poi>>> {
            return HashMap()
        }

        @Provides
        @JvmStatic
        @ApplicationScope
        fun provideDatabase(context: Context): TaxiFleetDatabase {
            return Room.databaseBuilder(
                context,
                TaxiFleetDatabase::class.java,
                TaxiFleetDatabase::class.java.simpleName)
                .build()
        }

        @Provides
        @JvmStatic
        @ApplicationScope
        fun provideDao(database: TaxiFleetDatabase): Dao<String, Poi> {
            return database.getTaxiFleetDao()
        }
    }
}