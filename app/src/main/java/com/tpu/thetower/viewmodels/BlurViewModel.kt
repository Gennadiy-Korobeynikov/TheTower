package com.tpu.thetower.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel

class BlurViewModel : ViewModel() {
    private val _blurCache = mutableMapOf<String, Bitmap>()
    val blurCache: Map<String, Bitmap> get() = _blurCache

    fun getBlur(key: String): Bitmap? = _blurCache[key]

    fun setBlur(key: String, bitmap: Bitmap) {
        _blurCache[key] = bitmap
    }

    fun clearBlur(key: String) {
        _blurCache.remove(key)
    }

    fun clearAll() {
        _blurCache.clear()
    }
}

