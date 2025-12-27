package com.tpu.thetower.fragments.lvl5

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl5PuzzleMooseBinding
import com.tpu.thetower.puzzles.Lvl5PuzzleMoose
import com.tpu.thetower.managers.SaveRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl5PuzzleMooseFragment : Fragment(R.layout.fragment_lvl5_puzzle_moose) {

    private lateinit var binding: FragmentLvl5PuzzleMooseBinding

    private var solution = ""
    private val puzzle: Puzzle = Lvl5PuzzleMoose(5, "moose")

    @Inject lateinit var saveRepo: SaveRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl5PuzzleMooseBinding.bind(view)

        setListeners()
    }

    private fun setListeners() {

        binding.btnLeft.setOnClickListener {
            binding.ivMoose.setImageResource(R.drawable.lvl5_puzzle1_left)
            binding.ivMoose.animate().alpha(1f).setDuration(1000).withEndAction {
                binding.ivMoose.setImageResource(R.drawable.lvl5_puzzle1_base)
            }
            solution += "0"
            check()
        }

        binding.btnRight.setOnClickListener {
            binding.ivMoose.setImageResource(R.drawable.lvl5_puzzle1_right)
            binding.ivMoose.animate().alpha(1f).setDuration(1000).withEndAction {
                binding.ivMoose.setImageResource(R.drawable.lvl5_puzzle1_base)
            }
            solution += "1"
            check()
        }
    }

    private fun check() {
        if (solution.length >= 5) {
            if (puzzle.checkSolution(requireActivity(), saveRepo, solution.takeLast(5))) {
                FragmentNavigation.changeBG(this, R.id.action_global_elevatorFragment)
                FragmentNavigation.changeBG(this, R.id.action_elevatorFragment_to_lvl5Fragment)
            }
        }
    }

}