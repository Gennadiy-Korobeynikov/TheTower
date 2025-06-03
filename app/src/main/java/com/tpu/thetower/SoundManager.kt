package com.tpu.thetower

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors

class SoundManager private constructor() {

    private var soundPool: SoundPool? = null
    private val soundMap = mutableMapOf<Int, Int>()
    private var volume: Float = 0.5f
//    private val executor = Executors.newSingleThreadExecutor()
//    private val mainHandler = Handler(Looper.getMainLooper())

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

    fun loadSound(context: Context, soundResIds: List<Int>){
        soundResIds.forEach{
            val soundId = soundPool?.load(context, it, 10) ?: -1
            if (soundId != -1) {
                soundMap[it] = soundId
            }
        }
    }

    fun playSound(soundResId: Int, repeat: Int = 0) {
        val soundId = soundMap[soundResId] ?: return
        soundPool?.play(soundId, volume, volume, 1, repeat, 1.0f)
    }

//    fun playSoundWithDelay(soundResId: Int, delayMillis: Long) {
//        executor.execute {
//            try {
//                Thread.sleep(delayMillis)
//                // Вызов проигрывания звука должен происходить в основном потоке
//                mainHandler.post {
//                    playSound(soundResId)
//                }
//            } catch (e: InterruptedException) {
//                e.printStackTrace()
//            }
//        }
//    }

    fun setVolume(volume: Float) {
        this.volume = volume.coerceIn(0f, 1f)
    }

    fun release() {
        soundPool?.release()
        soundPool = null
        soundMap.clear()
    }
}
