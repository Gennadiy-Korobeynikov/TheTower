package com.tpu.thetower.managers

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class PermissionManager (
    caller: ActivityResultCaller,
    private val activity: Activity
) {
    private val requestPermissionLauncher = caller.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted)
            UiVisibilityController.show(activity, UiVisibilityController.UiContainer.PERMISSION_DENIED)
    }

    private val requestMultiplePermissionsLauncher = caller.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        if (result.values.any { !it })
            UiVisibilityController.show(activity, UiVisibilityController.UiContainer.PERMISSION_DENIED)
    }

    fun getPermission(permission : String) {
        if (!isPermissionGranted(permission))
            requestPermissionLauncher.launch(permission)
    }

    fun getPermissions(permissions: Array<String>) {
        val toRequest = permissions.filterNot { isPermissionGranted(it) }
        if (toRequest.isEmpty()) return
        requestMultiplePermissionsLauncher.launch(toRequest.toTypedArray())
    }

    fun isPermissionGranted(permission : String) : Boolean {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
    }

}