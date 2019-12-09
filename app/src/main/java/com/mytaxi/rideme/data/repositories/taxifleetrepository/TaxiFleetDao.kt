package com.mytaxi.rideme.data.repositories.taxifleetrepository

import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class TaxiFleetDao : com.mytaxi.rideme.data.stores.room.Dao<String, Poi> {

    @Query("SELECT * FROM poi")
    abstract override fun queryAll(): List<Poi>

    @Query("SELECT * FROM poi WHERE id = :key")
    abstract override fun querySingle(key: String): Poi

    @Query("DELETE FROM poi")
    abstract override fun clear()

    @Query("DELETE FROM poi where id = :key")
    abstract override fun removeSingle(key: String)
}