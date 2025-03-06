package com.tpu.thetower.devicemanagers

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.util.Log

class FlashlightManager (context: Context, private val onTorchStateChanged: (Boolean) -> Unit) {
    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
    private var cameraId: String? = null


    private val torchCallback = object : CameraManager.TorchCallback() {
        // Фонарик включился / выключился
        override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
            super.onTorchModeChanged(cameraId, enabled)
            onTorchStateChanged(enabled)
        }

        // Фонарик не доступен
        override fun onTorchModeUnavailable(cameraId: String) {
            super.onTorchModeUnavailable(cameraId)
            onTorchStateChanged(false)
        }
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && cameraManager != null) {
            try {
                val cameraList = cameraManager.cameraIdList // Камер-то мб и несколько у богачей
                if (cameraList.isNotEmpty()) {
                    cameraId = cameraList.first() // Выбираем первую (основную)
                    cameraManager.registerTorchCallback(torchCallback, null)
                } else {
                    Log.e("FlashlightMonitor", "Нет доступных камер")
                }
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        } else {
            Log.e("FlashlightMonitor", "CameraManager = null или версия Android ниже 6.0")
        }
    }

    // Перестаём отслеживать фонарик
    fun unregister() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && cameraManager != null) {
            cameraManager.unregisterTorchCallback(torchCallback)
        }
    }
}

