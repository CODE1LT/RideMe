package com.mytaxi.rideme.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat.checkSelfPermission
import java.util.ArrayList

class PermissionsUtils {

    companion object {
        private const val ALL_PERMISSIONS_RESULT = 1011
    }

    private val permissions = ArrayList<String>()
    private var permissionsToRequest: ArrayList<String>? = null
    private val permissionsRejected = ArrayList<String>()
    private lateinit var activity: Activity

    fun initPermissionUtils(activity: Activity) {this.activity = activity}

    @SuppressLint("ObsoleteSdkInt")
    fun requestLocationPermissions() {
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissionsToRequest = permissionsToRequest(permissions)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest!!.size > 0) {
                requestPermissions(activity,
                    permissionsToRequest!!.toArray(
                        arrayOfNulls<String>(
                            permissionsToRequest!!.size)), ALL_PERMISSIONS_RESULT)
            }
        }
    }

    private fun permissionsToRequest(wantedPermissions: ArrayList<String>): ArrayList<String> {
        val result = ArrayList<String>()

        for (perm in wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm)
            }
        }
        return result
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun hasPermission(permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    @SuppressLint("ObsoleteSdkInt")
    fun onRequestPermissionsResult(requestCode: Int): Boolean {
        when (requestCode) {
            ALL_PERMISSIONS_RESULT -> {
                for (perm in permissionsToRequest!!) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm)
                    }
                }

                if (permissionsRejected.size > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(activity, permissionsRejected[0])) {
                            AlertDialog.Builder(activity)
                                .setMessage("These permissions are mandatory to get your location. You need to allow them.")
                                .setPositiveButton("OK"
                                ) { _, _ ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(activity,
                                            permissionsRejected.toTypedArray(),
                                            ALL_PERMISSIONS_RESULT)
                                    }
                                }.setNegativeButton("Cancel", null).create().show()
                            return false
                        }
                    }
                } else {
                    return true
                }
            }
        }
        return false
    }

}