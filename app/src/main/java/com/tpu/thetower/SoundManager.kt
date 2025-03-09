package com.tpu.thetower

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

class SoundManager private constructor() {

    private var soundPool: SoundPool? = null
    private val soundMap = mutableMapOf<Int, Int>()
    private var volume: Float = 0.5f

    companion object {
        private var instance: SoundManager? = null

        @Synchronized
        fun getInstance(): SoundManager {
            if (instance == null) {
                instance = SoundManager()
            }
            return instance!!
        }
    }

    fun init() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5) // Количество одновременно проигрываемых звуков
            .setAudioAttributes(audioAttributes)
            .build()
    }

    fun loadSound(context: Context, soundResId: Int): Int {
        val soundId = soundPool?.load(context, soundResId, 10) ?: -1
        if (soundId != -1) {
            soundMap[soundResId] = soundId
        }
        return soundId
    }

    fun playSound(soundResId: Int) {
        val soundId = soundMap[soundResId] ?: return
        soundPool?.play(soundId, volume, volume, 1, 0, 1.0f)
    }

    fun setVolume(volume: Float) {
        this.volume = volume.coerceIn(0f, 1f)
    }

    fun release() {
        soundPool?.release()
        soundPool = null
        soundMap.clear()
    }
}
