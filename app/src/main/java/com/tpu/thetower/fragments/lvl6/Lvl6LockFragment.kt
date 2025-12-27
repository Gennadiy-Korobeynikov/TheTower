package com.tpu.thetower.fragments.lvl6

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.databinding.FragmentLvl6LockBinding
import com.tpu.thetower.puzzles.Lvl6PuzzleLock
import com.tpu.thetower.utils.WheelSetupHelper
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.UiVisibilityController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl6LockFragment : Fragment(R.layout.fragment_lvl6_lock), Hintable {

    private var _binding: FragmentLvl6LockBinding? = null
    private val binding get() = _binding!!

    private val rvList = mutableListOf<RecyclerView>()

    private lateinit var puzzle: Puzzle
    private lateinit var hintManager: HintManager

    @Inject lateinit var soundManager: SoundManager
    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var hintManagerFactory: HintManager.Factory

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
        _binding = FragmentLvl6LockBinding.bind(view)

        soundManager.init()
        soundManager.loadSound(
            listOf(
                R.raw.sound_of_chain_release,
                R.raw.sound_of_segments_rotating_on_the_safe_lock
            )
        )

        hintManager = hintManagerFactory.create(
            hints = listOf(
                "lvl6_lock_hint1",
            ),
            level = 6,
            puzzle = "lock"
        )

        rvList.clear()
        rvList.addAll(
            listOf(
                binding.rvImage1,
                binding.rvImage2,
                binding.rvImage3,
                binding.rvImage4,
                binding.rvImage5
            )
        )

        puzzle = Lvl6PuzzleLock(6, "lock")
        setupWheels(images)
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
        UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
        binding.mainScreen.animate()
            .alpha(0.2f)
            .setDuration(2500)
            .withEndAction {
                FragmentNavigation.goBack(this)
            }
            .start()
    }

    override fun useHint() {
        hintManager.useHint(requireActivity())
    }

    override fun onPause() {
        super.onPause()
        soundManager.release() //todo Когда использовать?
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}