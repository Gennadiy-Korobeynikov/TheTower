package com.tpu.thetower.fragments.lvl4

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.UiVisibilityController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl4ChessFragment : Fragment(R.layout.fragment_lvl4_chess), Hintable {

    @Inject lateinit var dialogManager : DialogManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
    }

    override fun useHint() {
        dialogManager.startDialog(requireActivity(), "hint_is_not_here")
    }

    override fun skipPuzzle() {
        Snackbar.make(requireView(), "Подсказка к механизму на стене - C,A,G,E", Snackbar.LENGTH_SHORT).show()
    }
}