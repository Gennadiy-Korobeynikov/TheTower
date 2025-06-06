package com.tpu.thetower.puzzles

import android.app.Activity
import android.content.Context
import com.tpu.thetower.Puzzle

class ChessboardPuzzle(level: Int, puzzle: String) : Puzzle(level, puzzle) {
    private val answer = setOf(0, 8, 16, 63)

    override fun checkSolution(activity: Activity, solution: String): Boolean {
        val solutionSet = solution
            .takeIf { it.isNotEmpty() }
            ?.split(';')
            ?.map(String::toInt)
            ?.toSet()
            ?: emptySet()

        return if (solutionSet == answer) {
            super.complete(activity)
            true
        } else {
            false
        }
    }

}