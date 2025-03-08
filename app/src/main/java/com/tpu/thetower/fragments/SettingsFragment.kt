package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.SeekBar
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var musicManager: MusicManager
    private lateinit var saveManager: SaveManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        musicManager = MusicManager.getInstance()
        saveManager = SaveManager.getInstance()

        val binding = FragmentSettingsBinding.bind(view)
        val btn_back: Button = binding.btnBack
        val sb_music: SeekBar = binding.musicVolumeSeekBar
        val sb_sound: SeekBar = binding.soundVolumeSeekBar

        val gameData = saveManager.readData(requireContext())
        val savedMusicVolume = gameData?.gameSettings?.musicVolume ?: 0.5f
        val savedSoundVolume = gameData?.gameSettings?.soundVolume ?: 0.5f

        sb_music.progress = (savedMusicVolume * 100).toInt()
        sb_sound.progress = (savedSoundVolume * 100).toInt()

        sb_music.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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

        val languageSpinner = view.findViewById<Spinner>(R.id.language_spinner)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.language_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            languageSpinner.adapter = adapter
        }

        btn_back.setOnClickListener {
            FragmentManager.goBack(this)
        }
    }

    override fun onResume() {
        super.onResume()

        musicManager.resumeMusic()
    }
}