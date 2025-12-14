package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl5PuzzleMooseBinding
import com.tpu.thetower.puzzles.Lvl5PuzzleMoose

class Lvl5PuzzleMooseFragment : Fragment(R.layout.fragment_lvl5_puzzle_moose) {

    private lateinit var binding: FragmentLvl5PuzzleMooseBinding

    private lateinit var ivMoose: ImageView

    private lateinit var btnLeft: Button
    private lateinit var btnRight: Button

    private var solution = ""
    private val puzzle: Puzzle = Lvl5PuzzleMoose(5, "moose")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl5PuzzleMooseBinding.bind(view)

        bindView()
        setListeners()
    }

    private fun bindView() {
        ivMoose = binding.ivMoose
        btnLeft = binding.btnLeft
        btnRight = binding.btnRight
    }

    private fun setListeners() {

        btnLeft.setOnClickListener {
            ivMoose.setImageResource(R.drawable.lvl5_puzzle1_left)
            ivMoose.animate().alpha(1f).setDuration(1000).withEndAction {
                ivMoose.setImageResource(R.drawable.lvl5_puzzle1_base)
            }
            solution += "0"
            check()
        }

        btnRight.setOnClickListener {
            ivMoose.setImageResource(R.drawable.lvl5_puzzle1_right)
            ivMoose.animate().alpha(1f).setDuration(1000).withEndAction {
                ivMoose.setImageResource(R.drawable.lvl5_puzzle1_base)
            }
            solution += "1"
            check()
        }
    }

    private fun check() {
        if (solution.length >= 5) {
            if (puzzle.checkSolution(requireActivity(), solution.takeLast(5))) {
                FragmentManager.changeBG(this, R.id.action_global_elevatorFragment)
                FragmentManager.changeBG(this, R.id.action_elevatorFragment_to_lvl5Fragment)
            }
        }
    }

}