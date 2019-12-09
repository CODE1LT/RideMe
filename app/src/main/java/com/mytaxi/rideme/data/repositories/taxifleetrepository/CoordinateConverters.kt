package com.mytaxi.rideme.data.repositories.taxifleetrepository

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Suppress("UNUSED")
class CoordinateConverters {

    @TypeConverter
    fun toType(value: String?): Poi.Coordinate? {
        if (value != null) {
            val coordinate = object : TypeToken<Poi.Coordinate>() {}.type
            return Gson().fromJson<Poi.Coordinate>(value, coordinate)
        }
        return null
    }

    @TypeConverter
    fun fromType(coordinate: Poi.Coordinate?): String? {
        if (coordinate != null) {
            return Gson().toJson(coordinate)
        }
        return null
    }
}
