package com.tpu.thetower

import android.app.Activity
import android.util.Log
import com.tpu.thetower.managers.SaveRepository

// Заготовка под класс головоломок
abstract class Puzzle(val level: Int, val puzzle: String) {

    var isSolved: Boolean = false
    var usedHintsCount: Int = 0

    private val saveRepo: SaveRepository = SaveRepository.getInstance()

    abstract fun checkSolution(activity: Activity, solution : String = ""): Boolean

    fun complete(activity: Activity) {
        isSolved = true
        saveRepo.savePuzzleData(activity, level, puzzle, status = "completed")
        Log.i("Puzzle", "${puzzle} completed")
    }


}