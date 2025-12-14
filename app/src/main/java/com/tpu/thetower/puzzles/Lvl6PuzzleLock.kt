package com.tpu.thetower.puzzles

import android.app.Activity
import android.util.Log
import com.tpu.thetower.Puzzle

class Lvl6PuzzleLock(level: Int, puzzle: String) : Puzzle(level, puzzle) {
    private val answer = "34341"

    override fun checkSolution(activity: Activity, solution: String) : Boolean {
        Log.i("Lvl6PuzzleLock", "Checking solution: $solution against answer: $answer")
        if (solution == answer){
            super.complete(activity)
            return true
        }
        return false
    }

}