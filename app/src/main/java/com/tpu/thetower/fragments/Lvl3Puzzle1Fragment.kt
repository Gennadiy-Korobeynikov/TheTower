package com.tpu.thetower.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl3Puzzle1Binding
import com.tpu.thetower.puzzles.Lvl3Puzzle1

class Lvl3Puzzle1Fragment : Fragment(R.layout.fragment_lvl3_puzzle1) {

    private lateinit var binding: FragmentLvl3Puzzle1Binding

    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button
    private lateinit var btn4: Button
    private lateinit var btn5: Button
    private lateinit var btn6: Button

    private lateinit var mainScreen: FrameLayout

    private lateinit var puzzle: Puzzle

    private val buttons = mutableListOf<Button>()


    private var solution = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl3Puzzle1Binding.bind(view)
        puzzle = Lvl3Puzzle1("Lvl3Puzzle1")

        bindView()
        buttons.addAll(listOf(btn1, btn2, btn3, btn4, btn5, btn6))
        setListeners()
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
                clickedButton.setTextColor(Color.GRAY)
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
            }
            solution = ""
            buttons.forEach { button ->
                button.isClickable = true
                button.setTextColor(Color.BLACK)
            }
        }
    }
}
