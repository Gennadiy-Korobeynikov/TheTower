package com.tpu.thetower.puzzles

import android.app.Activity
import com.tpu.thetower.Puzzle
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.SaveRepository

class Lvl2PuzzlePassword(
    level: Int,
    puzzle: String,
    private val dialogManager: DialogManager
) : Puzzle(level, puzzle) {

    private val answer = "Gerberd"

    override fun checkSolution(activity: Activity, saveRepo: SaveRepository, solution: String): Boolean {
        return if (solution == answer) {
            complete(saveRepo)
            dialogManager.startDialog(activity, "lvl2_computer_lore")
            true
        } else {
            false
        }
    }

}