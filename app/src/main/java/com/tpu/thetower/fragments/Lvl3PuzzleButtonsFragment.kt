package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.tpu.thetower.DialogManager
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.LoadManager
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl3PuzzleButtonsBinding
import com.tpu.thetower.puzzles.Lvl3PuzzleButtons

class Lvl3PuzzleButtonsFragment : Fragment(R.layout.fragment_lvl3_puzzle_buttons), Hintable {

    private lateinit var binding: FragmentLvl3PuzzleButtonsBinding

    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button
    private lateinit var btn4: Button
    private lateinit var btn5: Button
    private lateinit var btn6: Button

    private lateinit var mainScreen: FrameLayout

    private lateinit var puzzle: Puzzle
    private lateinit var hintManager: HintManager

    private val buttons = mutableListOf<Button>()

    private var solution = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl3PuzzleButtonsBinding.bind(view)
        puzzle = Lvl3PuzzleButtons(3, "buttons")

        bindView()
        buttons.addAll(listOf(btn1, btn2, btn3, btn4, btn5, btn6))
        setListeners()
        hintManager = HintManager(listOf("lvl3_puzzle1_hint1", "lvl3_puzzle1_hint2",),
            LoadManager.getPuzzleUsedHintsCount(requireActivity(),3,"buttons"),
            3,"buttons")
    }

    private fun bindView() {
        btn1 = binding.btn1
        btn2 = binding.btn2
        btn3 = binding.btn3
        btn4 = binding.btn4
        btn5 = binding.btn5
        btn6 = binding.btn6

        mainScreen = binding.mainScreen
    }

    private fun setListeners() {

        buttons.forEach { button ->
            button.setOnClickListener { view ->
                val clickedButton = view as Button
                solution += clickedButton.text.toString()
                clickedButton.isClickable = false
                clickedButton.setBackgroundResource(R.drawable.lvl3_puzzle1_button_on)
                checkSolution()
            }
        }

    }

    private fun checkSolution() {
        if (solution.length == 6) {
            if (puzzle.checkSolution(requireContext(), solution)) {
                mainScreen.animate()
                    .alpha(0.2f)
                    .setDuration(2500)
                    .withEndAction {
                        FragmentManager.goBack(this)
                    }
                    .start()
            } else {
                solution = ""
                buttons.forEach { button ->
                    button.isClickable = true
                    button.setBackgroundResource(R.drawable.lvl3_puzzle1_button_off)
                }
            }
        }
    }

    override fun useHint() {
        if (LoadManager.getPuzzleStatus(requireActivity(), 3, "donuts") == "completed") // После тряски пончиков
            hintManager.useHint(requireActivity())
        else
            DialogManager.startDialog(requireActivity(), "hint_is_not_here")
    }
}
