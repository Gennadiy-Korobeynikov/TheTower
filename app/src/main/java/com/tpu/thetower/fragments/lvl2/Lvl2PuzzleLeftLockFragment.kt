package com.tpu.thetower.fragments.lvl2

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.databinding.FragmentLvl2PuzzleLockBinding
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.puzzles.Lvl2PuzzleLock
import com.tpu.thetower.utils.WheelSetupHelper
import com.tpu.thetower.managers.UiVisibilityController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl2PuzzleLeftLockFragment : Fragment(R.layout.fragment_lvl2_puzzle_lock), Hintable {

    private lateinit var binding: FragmentLvl2PuzzleLockBinding

    private val puzzle: Puzzle = Lvl2PuzzleLock(2, "lock")
    private lateinit var hintManager: HintManager

    @Inject lateinit var soundManager: SoundManager
    @Inject lateinit var hintManagerFactory: HintManager.Factory
    @Inject lateinit var saveRepo: SaveRepository


    private val solution = "11111".toCharArray()
    private var isSolved = false

    private val images = arrayOf(
        R.drawable.lvl2_puzzle0_letter_c,
        R.drawable.lvl2_puzzle0_letter_d,
        R.drawable.lvl2_puzzle0_letter_h,
        R.drawable.lvl2_puzzle0_letter_i,
        R.drawable.lvl2_puzzle0_letter_l,
        R.drawable.lvl2_puzzle0_letter_m,
        R.drawable.lvl2_puzzle0_letter_n,
        R.drawable.lvl2_puzzle0_letter_o,
        R.drawable.lvl2_puzzle0_letter_u,
        R.drawable.lvl2_puzzle0_letter_v
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl2PuzzleLockBinding.bind(view)

        soundManager.init()
        soundManager.loadSound(
            listOf(
                R.raw.sound_of_the_lock_opening,
                R.raw.sound_of_segments_rotating_on_the_safe_lock
            )
        )

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
            .withEndAction { FragmentNavigation.goBack(this) }
            .start()
        // TODO Добавить звук открывающейся двери сейфа
    }

    override fun useHint() {
        hintManager.useHint(requireActivity())
    }

    override fun skipPuzzle() {
        puzzle.complete(saveRepo)
        passed()
    }
}