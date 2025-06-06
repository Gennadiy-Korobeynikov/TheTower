package com.tpu.thetower.puzzles

import android.app.Activity
import android.content.Context
import com.tpu.thetower.Puzzle



class Lvl3PuzzleHoover(level: Int, puzzle: String) : Puzzle(level, puzzle) {

    // Координаты свободных точек
    private val isCorrectCell : Array<BooleanArray> = arrayOf(
        BooleanArray(15) { it in listOf(2, 3, 4, 6) },
        BooleanArray(15) { it in listOf(4, 6, 10) },
        BooleanArray(15) { it in listOf(4, 5, 6, 10, 11, 12, 13, 14) },
        BooleanArray(15) { it in listOf(4, 12) },
        BooleanArray(15) { it in listOf(4, 5, 6, 7, 8, 9, 10, 11, 12) },
        BooleanArray(15) { it in listOf(4, 8, 12) },
        BooleanArray(15) { it in listOf(2, 3, 4, 8, 9, 10, 12) },
        BooleanArray(15) { it in listOf(10, 12) },
        BooleanArray(15) { it in listOf(4, 5, 6, 10, 12, 13, 14) },
        BooleanArray(15) { it in listOf(4, 10) },
        BooleanArray(15) { it in listOf(4, 10) },
        BooleanArray(15) { it in listOf(2, 4, 10) },
        BooleanArray(15) { it in listOf(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14) },
        BooleanArray(15) { it in listOf(6) },
        BooleanArray(15) { it in listOf(6) }
    )
    private val fieldSize = 15

    private val directions =  arrayOf(
        Direction.Up,
        Direction.Right,
        Direction.Down,
        Direction.Left,
    )

    private val winPositionX = 6
    private val winPositionY = 0



    private var dirIndex = 0
    var currPositionX = 6
    var currPositionY = 14
    var currDirection : Direction = Direction.Up



    private fun setInitValues() {
        currPositionX = 6
        currPositionY = 14
        currDirection = Direction.Up
        dirIndex = 0
    }


    fun changeDirection( clockwise : Boolean ) {
        dirIndex = if (clockwise) dirIndex + 1  else dirIndex - 1
        dirIndex = (dirIndex % 4 + 4) % 4 // для зацикливания
        currDirection = directions[dirIndex]
    }

    fun moveForward() : Boolean{
        if ( currPositionY + currDirection.dy < fieldSize
            && currPositionX + currDirection.dx < fieldSize
            && isCorrectCell[currPositionY + currDirection.dy][currPositionX + currDirection.dx])
        {
            currPositionX += currDirection.dx * 2
            currPositionY += currDirection.dy * 2
            return true
        }
        else {
            setInitValues()
            return false
        }
    }

//    private  fun isWall() : Boolean{
//        if (!isCorrectCell[currPositionY][currPositionX]) {
//            setInitValues()
//            return true
//        }
//        return false
//    }

    override fun checkSolution(activity: Activity, solution: String): Boolean {
        if (currPositionX == winPositionX && currPositionY == winPositionY) {
             // Пока не работает
            super.complete(activity)
            return true
        }
        return false
    }


}


sealed class Direction(val dx: Int, val dy: Int) {
    data object Left : Direction(-1, 0)
    data object Right : Direction(1, 0)
    data object Up : Direction(0, -1)
    data object Down : Direction(0, 1)
}