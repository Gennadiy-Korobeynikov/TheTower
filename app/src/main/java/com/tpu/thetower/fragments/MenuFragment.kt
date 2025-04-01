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
import com.tpu.thetower.databinding.FragmentMenuBinding
import com.tpu.thetower.databinding.FragmentSettingsBinding


class MenuFragment : Fragment(R.layout.fragment_menu) {

    private lateinit var binding: FragmentMenuBinding

    private lateinit var musicManager: MusicManager
    private lateinit var soundManager: SoundManager
    private lateinit var saveManager: SaveManager

    private lateinit var btnToTitleScreen: Button
    private lateinit var btnResume: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMenuBinding.bind(view)

        bindView()
        setListeners()
        handleSounds()
    }

    private fun bindView() {
        btnResume = binding.btnResume
        btnToTitleScreen = binding.btnToTitlescreen
    }

    private fun setListeners() {

        btnResume.setOnClickListener {
            FragmentManager.hideMenu(requireActivity())
        }

        btnToTitleScreen.setOnClickListener {
            FragmentManager.hideMenu(requireActivity())
            FragmentManager.changeBG(this, R.id.action_global_titleScreenFragment)
            FragmentManager.showTitleScreen(requireActivity())
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