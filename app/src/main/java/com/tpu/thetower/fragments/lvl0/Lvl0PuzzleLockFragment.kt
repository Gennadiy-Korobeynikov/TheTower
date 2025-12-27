package com.tpu.thetower.fragments.lvl0

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tpu.thetower.Hintable
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl0PuzzleLockBinding
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.puzzles.Lvl0PuzzleLock
import com.tpu.thetower.utils.WheelSetupHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl0PuzzleLockFragment : Fragment(R.layout.fragment_lvl0_puzzle_lock), Hintable {

    private lateinit var binding: FragmentLvl0PuzzleLockBinding

    private val puzzle: Puzzle = Lvl0PuzzleLock(0, "lock")
    private lateinit var hintManager: HintManager

    @Inject lateinit var soundManager: SoundManager
    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var dialogManager: DialogManager
    @Inject lateinit var hintManagerFactory: HintManager.Factory
    @Inject lateinit var saveRepo : SaveRepository

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

        soundManager.init()
        soundManager.loadSound(
            listOf(
                R.raw.sound_of_the_lock_opening,
                R.raw.sound_of_segments_rotating_on_the_safe_lock
            )
        )

        hintManager = hintManagerFactory.create(
            hints = listOf(
                "lvl0_puzzle1_hint1",
                "lvl0_puzzle1_hint2",
                "lvl0_puzzle1_hint3"
            ),
            level = 0,
            puzzle = "lock"
        )

        setupWheels(images)
    }

    private fun setupWheels(data: Array<Array<Int>>) {
        setupWheel(binding.rvImage1, data[0], 0)
        setupWheel(binding.rvImage2, data[1], 1)
        setupWheel(binding.rvImage3, data[2], 2)
        setupWheel(binding.rvImage4, data[3], 3)
    }

    private fun setupWheel(rv: RecyclerView, data: Array<Int>, rvIndex: Int) {
        WheelSetupHelper.setupWheel(
            rv = rv,
            data = data,
            layoutImage = R.layout.item_image,
            orientation = LinearLayoutManager.HORIZONTAL,
            rvIndex = rvIndex,
            solution = solution,
            activity = requireActivity(),
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
        isSolved = true
        UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)

        binding.mainScreen.animate()
            .alpha(0.2f)
            .setDuration(2500)
            .withEndAction {
                FragmentNavigation.goBack(this)
                dialogManager.startDialog(requireActivity(), "lvl0_puzzle1_solved")
            }
            .start()
    }

    override fun useHint() {
        hintManager.useHint(requireActivity())
    }

    override fun skipPuzzle() {
        puzzle.complete(saveRepo)
        passed()
    }
}