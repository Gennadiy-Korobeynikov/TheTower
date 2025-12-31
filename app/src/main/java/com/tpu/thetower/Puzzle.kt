package com.tpu.thetower

import android.app.Activity
import android.util.Log
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.models.PuzzleStatus

abstract class Puzzle(val level: Int, val puzzleName: String) {

    var isSolved: Boolean = false

    abstract fun checkSolution(
        activity: Activity,
        saveRepo: SaveRepository,
        solution: String = ""
    ): Boolean

    fun complete(saveRepo: SaveRepository) {
        isSolved = true
        saveRepo.savePuzzleData(level, puzzleName, status = PuzzleStatus.COMPLETED.value)
        Log.i("Puzzle", "$puzzleName completed")
    }
}
