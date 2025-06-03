package com.tpu.thetower.puzzles

import android.app.Activity
import android.content.Context
import com.tpu.thetower.DialogManager
import com.tpu.thetower.Puzzle

class Lvl2PuzzlePassword(level: Int, puzzle: String) : Puzzle(level, puzzle) {
    private val answer = "Gerberd"

    override fun checkSolution(activity: Activity, solution: String) : Boolean {
        if (solution == answer){
            super.complete(activity)
            DialogManager.startDialog(activity, "lvl2_computer_lore")
            return true
        }
        return false
    }

}