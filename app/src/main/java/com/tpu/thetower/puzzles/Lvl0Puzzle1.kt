package com.tpu.thetower.puzzles

import android.content.Context
import com.tpu.thetower.Puzzle

class Lvl0Puzzle1(name: String) : Puzzle(name) {
    private val answer = "4830"

    override fun checkSolution(context: Context, solution: String) : Boolean {
        lastSolution = answer
        if (solution == answer){
            super.complete(name, context)
            return true
        }
        return false
    }

}