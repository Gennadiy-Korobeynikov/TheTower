package com.tpu.thetower

import android.content.Context
import android.media.MediaPlayer

class MusicManager private constructor()  {

    private var mediaPlayer: MediaPlayer? = null
    private var currentMusic: Int = -1
    private var volume: Float = 0.5f

    companion object {
        private var instance: MusicManager? = null

        @Synchronized
        fun getInstance(): MusicManager {
            if (instance == null) {
                instance = MusicManager()
            }
            return instance!!
        }
    }

    fun playMusic(context: Context, music: Int, restart: Boolean = false) {
        if (currentMusic == music && mediaPlayer != null && mediaPlayer!!.isPlaying && !restart) {
            return
        }

        stopMusic()

        mediaPlayer = MediaPlayer.create(context.applicationContext, music).apply {
            isLooping = true
            setVolume(volume, volume)
            setOnPreparedListener { start() }
        }
        currentMusic = music
    }

    fun stopMusic() {
        if (mediaPlayer != null) {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.stop()
            }
            mediaPlayer!!.release()
            mediaPlayer = null
            currentMusic = -1
        }
    }

    fun pauseMusic() {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer!!.pause()
        }
    }

    fun resumeMusic() {
        if (mediaPlayer != null && !mediaPlayer!!.isPlaying) {
            mediaPlayer!!.start()
        }
    }

    fun setVolume(volume: Float) {
        this.volume = volume.coerceIn(0f, 1f)
        mediaPlayer?.setVolume(this.volume, this.volume)
    }

    fun getCurrentMusic(): Int {
        return currentMusic
    }

}