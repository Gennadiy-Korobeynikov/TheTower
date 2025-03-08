package com.tpu.thetower

// Заготовка под класс головоломок
abstract class Puzzle(val name : String) {

    var isSolved : Boolean = false

    abstract fun checkSolution(solution : String) : Boolean
    fun complete() {
        isSolved = true
    }

}