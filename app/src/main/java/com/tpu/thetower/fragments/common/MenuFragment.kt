package com.tpu.thetower.fragments.common

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment

import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.managers.SaveManager
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.databinding.FragmentMenuBinding


class MenuFragment : Fragment(R.layout.fragment_menu) {

    private lateinit var binding: FragmentMenuBinding

    private lateinit var musicManager: MusicManager
    private lateinit var soundManager: SoundManager
    private lateinit var saveManager: SaveManager

    private lateinit var btnToTitleScreen: Button
    private lateinit var btnResume: Button

    private lateinit var progressBar: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMenuBinding.bind(view)

        bindView()
        setListeners()
        handleSounds()

    }

    private fun bindView() {
        btnResume = binding.btnResume
        btnToTitleScreen = binding.btnToTitleScreen
        progressBar = binding.progressBar
    }

    private fun setListeners() {

        btnResume.setOnClickListener {
            UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.MENU)
        }

        btnToTitleScreen.setOnClickListener {
            UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.MENU)
            FragmentNavigation.changeBG(this, R.id.action_global_titleScreenFragment)
            UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.TITLE)
        }

        requireActivity().supportFragmentManager
            .setFragmentResultListener("updateProgressBar", viewLifecycleOwner) { _, bundle ->
                val (solvedPuzzles, allPuzzles) = LoadManager.getLevelProgress(
                    requireActivity(),
                    LoadManager.getCurrentLevel(requireActivity())
                )
                if (allPuzzles != 0)
                    progressBar.progress = solvedPuzzles * 100 / allPuzzles
                else
                    progressBar.progress = 0
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