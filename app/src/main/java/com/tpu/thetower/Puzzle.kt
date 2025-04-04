package com.tpu.thetower

import android.content.Context

// Заготовка под класс головоломок
abstract class Puzzle(val name: String) {

    var isSolved: Boolean = false
    var usedHintsCount: Int = 0

    private lateinit var saveManager: SaveManager

    abstract fun checkSolution(context: Context, solution: String): Boolean
    fun complete(name: String, context: Context) {
        isSolved = true
        saveManager = SaveManager.getInstance()
        val (level_id, puzzle_id) = name.filter { it.isDigit() }.map { it.toString().toInt() }
        saveManager.savePuzzleData(context, level_id, puzzle_id)

    }

//    fun getNextHint() {
//        FragmentManager.showDialog(hints[usedHintsCount])
//    }

}