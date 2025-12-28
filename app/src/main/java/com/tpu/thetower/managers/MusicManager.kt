package com.tpu.thetower.managers

import android.content.Context
import android.media.MediaPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicManager @Inject constructor(
    @ApplicationContext private val appContext: Context
) {

    private var mediaPlayer: MediaPlayer? = null
    private var currentMusic: Int? = null
    private var volume: Float = 0.5f

    fun playMusic(music: Int, restart: Boolean = false) {
        if (currentMusic == music && mediaPlayer != null && mediaPlayer!!.isPlaying && !restart) {
            return
        }

        stopMusic()

        mediaPlayer = MediaPlayer.create(appContext, music).apply {
            isLooping = true
            setVolume(volume, volume)
            setOnErrorListener { _, _, _ ->
                stop()
                true
            }
            start()
        }
        currentMusic = music
    }

    fun stopMusic() {
        mediaPlayer?.release()
        mediaPlayer = null
        currentMusic = null
    }

    fun pauseMusic() {
        mediaPlayer?.takeIf { it.isPlaying }?.pause()
    }

    fun resumeMusic() {
        mediaPlayer?.takeIf { !it.isPlaying }?.start()
    }

    fun setVolume(volume: Float) {
        this.volume = volume.coerceIn(0f, 1f)
        mediaPlayer?.setVolume(this.volume, this.volume)
    }

    fun getCurrentMusic(): Int? = currentMusic
}