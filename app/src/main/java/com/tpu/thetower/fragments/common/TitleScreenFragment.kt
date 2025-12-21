package com.tpu.thetower.fragments.common

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.managers.FileSaveManager
import com.tpu.thetower.databinding.FragmentTitleScreenBinding
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.managers.SaveRepository

class TitleScreenFragment : Fragment(R.layout.fragment_title_screen) {

    private lateinit var binding: FragmentTitleScreenBinding

    private lateinit var musicManager: MusicManager
    private val saveRepo: SaveRepository = SaveRepository.getInstance()

    private lateinit var btnStart: Button
    private lateinit var btnSettings: Button
    private lateinit var btnResume: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTitleScreenBinding.bind(view)

        bindView()
        setListeners()
        handleSounds()

        UiVisibilityController.hide(
            requireActivity(),
            UiVisibilityController.UiContainer.HUD,
            UiVisibilityController.UiContainer.GO_BACK_ARROW
        )
    }

    private fun bindView() {
        btnStart = binding.btnToLvl0
        btnSettings = binding.btnToSettings
        btnResume = binding.btnResume
    }

    private fun setListeners() {
        btnStart.setOnClickListener {
            // Сброс — это обязанность FileSaveManager (файловая система)
            FileSaveManager.getInstance().resetData(requireContext())
            LoadManager.invalidateCache()
            LoadManager.loadProgress(requireActivity())

            FragmentNavigation.changeBG(this, R.id.action_global_titleScreenFragment)
            UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.TITLE)
            FragmentNavigation.changeBG(this, R.id.action_titleScreenFragment_to_lvl0Fragment)
        }

        btnSettings.setOnClickListener {
            UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.SETTINGS)
        }

        btnResume.setOnClickListener {
            LoadManager.loadProgress(requireActivity())
            FragmentNavigation.changeBG(this, R.id.action_global_titleScreenFragment)
            UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.TITLE)
            LoadManager.startSavedLevel(requireActivity())
        }
    }

    private fun handleSounds() {
        musicManager = MusicManager.getInstance()
    }

    override fun onResume() {
        super.onResume()

        musicManager.playMusic(requireContext(), R.raw.soundtrack_1)
    }

}