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

    // Текущие (активные) проигрывания по эффектам
    private val activeStreamsByEffect = mutableMapOf<SoundEffect, MutableSet<Int>>()

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
        val streamId = soundPool?.play(soundId, volume, volume, 1, repeat, 1f) ?: return
        if (streamId != 0) {
            activeStreamsByEffect.getOrPut(effect) { mutableSetOf() }.add(streamId)
        }
    }

    fun stopSound(effect: SoundEffect) {
        val pool = soundPool ?: return
        val streams = activeStreamsByEffect.remove(effect) ?: return
        streams.forEach { pool.stop(it) }
    }

    fun stopAllSounds() {
        val pool = soundPool ?: return
        activeStreamsByEffect.values.flatten().forEach { pool.stop(it) }
        activeStreamsByEffect.clear()
    }

    fun setVolume(volume: Float) {
        this.volume = volume.coerceIn(0f, 1f)
    }

    fun release() {
        stopAllSounds()
        soundPool?.release()
        soundPool = null
        soundMap.clear()
        activeStreamsByEffect.clear()
    }
}
