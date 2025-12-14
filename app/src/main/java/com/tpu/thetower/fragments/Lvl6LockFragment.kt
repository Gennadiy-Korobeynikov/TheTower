package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.LoadManager
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.SoundManager
import com.tpu.thetower.databinding.FragmentLvl6LockBinding
import com.tpu.thetower.puzzles.Lvl6PuzzleLock
import com.tpu.thetower.utils.WheelSetupHelper

class Lvl6LockFragment : Fragment(R.layout.fragment_lvl6_lock), Hintable {

    lateinit var binding: FragmentLvl6LockBinding

    lateinit var rv1: RecyclerView
    lateinit var rv2: RecyclerView
    lateinit var rv3: RecyclerView
    lateinit var rv4: RecyclerView
    lateinit var rv5: RecyclerView
    private val rvList = mutableListOf<RecyclerView>()

    private lateinit var mainScreen: FrameLayout

    private lateinit var puzzle: Puzzle
    private lateinit var hintManager: HintManager
    private lateinit var soundManager: SoundManager

    private var solution = "00000".toCharArray()

    private var isSolved = false

    private val images =
        arrayOf(
            arrayOf(
                R.drawable.lvl6_puzzle0_dots_1,
                R.drawable.lvl6_puzzle0_dots_3,
                R.drawable.lvl6_puzzle0_dots_4,
                R.drawable.lvl6_puzzle0_dots_5,
                R.drawable.lvl6_puzzle0_dots_9
            ),
            arrayOf(
                R.drawable.lvl6_puzzle0_color_blue,
                R.drawable.lvl6_puzzle0_color_green,
                R.drawable.lvl6_puzzle0_color_pink,
                R.drawable.lvl6_puzzle0_color_cyan,
                R.drawable.lvl6_puzzle0_color_orange
            ),
            arrayOf(
                R.drawable.lvl6_puzzle0_path_1,
                R.drawable.lvl6_puzzle0_path_2,
                R.drawable.lvl6_puzzle0_path_3,
                R.drawable.lvl6_puzzle0_path_4,
                R.drawable.lvl6_puzzle0_path_5
            ),
            arrayOf(
                R.drawable.lvl6_puzzle0_pic_1,
                R.drawable.lvl6_puzzle0_pic_2,
                R.drawable.lvl6_puzzle0_pic_3,
                R.drawable.lvl6_puzzle0_pic_4,
                R.drawable.lvl6_puzzle0_pic_5
            ),
            arrayOf(
                R.drawable.lvl6_puzzle0_tool_1,
                R.drawable.lvl6_puzzle0_tool_2,
                R.drawable.lvl6_puzzle0_tool_3,
                R.drawable.lvl6_puzzle0_tool_4,
                R.drawable.lvl6_puzzle0_tool_5
            )
        )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLvl6LockBinding.bind(view)
        bindView()
        soundManager = SoundManager.getInstance()
        soundManager.init()
        soundManager.loadSound(
            requireContext(), listOf(
                R.raw.sound_of_chain_release,
                R.raw.sound_of_segments_rotating_on_the_safe_lock
            )
        )

        hintManager = HintManager(
            listOf(
                "lvl6_lock_hint1",
            ),
            LoadManager.getPuzzleUsedHintsCount(requireActivity(), 6, "lock"),
            6, "lock"
        )

        rvList.addAll(listOf(rv1, rv2, rv3, rv4, rv5))
        puzzle = Lvl6PuzzleLock(6, "lock")
        setupWheels(images)
    }

    private fun bindView() {
        rv1 = binding.rvImage1
        rv2 = binding.rvImage2
        rv3 = binding.rvImage3
        rv4 = binding.rvImage4
        rv5 = binding.rvImage5
        mainScreen = binding.mainScreen
    }

    private fun setupWheels(data: Array<Array<Int>>) {
        rvList.forEachIndexed { index, rv ->
            setupWheel(rv, data[index], index)
        }
    }

    private fun setupWheel(rv: RecyclerView, data: Array<Int>, rvIndex: Int) {
        WheelSetupHelper.setupWheel(
            rv = rv,
            data = data,
            layoutImage = R.layout.item_lvl6_image,
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
                    soundManager.playSound(R.raw.sound_of_chain_release)
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
    }

    override fun useHint() {
        hintManager.useHint(requireActivity())
    }

}