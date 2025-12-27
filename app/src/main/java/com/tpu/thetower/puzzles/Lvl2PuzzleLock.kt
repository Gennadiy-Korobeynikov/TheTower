package com.tpu.thetower.puzzles

import android.app.Activity
import com.tpu.thetower.Puzzle
import com.tpu.thetower.managers.SaveRepository

class Lvl2PuzzleLock(level: Int, puzzle: String) : Puzzle(level, puzzle) {
    private val answer = "59713"

    override fun checkSolution(activity: Activity, saveRepo: SaveRepository, solution: String): Boolean {
        return if (solution == answer) {
            complete(saveRepo)
            true
        } else {
            false
        }
    }

}