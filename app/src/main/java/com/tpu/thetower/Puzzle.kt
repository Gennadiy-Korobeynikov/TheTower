package com.tpu.thetower

// Заготовка под класс головоломок
abstract class Puzzle(val name : String) {

    var isSolved : Boolean = false
    var usedHintsCount : Int = 0
//    abstract var hints : Array<Dialog>

    abstract fun checkSolution(solution : String) : Boolean
    fun complete() {
        isSolved = true
    }

//    fun getNextHint() {
//        FragmentManager.showDialog(hints[usedHintsCount])
//    }

}