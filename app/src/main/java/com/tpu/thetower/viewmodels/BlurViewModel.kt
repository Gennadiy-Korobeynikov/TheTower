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


    override fun onCleared() {
        // ViewModel живёт в scope nav graph, поэтому сюда попадём при уничтожении графа.
        // Освобождаем ссылки на Bitmap, чтобы избежать утечек памяти.
        _blurCache.clear()
        super.onCleared()
    }
}
