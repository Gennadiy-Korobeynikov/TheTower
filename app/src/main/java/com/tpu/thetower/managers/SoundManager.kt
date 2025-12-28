package com.tpu.thetower.managers

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.tpu.thetower.utils.SoundEffect
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundManager @Inject constructor(
    @ApplicationContext private val appContext: Context
) {

    private var soundPool: SoundPool? = null
    private val soundMap = mutableMapOf<SoundEffect, Int>()
    private var volume: Float = 0.5f

    fun init(maxStreamsNumber: Int = 5) {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(maxStreamsNumber) // Количество одновременно проигрываемых звуков
            .setAudioAttributes(audioAttributes)
            .build()
    }

    fun loadSounds(effects: List<SoundEffect>) {
        effects.forEach { effect ->
            if (soundMap.containsKey(effect)) return@forEach

            val soundId = soundPool?.load(appContext, effect.resId, 1)
                ?: return@forEach
            soundMap[effect] = soundId
        }
    }

    fun playSound(effect: SoundEffect, repeat: Int = 0) {
        val soundId = soundMap[effect] ?: return
        soundPool?.play(soundId, volume, volume,
            1, repeat, 1f)
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
