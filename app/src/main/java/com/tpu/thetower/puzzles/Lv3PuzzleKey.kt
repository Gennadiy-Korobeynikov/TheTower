package com.tpu.thetower.puzzles

import android.app.Activity
import com.tpu.thetower.Puzzle

class Lvl3PuzzleKey(level: Int, puzzle: String) : Puzzle(level, puzzle) {
    private val answer = "5010015010050"

    override fun checkSolution(activity: Activity, solution: String) : Boolean {
        if (solution == answer){
            super.complete(activity)
            return true
        }
        return false
    }

}