package com.tpu.thetower

import android.app.Activity
import android.content.Context
import android.util.Log

// Заготовка под класс головоломок
abstract class Puzzle(val level: Int, val puzzle: String) {

    var isSolved: Boolean = false
    var usedHintsCount: Int = 0

    private lateinit var saveManager: SaveManager

    abstract fun checkSolution(activity: Activity, solution : String = ""): Boolean

    fun complete(activity: Activity) {
        isSolved = true
        saveManager = SaveManager.getInstance()
        saveManager.savePuzzleData(activity, level, puzzle, status = "completed")
        Log.i("Puzzle", "${puzzle} completed")
    }


}