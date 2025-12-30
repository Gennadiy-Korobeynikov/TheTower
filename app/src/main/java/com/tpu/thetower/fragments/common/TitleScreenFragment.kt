package com.tpu.thetower.fragments.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.managers.FileSaveManager
import com.tpu.thetower.databinding.FragmentTitleScreenBinding
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.managers.SaveRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TitleScreenFragment : Fragment(R.layout.fragment_title_screen) {

    private lateinit var binding: FragmentTitleScreenBinding

    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var musicManager: MusicManager
    @Inject lateinit var fileSaveManager: FileSaveManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTitleScreenBinding.bind(view)

        setListeners()
    }

    private fun setListeners() {
        binding.btnToLvl0.setOnClickListener {
            // Сброс сейва
            fileSaveManager.resetData()
            loadManager.invalidateCache()

            FragmentNavigation.changeBG(this, R.id.action_global_titleScreenFragment)
            UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.TITLE)

            FragmentNavigation.changeBG(this, R.id.action_titleScreenFragment_to_lvl0Fragment)
        }

        binding.btnToSettings.setOnClickListener {
            UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.SETTINGS)
        }

        binding.btnResume.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_global_titleScreenFragment)
            UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.TITLE)
            loadManager.startSavedLevel(requireActivity())
        }
    }

//    override fun onResume() {
//        super.onResume()
//        musicManager.playMusic(R.raw.soundtrack_1)
//    }

}