package com.tpu.thetower.fragments.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentTitleScreenBinding
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.managers.devicemanagers.ChestManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TitleScreenFragment : Fragment(R.layout.fragment_title_screen) {

    private lateinit var binding: FragmentTitleScreenBinding

    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var musicManager: MusicManager

    @Inject lateinit var chestManager: ChestManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTitleScreenBinding.bind(view)
        setListeners()
    }

    private fun setListeners() {
        binding.btnRestart.setOnClickListener {
            // Сброс сейва
            saveRepo.resetFileData()
            loadManager.invalidateCache()
            chestManager.resetForNewGame()

            UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.TITLE)
            FragmentNavigation.changeBG(this, R.id.action_titleScreenFragment_to_lvl0Fragment)
        }

        binding.btnResume.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_global_titleScreenFragment)
            UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.TITLE)
            loadManager.startSavedLevel(requireActivity())
        }

        binding.btnExit.setOnClickListener {
            requireActivity().finish()
        }
    }

}