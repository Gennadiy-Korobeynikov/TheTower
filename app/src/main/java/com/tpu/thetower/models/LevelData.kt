package com.tpu.thetower.models

data class LevelData(
    val id: Int,
    val name: String,
    var isCompleted: Boolean,
    var puzzles: List<PuzzleData>
)

data class PuzzleData(
    val id: Int,
    val name: String,
    var status: String,
    var attempts: Int,
    var timeSpent: Int,
    var hintsUsed: Int
)
