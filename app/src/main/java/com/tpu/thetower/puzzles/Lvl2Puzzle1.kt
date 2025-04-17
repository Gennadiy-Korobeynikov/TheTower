package com.tpu.thetower.puzzles

import android.content.Context
import com.tpu.thetower.Puzzle

class Lvl2Puzzle1(name: String) : Puzzle(name) {
    private val answer = "cooper"

    override fun checkSolution(context: Context, solution: String) : Boolean {
        if (solution == answer){
            super.complete(context)
            return true
        }
        return false
    }

}