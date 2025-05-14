package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.LoadManager
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.SoundManager
import com.tpu.thetower.databinding.FragmentLvl2PuzzleLockBinding
import com.tpu.thetower.puzzles.Lvl2PuzzleLock0
import com.tpu.thetower.puzzles.Lvl2PuzzleChat
import com.tpu.thetower.utils.WheelSetupHelper


class Lvl2PuzzleLockFragment : Fragment(R.layout.fragment_lvl2_puzzle_lock), Hintable {

    private lateinit var binding: FragmentLvl2PuzzleLockBinding

    private lateinit var rv1: RecyclerView
    private lateinit var rv2: RecyclerView
    private lateinit var rv3: RecyclerView
    private lateinit var rv4: RecyclerView
    private lateinit var rv5: RecyclerView
    private val rvList = mutableListOf<RecyclerView>()

    private lateinit var mainScreen: FrameLayout


    private lateinit var puzzle: Puzzle
    private lateinit var hintManager: HintManager
    private lateinit var soundManager: SoundManager

    private var solution = "11111".toCharArray()

    private var isSolved = false

    private val images =
        arrayOf(
            R.drawable.lvl2_puzzle0_a,
            R.drawable.lvl2_puzzle0_b,
            R.drawable.lvl2_puzzle0_c,
            R.drawable.lvl2_puzzle0_d,
            R.drawable.lvl2_puzzle0_e,
            R.drawable.lvl2_puzzle0_h,
            R.drawable.lvl2_puzzle0_l,
            R.drawable.lvl2_puzzle0_n,
            R.drawable.lvl2_puzzle0_u
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl2PuzzleLockBinding.bind(view)
        bindView()
        soundManager = SoundManager.getInstance()
        soundManager.loadSound(
            requireContext(), listOf(
                R.raw.sound_of_the_lock_opening,
                R.raw.sound_of_segments_rotating_on_the_safe_lock
            )
        )

        rvList.addAll(listOf(rv1, rv2, rv3, rv4, rv5))


        hintManager = HintManager(
            listOf(
                "lvl2_puzzle0_hint",
            ),
            LoadManager.getPuzzleUsedHintsCount(requireActivity(), 2, "lock"),
            2, "lock"
        )
        requireActivity().supportFragmentManager
            .setFragmentResultListener("puzzleChoosing", viewLifecycleOwner) { _, bundle ->
                val puzzleNum = bundle.getInt("puzzleNum")
                when (puzzleNum) {
                    0 -> puzzle = Lvl2PuzzleLock0(2, "lock")
                    1 -> puzzle = Lvl2PuzzleChat(2, "chat")
                }
                setupWheels(images)
            }

    }

    private fun bindView() {
        rv1 = binding.rvImage1
        rv2 = binding.rvImage2
        rv3 = binding.rvImage3
        rv4 = binding.rvImage4
        rv5 = binding.rvImage5
//
        mainScreen = binding.mainScreen
    }

    private fun setupWheels(data: Array<Int>) {
        rvList.forEachIndexed { index, rv ->
            setupWheel(rv, data, index)
        }
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
        isSolved = true
        FragmentManager.hideGoBackArrow(requireActivity())
        mainScreen.animate()
            .alpha(0.2f)
            .setDuration(2500)
            .withEndAction {
                FragmentManager.goBack(this)
            }
            .start()
        // TODO Добавить звук открывающейся двери сейфа
    }

    override fun useHint() {
        hintManager.useHint(requireActivity())
    }
}