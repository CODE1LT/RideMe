package com.mytaxi.rideme.data.stores.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface DaoSingular<Value> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(value: Value)

    fun query(): Value

    fun clear()
}