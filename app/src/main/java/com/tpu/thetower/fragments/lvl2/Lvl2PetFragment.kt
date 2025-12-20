package com.tpu.thetower.fragments.lvl2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.tpu.thetower.managers.DialogManager

import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.managers.UiVisibilityController

class Lvl2PetFragment : Fragment(R.layout.fragment_lvl2_pet), Hintable {

    private lateinit var soundManager: SoundManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)

        soundManager = SoundManager.getInstance()
        soundManager.init()
        soundManager.loadSound(requireContext(), listOf(
            R.raw.sound_of_drawer_closing
        ))
    }

    override fun useHint() {
        DialogManager.startDialog(requireActivity(), "hint_is_not_here")
    }

    override fun onPause() {
        super.onPause()

        soundManager.playSound(R.raw.sound_of_drawer_closing)
    }

}