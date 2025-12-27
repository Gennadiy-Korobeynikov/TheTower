package com.tpu.thetower.managers

import android.content.Context
import androidx.core.content.edit

class AppPreferences(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var isDevMode: Boolean
        get() = prefs.getBoolean(KEY_IS_DEV_MODE, false)
        set(value) {
            prefs.edit { putBoolean(KEY_IS_DEV_MODE, value) }
        }

    companion object {
        private const val PREFS_NAME = "app_prefs"
        private const val KEY_IS_DEV_MODE = "is_dev_mode"
    }
}

