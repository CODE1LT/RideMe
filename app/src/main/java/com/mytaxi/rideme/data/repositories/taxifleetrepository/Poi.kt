package com.mytaxi.rideme.data.repositories.taxifleetrepository

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
@TypeConverters(CoordinateConverters::class)
data class Poi(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val fleetType: String?,
    val heading: Double?,
    val coordinate: Coordinate?
) {
    data class Coordinate(
        val latitude: Double?,
        val longitude: Double?
    )
}
