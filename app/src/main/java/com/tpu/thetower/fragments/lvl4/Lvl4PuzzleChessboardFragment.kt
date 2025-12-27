package com.tpu.thetower.fragments.lvl4

import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.databinding.FragmentLvl4PuzzleChessboardBinding
import com.tpu.thetower.puzzles.Lvl4ChessboardPuzzle
import com.tpu.thetower.managers.FragmentNavigation


@AndroidEntryPoint
class Lvl4PuzzleChessboardFragment : Fragment(R.layout.fragment_lvl4_puzzle_chessboard), Hintable {
    private lateinit var binding: FragmentLvl4PuzzleChessboardBinding
    private val cellStates = MutableList(64) { false }

    private val puzzle: Puzzle = Lvl4ChessboardPuzzle(4, "chess")

    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var hintManagerFactory: HintManager.Factory

    private lateinit var hintManager: HintManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLvl4PuzzleChessboardBinding.bind(view)

        binding.gridBoard.post {
            val boardPx = binding.gridBoard.width //
            setupBoard(boardPx)
        }

        hintManager = hintManagerFactory.create(
            hints = listOf(
                "lvl4_chess_hint1","lvl4_chess_hint2", "lvl4_chess_hint3" ,"lvl4_chess_hint4",
            ),
            level = 4,
            puzzle = "chess"
        )
    }

    private fun switchCellState(cell: ImageView, index: Int) {
        cellStates[index] = !cellStates[index]
        cell.setImageResource(
            if (cellStates[index]) R.drawable.pressed_button else R.drawable.unpressed_button
        )
        val solutionString = cellStates
            .mapIndexedNotNull { idx, sel -> if (sel) idx.toString() else null }
            .joinToString(";")
        if (puzzle.checkSolution(requireActivity(), saveRepo, solutionString)) {
            passed()
        }
    }

    private fun setupBoard(boardPx: Int) {
        val cellSize = boardPx / 8 - 16
        val board: GridLayout = binding.gridBoard
        board.rowCount = 8
        board.columnCount = 8

        for (i in 0 until 64) {
            val cell = ImageView(requireContext()).apply {
                setImageResource(R.drawable.unpressed_button)
                setBackgroundResource(0)
                setOnClickListener {
                    switchCellState(this, i)
                }
                scaleType = ImageView.ScaleType.CENTER_CROP
            }

            val params = GridLayout.LayoutParams().apply {
                width = cellSize
                height = cellSize
                rowSpec = GridLayout.spec(i / 8)
                columnSpec = GridLayout.spec(i % 8)
                setMargins(0, 0, 18, 16)
            }

            board.addView(cell, params)
        }

    }

    private fun passed() {
        FragmentNavigation.goBack(this)
    }

    override fun useHint() {
        hintManager.useHint(requireActivity())
    }
}