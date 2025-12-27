package com.tpu.thetower.fragments.lvl2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.managers.UiVisibilityController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl2PetFragment : Fragment(R.layout.fragment_lvl2_pet), Hintable {

    @Inject lateinit var soundManager: SoundManager
    @Inject lateinit var dialogManager: DialogManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)

        soundManager.init()
        soundManager.loadSound(
            listOf(
                R.raw.sound_of_drawer_closing
            )
        )
    }

    override fun useHint() {
        dialogManager.startDialog(requireActivity(), "hint_is_not_here")
    }

    override fun onPause() {
        super.onPause()
        soundManager.playSound(R.raw.sound_of_drawer_closing)
    }

}