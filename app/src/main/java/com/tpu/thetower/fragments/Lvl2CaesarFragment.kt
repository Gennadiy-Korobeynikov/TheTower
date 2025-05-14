package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tpu.thetower.DialogManager
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.LoadManager
import com.tpu.thetower.R


class Lvl2CaesarFragment : Fragment(R.layout.fragment_lvl2_caesar), Hintable {

    private lateinit var hintManager: HintManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hintManager = HintManager(listOf("lvl2_puzzle1_hint1", "lvl2_puzzle1_hint2", "lvl2_puzzle1_hint3","lvl2_puzzle1_hint4",),
            LoadManager.getPuzzleUsedHintsCount(requireActivity(),2,"caesar"),
            2,"caesar")
        FragmentManager.showGoBackArrow(requireActivity())
    }

    override fun useHint() {
        if (LoadManager.getPuzzleStatus(requireActivity(), 2, "lock") == "in_progress") // До открытия первого замка
            hintManager.useHint(requireActivity())
        else
            DialogManager.startDialog(requireActivity(), "hint_is_not_here")
    }

}