package com.tpu.thetower.puzzles

import android.app.Activity
import com.tpu.thetower.Puzzle


class Lvl5PuzzleFishRack(level: Int, puzzle: String) : Puzzle(level, puzzle) {
    private val answer = "1234567890"

    override fun checkSolution(activity: Activity, solution: String) : Boolean {
        if (solution == answer){
            super.complete(activity)
            return true
        }
        return false
    }

}