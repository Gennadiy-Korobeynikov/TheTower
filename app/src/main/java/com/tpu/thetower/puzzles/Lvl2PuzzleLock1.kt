package com.tpu.thetower.puzzles

import android.content.Context
import com.tpu.thetower.Puzzle

class Lvl2PuzzleLock1(level: Int, puzzle: String) : Puzzle(level, puzzle) {
    private val answer = "Gerberd"

    override fun checkSolution(context: Context, solution: String) : Boolean {
        if (solution == answer){
            super.complete(context)
            return true
        }
        return false
    }

}