package com.tpu.thetower.puzzles

import android.app.Activity
import com.tpu.thetower.Puzzle
import com.tpu.thetower.managers.SaveRepository

class Lvl3PuzzleKey(level: Int, puzzle: String) : Puzzle(level, puzzle) {
    private val answer = "5010015010050"

    override fun checkSolution(activity: Activity?, saveRepo: SaveRepository, solution: String): Boolean {
        return if (solution == answer) {
            complete(saveRepo)
            true
        } else {
            false
        }
    }

}