package com.tpu.thetower.fragments.common

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding

    private lateinit var musicManager: MusicManager
    private lateinit var soundManager: SoundManager
    private val saveRepo: SaveRepository = SaveRepository.getInstance()

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
        val gameData = saveRepo.get(requireActivity())
        val savedMusicVolume = gameData.gameSettings.musicVolume
        val savedSoundVolume = gameData.gameSettings.soundVolume

        sbMusic.progress = (savedMusicVolume * 100).toInt()
        sbSound.progress = (savedSoundVolume * 100).toInt()

        sbMusic.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val volume = progress / 100f
                saveRepo.saveMusicVolume(requireActivity(), volume)
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
                saveRepo.saveSoundVolume(requireActivity(), volume)
                soundManager.setVolume(volume)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        btnBack.setOnClickListener {
            UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.SETTINGS)
        }
    }

    private fun handleSounds() {
        musicManager = MusicManager.getInstance()
        soundManager = SoundManager.getInstance()
    }
}