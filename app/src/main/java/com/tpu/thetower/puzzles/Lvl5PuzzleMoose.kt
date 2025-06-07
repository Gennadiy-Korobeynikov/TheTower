package com.tpu.thetower.puzzles

import android.app.Activity
import com.tpu.thetower.Puzzle


class Lvl5PuzzleMoose(level: Int, puzzle: String) : Puzzle(level, puzzle) {
    private val answer = "01100"

    override fun checkSolution(activity: Activity, solution: String) : Boolean {
        if (solution == answer){
            super.complete(activity)
            return true
        }
        return false
    }

}