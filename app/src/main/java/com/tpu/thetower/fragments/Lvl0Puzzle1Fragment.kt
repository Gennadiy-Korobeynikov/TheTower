package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
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
import com.tpu.thetower.adapters.ImageCodeAdapter
import com.tpu.thetower.databinding.FragmentLvl0Puzzle1Binding
import com.tpu.thetower.puzzles.Lvl0Puzzle1


class Lvl0Puzzle1Fragment : Fragment(R.layout.fragment_lvl0_puzzle1), Hintable {

    private lateinit var binding: FragmentLvl0Puzzle1Binding

    private lateinit var rv1: RecyclerView
    private lateinit var rv2: RecyclerView
    private lateinit var rv3: RecyclerView
    private lateinit var rv4: RecyclerView

    private lateinit var mainScreen: FrameLayout


    private val puzzle: Puzzle = Lvl0Puzzle1("Lvl0Puzzle1")
    private lateinit var hintManager: HintManager
    private lateinit var soundManager: SoundManager

    private var solution = puzzle.lastSolution.toCharArray()

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

        binding = FragmentLvl0Puzzle1Binding.bind(view)
        bindView()

        soundManager = SoundManager.getInstance()
        soundManager.loadSound(requireContext(), listOf(
            R.raw.sound_of_the_lock_opening,
            R.raw.sound_of_segments_rotating_on_the_safe_lock
        ))

        hintManager = HintManager(
            listOf(
                "lvl0_puzzle1_hint1",
                "lvl0_puzzle1_hint2",
                "lvl0_puzzle1_hint3"
            ),
            LoadManager.getPuzzleUsedHintsCount(requireActivity(), 0, 0),
            0, 0
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
        val adapter = ImageCodeAdapter(data)
        rv.adapter = adapter

        val layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rv.layoutManager = layoutManager

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rv)
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(rv: RecyclerView, newState: Int) {
                super.onScrollStateChanged(rv, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    soundManager.playSound(R.raw.sound_of_segments_rotating_on_the_safe_lock)
                    val centerView = snapHelper.findSnapView(layoutManager) ?: return
                    val position = layoutManager.getPosition(centerView)
                    if (position != RecyclerView.NO_POSITION) {
                        val digit = position % 10
                        solution[rvIndex] = digit.digitToChar()

                        val isSolved = puzzle.checkSolution(requireContext(), String(solution))

                        if (isSolved){
                            soundManager.playSound(R.raw.sound_of_the_lock_opening)
                            passed()
                        }
                    }
                }
            }
        })
        rv.post {
            val midPosition = -2 + adapter.itemCount / 2
            layoutManager.scrollToPositionWithOffset(midPosition, 0)
        }

    }

    private fun passed() {
        LevelAccessManager.upgradeAccessLvl(this)
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