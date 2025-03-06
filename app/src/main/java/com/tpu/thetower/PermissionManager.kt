package com.tpu.thetower

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf

class PermissionManager (
    caller: ActivityResultCaller,
    private val activity: Activity
) {

    private val requestPermissionLauncher = caller.registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted)
        // Пользователь отказал в разрешении
        FragmentManager.showPermissionDeniedFragment(activity)
    }



    fun getPermission(permission : String) {
        if (!isPermissionGranted(permission))
            requestPermissionLauncher.launch(permission)
    }

    fun isPermissionGranted(permission : String) : Boolean {
        return ContextCompat.checkSelfPermission( activity, permission) == PackageManager.PERMISSION_GRANTED
    }

}