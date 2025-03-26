package com.tpu.thetower.devicemanagers

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.util.Log

class FlashlightManager (context: Context, private val onTorchStateChanged: (Boolean) -> Unit) {
    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
    private var cameraId: String? = null
    private var isRegistered = false

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

    fun startMonitoring() {
        if (isRegistered) return  // Не даём зарегистрироваться повторно
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && cameraManager != null) {
            try {
                val cameraList = cameraManager.cameraIdList
                if (cameraList.isNotEmpty()) {
                    cameraId = cameraList.first()
                    cameraManager.registerTorchCallback(torchCallback, null)
                    isRegistered = true  // Устанавливаем флаг, что регистрация выполнена
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

    fun stopMonitoring() {
        if (isRegistered && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && cameraManager != null) {
            cameraManager.unregisterTorchCallback(torchCallback)
            isRegistered = false  // Сбрасываем флаг регистрации
        }
    }


    fun toggleFlashlight(isOn: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && cameraManager != null && cameraId != null) {
            try {
                cameraManager.setTorchMode(cameraId!!, isOn)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        } else {
            Log.e("FlashlightMonitor", "Невозможно переключить фонарик")
        }
    }
}
