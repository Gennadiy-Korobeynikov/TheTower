package com.tpu.thetower.puzzles

import android.content.Context
import com.tpu.thetower.Puzzle

class Lvl2Puzzle2(name: String) : Puzzle(name) {
    private val answer = "01010"

    override fun checkSolution(context: Context, solution: String) : Boolean {
        if (solution == answer){
            super.complete(name, context)
            return true
        }
        return false
    }

}