package com.tpu.thetower.puzzles

import android.app.Activity
import android.content.Context
import com.tpu.thetower.Puzzle

class Lvl2PuzzleLock(level: Int, puzzle: String) : Puzzle(level, puzzle) {
    private val answer = "59713"

    override fun checkSolution(activity: Activity, solution: String) : Boolean {
        if (solution == answer){
            super.complete(activity)
            return true
        }
        return false
    }

}