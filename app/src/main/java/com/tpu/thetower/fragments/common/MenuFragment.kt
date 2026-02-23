package com.tpu.thetower.fragments.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentMenuBinding
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.managers.UiVisibilityController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MenuFragment : Fragment(R.layout.fragment_menu) {

    private lateinit var binding: FragmentMenuBinding

    @Inject
    lateinit var loadManager: LoadManager
    @Inject
    lateinit var musicManager: MusicManager
    @Inject
    lateinit var soundManager: SoundManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMenuBinding.bind(view)

        setListeners()
    }

    private fun setListeners() {
        binding.btnResume.setOnClickListener {
            UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.MENU)
        }

        binding.btnToTitleScreen.setOnClickListener {
            UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.MENU)
            FragmentNavigation.changeBG(this, R.id.action_global_titleScreenFragment)
            musicManager.playMusic(R.raw.soundtrack_1)
            UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.TITLE)
        }
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