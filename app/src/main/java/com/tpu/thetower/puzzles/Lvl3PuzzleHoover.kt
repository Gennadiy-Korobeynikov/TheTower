package com.tpu.thetower.puzzles

import android.content.Context
import com.tpu.thetower.Puzzle

class Lvl3PuzzleHoover(level: Int, puzzle: String) : Puzzle(level, puzzle) {

    private val isCorrectCell  = arrayOf(
        arrayOf(false,false,false,false,false),
        arrayOf(false,true,true,false,false),
        arrayOf(false,false,true,true,false),
        arrayOf(false,false,false,true,false),
        arrayOf(false,false,false,false,false),
    )

    private val directions =  arrayOf(
        Direction.Right,
        Direction.Down,
        Direction.Left,
        Direction.Up
    )

    private val winPositionX = 3
    private val winPositionY = 3

// для теста public - потом раскомментить

    /*private*/ var dirIndex = 0
    /*private*/ var currPositionX = 1
    /*private*/ var currPositionY = 1
    /*private*/ var currDirection : Direction = Direction.Right



    private fun setInitValues() {
        currPositionX = 1
        currPositionY = 1
        currDirection = Direction.Right
        dirIndex = 0
    }


    fun changeDirection( clockwise : Boolean ) {
        dirIndex = if (clockwise) dirIndex + 1  else dirIndex - 1
        dirIndex = (dirIndex % 4 + 4) % 4 // для зацикливания
        currDirection = directions[dirIndex]
    }

    fun moveForward() {
        currPositionX += currDirection.dx
        currPositionY += currDirection.dy
    }

    fun isWall() : Boolean{
        if (!isCorrectCell[currPositionY][currPositionX]) {
            setInitValues()
            return true
        }
        return false
    }

    override fun checkSolution(context: Context, solution: String): Boolean {
        if (currPositionX == winPositionX && currPositionY == winPositionY) {
             // Пока не работает
            //super.complete(context)
            return true
        }
        return false
    }


}


sealed class Direction(val dx: Int, val dy: Int) {
    object Left : Direction(-1, 0)
    object Right : Direction(1, 0)
    object Up : Direction(0, -1)
    object Down : Direction(0, 1)
}