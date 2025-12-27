package com.tpu.thetower.puzzles

import android.app.Activity
import com.tpu.thetower.Puzzle
import com.tpu.thetower.managers.SaveRepository

class Lvl4ChessboardPuzzle(level: Int, puzzle: String) : Puzzle(level, puzzle) {
    private val answer = setOf(10, 16, 22, 36, 40, 50)

    override fun checkSolution(activity: Activity, saveRepo: SaveRepository, solution: String): Boolean {
        val solutionSet = solution
            .takeIf { it.isNotEmpty() }
            ?.split(';')
            ?.map(String::toInt)
            ?.toSet()
            ?: emptySet()

        return if (solutionSet == answer) {
            complete(saveRepo)
            true
        } else {
            false
        }
    }

}