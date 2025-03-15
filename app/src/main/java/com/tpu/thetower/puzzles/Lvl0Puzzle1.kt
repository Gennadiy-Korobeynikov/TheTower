package com.tpu.thetower.puzzles

import com.tpu.thetower.Puzzle

class Lvl0Puzzle1(name: String) : Puzzle(name) {
    private val answer = "1234"
    override fun checkSolution(solution: String) : Boolean {
        if(solution == answer){
            super.complete()
            return true
        }
        return false
    }
}