package com.tpu.thetower

// Заготовка под класс головоломок
abstract class Puzzle(val name : String) {

    abstract  var isSolved : Boolean
    abstract var usedHintsCount : Int
//    abstract var hints : Array<Dialog>

    abstract fun checkSolution(solution : String)
    fun complete() {
        isSolved = true
    }

//    fun getNextHint() {
//        FragmentManager.showDialog(hints[usedHintsCount])
//    }

}