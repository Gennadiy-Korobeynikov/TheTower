package com.tpu.thetower.puzzles

import android.content.Context
import com.tpu.thetower.Puzzle

class Lvl3Puzzle1(name: String) : Puzzle(name) {
    private val answer = "165243"

    override fun checkSolution(context: Context, solution: String) : Boolean {
        if (solution == answer){
            super.complete(context)
            return true
        }
        return false
    }

}