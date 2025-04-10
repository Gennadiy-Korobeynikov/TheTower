package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
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
import com.tpu.thetower.databinding.FragmentLvl2PuzzleHooverBinding
import com.tpu.thetower.puzzles.Direction
import com.tpu.thetower.puzzles.Lvl0Puzzle1
import com.tpu.thetower.puzzles.Lvl2PuzzleHoover


class Lvl2PuzzleHooverFragment : Fragment(R.layout.fragment_lvl2_puzzle_hoover), Hintable {

    private lateinit var binding: FragmentLvl2PuzzleHooverBinding

    private lateinit var tvCoordinates : TextView
    private lateinit var tvDirection: TextView
    private lateinit var tvRestart: TextView
    private lateinit var tvWin : TextView

    private lateinit var btnRight: Button
    private lateinit var btnLeft: Button
    private lateinit var btnForward: Button


    private val puzzleHoover = Lvl2PuzzleHoover("Lvl2PuzzleHooverPuzzle1")
    private lateinit var hintManager: HintManager
    private lateinit var soundManager: SoundManager


    private var restart : Boolean = false
    private var win : Boolean = false



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl2PuzzleHooverBinding.bind(view)
        bindView()
        setListeners()
        test()

//        soundManager = SoundManager.getInstance()
//        soundManager.loadSound(requireContext(), listOf(
//            R.raw.sound_of_the_lock_opening,
//            R.raw.sound_of_segments_rotating_on_the_safe_lock
//        ))

        hintManager = HintManager(
            listOf(
                "lvl0_puzzle1_hint1",
                "lvl0_puzzle1_hint2",
                "lvl0_puzzle1_hint3"
            ),
            LoadManager.getPuzzleUsedHintsCount(requireActivity(), 0, 0),
            0, 0
        )
    }

    private fun bindView() {
        tvCoordinates = binding.tvCoordinates
        tvDirection = binding.tvDirection
        tvRestart = binding.tvRestart
        tvWin = binding.tvWin

        btnRight = binding.btnRight
        btnLeft = binding.btnLeft
        btnForward = binding.btnForward
    }


    private  fun setListeners() {
        btnLeft.setOnClickListener {
            puzzleHoover.changeDirection(clockwise = false)
            test()
        }
        btnRight.setOnClickListener {
            puzzleHoover.changeDirection(clockwise = true)
            test()
        }
        btnForward.setOnClickListener {
            tvRestart.text = ""
            tvWin.text = ""
            puzzleHoover.moveForward()
            restart = puzzleHoover.isWall()
            win = puzzleHoover.checkSolution(requireContext())
            test()
        }
    }

// Временно для тестирования
    private fun test() {
        if (restart) {
            tvRestart.text = "Въебался в стену!"
            restart = false
        }

        if (win) {
            tvWin.text = "Победил!"
            win = false
        }

        tvDirection.text =
            when (puzzleHoover.currDirection) {
                Direction.Right -> "Вправо"
                Direction.Left -> "Влево"
                Direction.Down -> "Вниз"
                Direction.Up -> "Вверх"
            }
        tvCoordinates.text = "${puzzleHoover.currPositionX- 1 } ${puzzleHoover.currPositionY -1  }"

    }

    override fun useHint() {
        hintManager.useHint(this.requireActivity())
    }


}