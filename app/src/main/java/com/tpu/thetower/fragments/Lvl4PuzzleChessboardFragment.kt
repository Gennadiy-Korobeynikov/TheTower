package com.tpu.thetower.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.LoadManager
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.databinding.FragmentLvl4PuzzleChessboardBinding
import com.tpu.thetower.puzzles.Lvl4ChessboardPuzzle


class Lvl4PuzzleChessboardFragment : Fragment(R.layout.fragment_lvl4_puzzle_chessboard), Hintable {
    private lateinit var binding: FragmentLvl4PuzzleChessboardBinding
    private val cellStates = MutableList(64) { false }

    private val puzzle: Puzzle = Lvl4ChessboardPuzzle(4, "chess")
    private lateinit var saveManager: SaveManager
    private lateinit var hintManager: HintManager

    private lateinit var board: GridLayout

    private fun bind() {
        board = binding.gridBoard
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLvl4PuzzleChessboardBinding.bind(view)
        saveManager = SaveManager.getInstance()
        bind()
        hintManager = HintManager(
            listOf(
                "lvl4_chess_hint1","lvl4_chess_hint2", "lvl4_chess_hint3" ,"lvl4_chess_hint4",
            ),
            LoadManager.getPuzzleUsedHintsCount(requireActivity(), 4, "chess"),
            4, "chess"
        )
        board.post {
            val boardPx = board.width //
            setupBoard(boardPx)
        }
    }

    private fun switchCellState(cell: ImageView, index: Int) {
        cellStates[index] = !cellStates[index]
        cell.setImageResource(
            if (cellStates[index]) R.drawable.pressed_button else R.drawable.unpressed_button
        )
        val solutionString = cellStates
            .mapIndexedNotNull { idx, sel -> if (sel) idx.toString() else null }
            .joinToString(";")
        if (puzzle.checkSolution(requireActivity(), solutionString)) {
            passed()
        }
    }

    private fun setupBoard(boardPx: Int) {
        val cellSize = boardPx / 8 - 16
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
        FragmentManager.goBack(this)
    }

    override fun useHint() {
        hintManager.useHint(requireActivity())
    }
}