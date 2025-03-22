package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.LoadManager
import com.tpu.thetower.MainActivity
import com.tpu.thetower.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.databinding.FragmentTitleScreenBinding

class TitleScreenFragment : Fragment(R.layout.fragment_title_screen) {

    private lateinit var binding: FragmentTitleScreenBinding

    private lateinit var musicManager: MusicManager
    private lateinit var saveManager: SaveManager

    private lateinit var btnStart: Button
    private lateinit var btnSettings: Button
    private lateinit var btnResume: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTitleScreenBinding.bind(view)

        bindView()
        setListeners()
        handleSounds()

        FragmentManager.hideHUD(requireActivity())
        FragmentManager.hideGoBackArrow(requireActivity())
    }

    private fun bindView() {
        btnStart = binding.btnToLvl0
        btnSettings = binding.btnToSettings
        btnResume = binding.btnResume
    }

    private fun setListeners() {
        btnStart.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_titleScreenFragment_to_lvl0Fragment)
            FragmentManager.showHUD(requireActivity())
            FragmentManager.showGoBackArrow(requireActivity())
        }

        btnSettings.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_titleScreenFragment_to_settingsFragment)
        }

        btnResume.setOnClickListener {
            val loadManager = LoadManager.getInstance(requireContext() as MainActivity)
            loadManager.loadProgress()
            FragmentManager.showHUD(requireActivity())
            FragmentManager.showGoBackArrow(requireActivity())
            loadManager.startSavedLevel()
        }
    }

    private fun handleSounds() {
        musicManager = MusicManager.getInstance()
        saveManager = SaveManager.getInstance()
    }

    override fun onResume() {
        super.onResume()

        val music = R.raw.soundtrack_1
        val currentMusic = musicManager.getCurrentMusic()

        if (currentMusic != music) {
            musicManager.playMusic(requireContext(), music)
        } else {
            musicManager.resumeMusic()
        }
    }

    override fun onPause() {
        super.onPause()

        musicManager.pauseMusic()
    }
}