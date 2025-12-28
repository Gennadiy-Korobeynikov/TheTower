package com.tpu.thetower.fragments.lvl3

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tpu.thetower.Hintable
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl3PuzzleButtonsBinding
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.utils.SoundEffect
import com.tpu.thetower.puzzles.Lvl3PuzzleButtons
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl3PuzzleButtonsFragment : Fragment(R.layout.fragment_lvl3_puzzle_buttons), Hintable {

    private var _binding: FragmentLvl3PuzzleButtonsBinding? = null
    private val binding get() = _binding!!

    private lateinit var puzzle: Puzzle
    private lateinit var hintManager: HintManager

    private var solution = ""

    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var dialogManager: DialogManager
    @Inject lateinit var soundManager: SoundManager
    @Inject lateinit var hintManagerFactory: HintManager.Factory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLvl3PuzzleButtonsBinding.bind(view)

        puzzle = Lvl3PuzzleButtons(3, "buttons")

        hintManager = hintManagerFactory.create(
            hints = listOf("lvl3_puzzle1_hint1", "lvl3_puzzle1_hint2"),
            level = 3,
            puzzle = "buttons"
        )

        setListeners()
    }

    private fun setListeners() {
        val buttons = listOf(binding.btn1, binding.btn2, binding.btn3, binding.btn4, binding.btn5, binding.btn6)

        buttons.forEach { button ->
            button.setOnClickListener { v ->
                val clicked = v as android.widget.Button
                soundManager.playSound(SoundEffect.BUTTON_PRESS)
                solution += clicked.text
                clicked.isClickable = false
                clicked.setBackgroundResource(R.drawable.lvl3_puzzle1_button_on)
                checkSolution(buttons)
            }
        }
    }

    private fun checkSolution(buttons: List<android.widget.Button>) {
        if (solution.length != 6) return

        if (puzzle.checkSolution(requireActivity(), saveRepo, solution)) {
            soundManager.playSound(SoundEffect.LOCK_OPENING)
            binding.mainScreen.animate()
                .alpha(0.2f)
                .setDuration(2500)
                .withEndAction { FragmentNavigation.goBack(this) }
                .start()
        } else {
            solution = ""
            buttons.forEach { b ->
                b.isClickable = true
                b.setBackgroundResource(R.drawable.lvl3_puzzle1_button_off)
            }
        }
    }

    override fun useHint() {
        if (loadManager.getPuzzleStatus(3, "donuts") == "completed") // После тряски пончиков
            hintManager.useHint(requireActivity())
        else
            dialogManager.startDialog(requireActivity(), "hint_is_not_here")
    }

    override fun skipPuzzle() {
        puzzle.complete(saveRepo)
        soundManager.playSound(SoundEffect.LOCK_OPENING)
        binding.mainScreen.animate()
            .alpha(0.2f)
            .setDuration(2500)
            .withEndAction { FragmentNavigation.goBack(this) }
            .start()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
