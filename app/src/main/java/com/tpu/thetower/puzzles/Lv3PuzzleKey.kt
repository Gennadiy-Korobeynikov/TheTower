package com.tpu.thetower.puzzles

import android.content.Context
import com.tpu.thetower.Puzzle

class Lvl3PuzzleKey(level: Int, puzzle: String) : Puzzle(level, puzzle) {
    private val answer = "400300250450350"

    override fun checkSolution(context: Context, solution: String) : Boolean {
        if (solution == answer){
            super.complete(context)
            return true
        }
        return false
    }

}