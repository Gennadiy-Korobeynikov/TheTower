package com.tpu.thetower.fragments.lvl2

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tpu.thetower.Hintable
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl2PuzzleRightLockBinding
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.puzzles.Lvl2PuzzleChat
import com.tpu.thetower.utils.CommonAnimationHelper
import com.tpu.thetower.utils.SoundEffect
import com.tpu.thetower.utils.wheellocks.WheelSetupHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl2PuzzleRightLockFragment : Fragment(R.layout.fragment_lvl2_puzzle_right_lock), Hintable {

    private lateinit var binding: FragmentLvl2PuzzleRightLockBinding

    private val puzzle: Puzzle = Lvl2PuzzleChat(2, "chat")
    private lateinit var hintManager: HintManager

    @Inject lateinit var soundManager: SoundManager
    @Inject lateinit var hintManagerFactory: HintManager.Factory
    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var dialogManager: DialogManager


    private val currSolution = "00000".toCharArray()
    private var isSolved = false

    private val images = arrayOf(
        R.drawable.lvl2_puzzle0_letter_b,
        R.drawable.lvl2_puzzle0_letter_d,
        R.drawable.lvl2_puzzle0_letter_g,
        R.drawable.lvl2_puzzle0_letter_i,
        R.drawable.lvl2_puzzle0_letter_k,
        R.drawable.lvl2_puzzle0_letter_m,
        R.drawable.lvl2_puzzle0_letter_n,
        R.drawable.lvl2_puzzle0_letter_o,
        R.drawable.lvl2_puzzle0_letter_x,
        R.drawable.lvl2_puzzle0_letter_y
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl2PuzzleRightLockBinding.bind(view)

        hintManager = hintManagerFactory.create(
            hints = listOf("lvl2_puzzle0_hint"),
            level = 2,
            puzzle = "lock"
        )

        setupWheels(images)
    }

    private fun setupWheels(data: Array<Int>) {
        val rvList = listOf(
            binding.rvImage1,
            binding.rvImage2,
            binding.rvImage3,
            binding.rvImage4,
            binding.rvImage5
        )
        rvList.forEachIndexed { index, rv -> setupWheel(rv, data, index) }
    }

    private fun setupWheel(rv: RecyclerView, data: Array<Int>, rvIndex: Int) {
        WheelSetupHelper.setupWheel(
            rv = rv,
            data = data,
            layoutImage = R.layout.letter_image,
            orientation = LinearLayoutManager.VERTICAL,
            rvIndex = rvIndex,
            currentSolution = currSolution,
            activity = requireActivity(),
            puzzle = puzzle,
            soundManager = soundManager,
            soundEffect = SoundEffect.SEGMENTS_ROTATING,
            isSolvedRef = { isSolved },
            onSolvedListener = object : WheelSetupHelper.WheelSolvedListener {
                override fun onPuzzleSolved() {
                    passed()
                }
            }
        )
    }

    private fun passed() {
        isSolved = true
        soundManager.playSound(SoundEffect.LOCK_OPENING)

        CommonAnimationHelper.animatePuzzleCompletion(
            fragment = this,
            mainScreen = binding.mainScreen,
            fragmentRoot = binding.root
        )

        dialogManager.startDialog(requireActivity(), "lvl2_card_chain")
    }

    override fun useHint() {
        hintManager.useHint(requireActivity())
    }

    override fun skipPuzzle() {
        puzzle.complete(saveRepo)
        passed()
    }
}