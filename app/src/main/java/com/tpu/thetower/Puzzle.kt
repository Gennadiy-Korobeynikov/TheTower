package com.tpu.thetower

import android.content.Context
import android.util.Log

// Заготовка под класс головоломок
abstract class Puzzle(val name: String) {

    var isSolved: Boolean = false
    var usedHintsCount: Int = 0

    private lateinit var saveManager: SaveManager

    abstract fun checkSolution(context: Context, solution : String = ""): Boolean

    fun complete(context: Context) {
        isSolved = true
        saveManager = SaveManager.getInstance()
        val (level_id, puzzle_id) = name.filter { it.isDigit() }.map { it.toString().toInt() }
        saveManager.savePuzzleData(context, level_id, puzzle_id)
        Log.i("Puzzle", "${name} completed")
    }


}