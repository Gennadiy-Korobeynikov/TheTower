package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.SoundManager
import com.tpu.thetower.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding

    private lateinit var musicManager: MusicManager
    private lateinit var soundManager: SoundManager
    private lateinit var saveManager: SaveManager

    private lateinit var btnBack: Button
    private lateinit var sbMusic: SeekBar
    private lateinit var sbSound: SeekBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSettingsBinding.bind(view)

        bindView()
        setListeners()
        handleSounds()
    }

    private fun bindView() {

        btnBack = binding.btnBack
        sbMusic = binding.musicVolumeSeekBar
        sbSound = binding.soundVolumeSeekBar
    }

    private fun setListeners() {
        saveManager = SaveManager.getInstance()
        val gameData = saveManager.readData(requireContext())
        val savedMusicVolume = gameData?.gameSettings?.musicVolume ?: 0.5f
        val savedSoundVolume = gameData?.gameSettings?.soundVolume ?: 0.5f

        sbMusic.progress = (savedMusicVolume * 100).toInt()
        sbSound.progress = (savedSoundVolume * 100).toInt()

        sbMusic.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val volume = progress / 100f

                val updatedGameData = gameData?.copy(
                    gameSettings = gameData.gameSettings.copy(musicVolume = volume)
                )

                if (updatedGameData != null) {
                    saveManager.saveData(requireContext(), updatedGameData)
                }

                musicManager.setVolume(volume)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        sbSound.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val volume = progress / 100f

                val updatedGameData = gameData?.copy(
                    gameSettings = gameData.gameSettings.copy(soundVolume = volume)
                )

                if (updatedGameData != null) {
                    saveManager.saveData(requireContext(), updatedGameData)
                }

                soundManager.setVolume(volume)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        btnBack.setOnClickListener {
            FragmentManager.goBack(this)
        }
    }

    private fun handleSounds() {
        musicManager = MusicManager.getInstance()
        soundManager = SoundManager.getInstance()
        saveManager = SaveManager.getInstance()
    }

    override fun onResume() {
        super.onResume()

        musicManager.resumeMusic()
    }

    override fun onPause() {
        super.onPause()

        musicManager.pauseMusic()
    }
}