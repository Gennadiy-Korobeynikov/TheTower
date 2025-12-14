package com.tpu.thetower.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import com.tpu.thetower.DialogManager
import com.tpu.thetower.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.LoadManager
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.databinding.FragmentChessboardTestBinding
import com.tpu.thetower.databinding.FragmentLvl4Puzzle1Binding
import com.tpu.thetower.puzzles.ChessboardPuzzle


class Lvl4Puzzle1Fragment : Fragment(R.layout.fragment_lvl4_puzzle1), Hintable {
    private lateinit var binding: FragmentLvl4Puzzle1Binding

    private lateinit var saveManager: SaveManager
    private lateinit var hintManager: HintManager


    private lateinit var toggleBtnAskii : ToggleButton


    private fun bind() {
        toggleBtnAskii = binding.toggleBtnAskii
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLvl4Puzzle1Binding.bind(view)
        saveManager = SaveManager.getInstance()
        bind()
        toggleBtnAskii.isChecked = LoadManager.isASKII // TODO Исправить!!!!! длолжно быть через сохранения


        hintManager = HintManager(
            listOf(
                "lvl4_askiiBtn_hint1", "lvl4_askiiBtn_hint2"
            ),
            LoadManager.getPuzzleUsedHintsCount(requireActivity(), 4, "askiibtn"),
            4, "askiibtn"
        )
        toggleBtnAskii.setOnCheckedChangeListener { buttonView, isChecked ->
            var dialog : String
            if (isChecked) { // TODO Исправить!!!!! длолжно быть через сохранения
                //saveManager.savePuzzleData(requireContext(), 4, 2)
                LoadManager.isASKII = true
                dialog = "lvl4_puzzle1_askii"
            } else {
                LoadManager.isASKII = false
                dialog = "lvl4_puzzle1_normal"
            }
            DialogManager.startDialog(requireActivity(),dialog)
        }

    }

    override fun useHint() {
        hintManager.useHint(requireActivity())
    }

}
