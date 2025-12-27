package com.tpu.thetower.puzzles

import android.app.Activity
import android.util.Log
import com.tpu.thetower.Puzzle
import com.tpu.thetower.managers.SaveRepository

class Lvl6PuzzleLock(level: Int, puzzle: String) : Puzzle(level, puzzle) {
    private val answer = "34341"

    override fun checkSolution(activity: Activity, saveRepo: SaveRepository, solution: String): Boolean {
        Log.i("Lvl6PuzzleLock", "Checking solution: $solution against answer: $answer")
        return if (solution == answer) {
            complete(saveRepo)
            true
        } else {
            false
        }
    }

}