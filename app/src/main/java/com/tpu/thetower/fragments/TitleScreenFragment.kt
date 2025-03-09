package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentTitleScreenBinding


class TitleScreenFragment : Fragment(R.layout.fragment_title_screen) {

    protected lateinit var musicManager: MusicManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        musicManager = MusicManager.getInstance()

        val binding = FragmentTitleScreenBinding.bind(view)
        val btnStart : Button = binding.btnToLvl0
        val btnSettings : Button = binding.btnToSettings

        FragmentManager.hideHUD(requireActivity())
        FragmentManager.hideGoBackArrow(requireActivity())

        btnStart.setOnClickListener {
            FragmentManager.changeBG(this,R.id.action_titleScreenFragment_to_lvl0Fragment)
            FragmentManager.showHUD(requireActivity())
            FragmentManager.showGoBackArrow(requireActivity())
        }

        btnSettings.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_titleScreenFragment_to_settingsFragment)
        }
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