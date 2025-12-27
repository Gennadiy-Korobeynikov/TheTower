package com.tpu.thetower.fragments.lvl2

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl2CaesarFragment : Fragment(R.layout.fragment_lvl2_caesar), Hintable {

    private lateinit var hintManager: HintManager

    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var dialogManager: DialogManager
    @Inject lateinit var hintManagerFactory: HintManager.Factory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hintManager = hintManagerFactory.create(
            hints = listOf(
                "lvl2_puzzle1_hint1",
                "lvl2_puzzle1_hint2",
                "lvl2_puzzle1_hint3",
                "lvl2_puzzle1_hint4",
                "lvl2_puzzle1_hint5"
            ),
            level = 2,
            puzzle = "caesar"
        )

        UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
    }

    override fun useHint() {
        if (loadManager.getPuzzleStatus(2, "lock") == "in_progress") {
            hintManager.useHint(requireActivity())
        } else {
            dialogManager.startDialog(requireActivity(), "hint_is_not_here")
        }
    }

}