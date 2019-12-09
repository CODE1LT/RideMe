package com.mytaxi.rideme.data.stores.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface Dao<Key, Value> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(value: Value)

    fun queryAll(): List<Value>

    fun querySingle(key: Key) :Value

    fun clear()

    fun removeSingle(key: Key)
}