package com.tpu.thetower.puzzles

import android.content.Context
import com.tpu.thetower.Puzzle

class ChessboardPuzzle(name: String) : Puzzle(name) {
    private val answer = setOf(0, 8, 16, 63)

    override fun checkSolution(context: Context, solution: String): Boolean {
        val solutionSet = solution
            .takeIf { it.isNotEmpty() }
            ?.split(';')
            ?.map(String::toInt)
            ?.toSet()
            ?: emptySet()

        return if (solutionSet == answer) {
            super.complete(context)
            true
        } else {
            false
        }
    }

}