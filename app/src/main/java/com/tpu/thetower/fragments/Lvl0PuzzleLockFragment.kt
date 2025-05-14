package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tpu.thetower.DialogManager
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.LevelAccessManager
import com.tpu.thetower.LoadManager
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.SoundManager
import com.tpu.thetower.databinding.FragmentLvl0PuzzleLockBinding
import com.tpu.thetower.puzzles.Lvl0PuzzleLock
import com.tpu.thetower.utils.WheelSetupHelper


class Lvl0PuzzleLockFragment : Fragment(R.layout.fragment_lvl0_puzzle_lock), Hintable {

    private lateinit var binding: FragmentLvl0PuzzleLockBinding

    private lateinit var rv1: RecyclerView
    private lateinit var rv2: RecyclerView
    private lateinit var rv3: RecyclerView
    private lateinit var rv4: RecyclerView

    private lateinit var mainScreen: FrameLayout


    private val puzzle: Puzzle = Lvl0PuzzleLock(0, "lock")
    private lateinit var hintManager: HintManager
    private lateinit var soundManager: SoundManager

    private var solution = "1111".toCharArray()

    private var isSolved = false

    private val images = arrayOf(
        arrayOf(
            R.drawable.lvl0_puzzle1_num_0,
            R.drawable.lvl0_puzzle1_num_1,
            R.drawable.lvl0_puzzle1_num_2,
            R.drawable.lvl0_puzzle1_num_3,
            R.drawable.lvl0_puzzle1_num_4_2,
            R.drawable.lvl0_puzzle1_num_5,
            R.drawable.lvl0_puzzle1_num_6,
            R.drawable.lvl0_puzzle1_num_7,
            R.drawable.lvl0_puzzle1_num_8,
            R.drawable.lvl0_puzzle1_num_9
        ),
        arrayOf(
            R.drawable.lvl0_puzzle1_num_0,
            R.drawable.lvl0_puzzle1_num_1,
            R.drawable.lvl0_puzzle1_num_2,
            R.drawable.lvl0_puzzle1_num_3,
            R.drawable.lvl0_puzzle1_num_4,
            R.drawable.lvl0_puzzle1_num_5,
            R.drawable.lvl0_puzzle1_num_6_1,
            R.drawable.lvl0_puzzle1_num_7,
            R.drawable.lvl0_puzzle1_num_8,
            R.drawable.lvl0_puzzle1_num_9
        ),
        arrayOf(
            R.drawable.lvl0_puzzle1_num_0,
            R.drawable.lvl0_puzzle1_num_1_1,
            R.drawable.lvl0_puzzle1_num_2,
            R.drawable.lvl0_puzzle1_num_3,
            R.drawable.lvl0_puzzle1_num_4_1,
            R.drawable.lvl0_puzzle1_num_5,
            R.drawable.lvl0_puzzle1_num_6,
            R.drawable.lvl0_puzzle1_num_7,
            R.drawable.lvl0_puzzle1_num_8,
            R.drawable.lvl0_puzzle1_num_9
        ),
        arrayOf(
            R.drawable.lvl0_puzzle1_num_0,
            R.drawable.lvl0_puzzle1_num_1,
            R.drawable.lvl0_puzzle1_num_2,
            R.drawable.lvl0_puzzle1_num_3,
            R.drawable.lvl0_puzzle1_num_4,
            R.drawable.lvl0_puzzle1_num_5,
            R.drawable.lvl0_puzzle1_num_6,
            R.drawable.lvl0_puzzle1_num_7_1,
            R.drawable.lvl0_puzzle1_num_8,
            R.drawable.lvl0_puzzle1_num_9
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl0PuzzleLockBinding.bind(view)
        bindView()

        soundManager = SoundManager.getInstance()
        soundManager.loadSound(
            requireContext(), listOf(
                R.raw.sound_of_the_lock_opening,
                R.raw.sound_of_segments_rotating_on_the_safe_lock
            )
        )

        hintManager = HintManager(
            listOf(
                "lvl0_puzzle1_hint1",
                "lvl0_puzzle1_hint2",
                "lvl0_puzzle1_hint3"
            ),
            LoadManager.getPuzzleUsedHintsCount(requireActivity(), 0, "lock"),
            0, "lock"
        )
        setupWheels(images)
    }

    private fun bindView() {
        rv1 = binding.rvImage1
        rv2 = binding.rvImage2
        rv3 = binding.rvImage3
        rv4 = binding.rvImage4

        mainScreen = binding.mainScreen
    }

    private fun setupWheels(data: Array<Array<Int>>) {
        setupWheel(rv1, data[0], 0)
        setupWheel(rv2, data[1], 1)
        setupWheel(rv3, data[2], 2)
        setupWheel(rv4, data[3], 3)
    }

    private fun setupWheel(rv: RecyclerView, data: Array<Int>, rvIndex: Int) {
        WheelSetupHelper.setupWheel(
            rv = rv,
            data = data,
            rvIndex = rvIndex,
            solution = solution,
            context = requireContext(),
            puzzle = puzzle,
            soundManager = soundManager,
            rotationSoundResId = R.raw.sound_of_segments_rotating_on_the_safe_lock,
            isSolvedRef = { isSolved },
            onSolvedListener = object : WheelSetupHelper.WheelSolvedListener {
                override fun onPuzzleSolved() {
                    soundManager.playSound(R.raw.sound_of_the_lock_opening)
                    passed()
                }
            }
        )
    }

    private fun passed() {
        LevelAccessManager.upgradeAccessLvl(this)
        isSolved = true
        FragmentManager.hideGoBackArrow(requireActivity())
        mainScreen.animate()
            .alpha(0.2f)
            .setDuration(2500)
            .withEndAction {
                FragmentManager.goBack(this)
                DialogManager.startDialog(requireActivity(), "lvl0_puzzle1_solved")
            }
            .start()
        // TODO Добавить звук открывающейся двери сейфа
        // TODO Опционально добавить фото карты при выходе из сейфа
    }

    override fun useHint() {
        hintManager.useHint(this.requireActivity())
    }


}