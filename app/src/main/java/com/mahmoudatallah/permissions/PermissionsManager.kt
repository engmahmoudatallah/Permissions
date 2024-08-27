package com.mahmoudatallah.permissions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionsManager {

    fun checkPermissions(
        activity: Activity,
        permissions: Array<String>,
        rationale: String? = null,
        callback: PermissionHandler
    ) {
        val deniedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (deniedPermissions.isEmpty()) {
            callback.onGranted()
        } else {
            if (rationale != null && shouldShowRationale(activity, deniedPermissions)) {
                // Show rationale dialog (optional)
                // For simplicity, just requesting permissions directly
                requestPermissions(activity, deniedPermissions.toTypedArray())
            } else {
                requestPermissions(activity, deniedPermissions.toTypedArray())
            }
        }
    }

    private fun shouldShowRationale(activity: Activity, permissions: List<String>): Boolean {
        return permissions.any {
            ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
        }
    }

    private fun requestPermissions(activity: Activity, permissions: Array<String>) {
        ActivityCompat.requestPermissions(activity, permissions, 1)
    }

    // Method to handle onRequestPermissionsResult in your activity/fragment
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
        callback: PermissionHandler
    ) {
        if (requestCode == 1) {
            val deniedPermissions = permissions.filterIndexed { index, _ ->
                grantResults[index] != PackageManager.PERMISSION_GRANTED
            }

            if (deniedPermissions.isEmpty()) {
                callback.onGranted()
            } else {
                callback.onDenied(deniedPermissions)
            }
        }
    }
}