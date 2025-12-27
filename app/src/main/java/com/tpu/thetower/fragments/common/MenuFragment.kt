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
    lateinit var soundManager: SoundManager // оставлено, если используется в будущем

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMenuBinding.bind(view)

        setListeners()
        // handleSounds() удалён: менеджеры приходят через DI
    }

    private fun setListeners() {
        binding.btnResume.setOnClickListener {
            UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.MENU)
        }

        binding.btnToTitleScreen.setOnClickListener {
            UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.MENU)
            FragmentNavigation.changeBG(this, R.id.action_global_titleScreenFragment)
            UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.TITLE)
        }

        requireActivity().supportFragmentManager
            .setFragmentResultListener("updateProgressBar", viewLifecycleOwner) { _, _ ->
                val (solvedPuzzles, allPuzzles) = loadManager.getLevelProgress(loadManager.getCurrentLevel())
                binding.progressBar.progress =
                    if (allPuzzles != 0) solvedPuzzles * 100 / allPuzzles else 0
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