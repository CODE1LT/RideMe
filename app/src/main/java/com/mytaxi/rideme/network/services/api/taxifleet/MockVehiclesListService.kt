package com.mytaxi.rideme.network.services.api.taxifleet

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mytaxi.rideme.R
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException
import io.reactivex.Single

@Suppress("UNUSED")
class MockVehiclesListService(private val mContext: Context) : VehiclesListService {

    companion object {
        private val TAG = VehiclesListService::class.java.simpleName
    }

    private val mGson = Gson()

    override fun getVehiclesList(p1Lat: String, p1Lon: String, p2Lat: String, p2Lon: String): Single<GetVehiclesListResponse> {
        val type = object : TypeToken<GetVehiclesListResponse>() {

        }.type
        try {
            return Single.just(
                mGson.fromJson(
                    InputStreamReader(
                        mContext.resources.openRawResource(R.raw.mock_vehicles_list_response),
                        "UTF-8"),
                    type))
        } catch (e: UnsupportedEncodingException) {
            throw IllegalArgumentException("$TAG: Error parsing from file")
        }

    }

}