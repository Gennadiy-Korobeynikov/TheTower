package com.tpu.thetower

import android.content.Context
import android.util.Log

// Заготовка под класс головоломок
abstract class Puzzle(val level: Int, val puzzle: String) {

    var isSolved: Boolean = false
    var usedHintsCount: Int = 0

    private lateinit var saveManager: SaveManager

    abstract fun checkSolution(context: Context, solution : String = ""): Boolean

    fun complete(context: Context) {
        isSolved = true
        saveManager = SaveManager.getInstance()
        saveManager.savePuzzleData(context, level, puzzle, status = "completed")
        Log.i("Puzzle", "${puzzle} completed")
    }


}