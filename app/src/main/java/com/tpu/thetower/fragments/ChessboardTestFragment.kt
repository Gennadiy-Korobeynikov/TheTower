package com.tpu.thetower.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentChessboardTestBinding
import com.tpu.thetower.puzzles.ChessboardPuzzle


class ChessboardTestFragment : Fragment(R.layout.fragment_chessboard_test) {
    private lateinit var binding: FragmentChessboardTestBinding
    private val cellStates = MutableList(64) { false }

    private val puzzle: Puzzle = ChessboardPuzzle("Lvl2Puzzle1")


    private lateinit var board: GridLayout


    private fun bind() {
        board = binding.gridBoard
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChessboardTestBinding.bind(view)
        bind()
        val screenHeight = requireContext().resources.displayMetrics.heightPixels
        val boardPx = (screenHeight * 0.7).toInt()
        setupBoard(boardPx)
    }

    private fun switchCellState(cell: View, index: Int) {
        cellStates[index] = !cellStates[index]
        cell.setBackgroundColor(
            if (cellStates[index]) Color.BLACK else Color.WHITE
        )
        val solutionString = cellStates
            .mapIndexedNotNull { idx, sel -> if (sel) idx.toString() else null }
            .joinToString(";")
        if (puzzle.checkSolution(requireContext(), solutionString)) {
            passed()
        }
    }

    private fun setupBoard(boardPx: Int) {
        val cellSize = boardPx / 8
        board.rowCount = 8
        board.columnCount = 8

        for (i in 0 until 64) {
            val cell = View(requireContext()).apply {
                setBackgroundColor(Color.WHITE)
                setOnClickListener {
                    switchCellState(it, i)
                }
            }

            val params = GridLayout.LayoutParams().apply {
                width = cellSize
                height = cellSize
                rowSpec = GridLayout.spec(i / 8)
                columnSpec = GridLayout.spec(i % 8)
                setMargins(1, 1, 1, 1)
            }

            board.addView(cell, params)
        }
    }

    private fun passed() {
        Log.i("Puzzle", "Passed")
    }
}