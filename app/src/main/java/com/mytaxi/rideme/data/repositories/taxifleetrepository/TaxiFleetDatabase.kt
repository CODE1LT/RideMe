package com.mytaxi.rideme.data.repositories.taxifleetrepository

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Poi::class], version = 1)
@TypeConverters(CoordinateConverters::class)
abstract class TaxiFleetDatabase : RoomDatabase() {
    abstract fun getTaxiFleetDao(): TaxiFleetDao
}