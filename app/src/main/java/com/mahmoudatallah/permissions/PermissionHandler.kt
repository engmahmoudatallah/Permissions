package com.mahmoudatallah.permissions

interface PermissionHandler {
    fun onGranted()
    fun onDenied(deniedPermissions: List<String>)
}