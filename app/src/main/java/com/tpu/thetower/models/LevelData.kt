package com.tpu.thetower.models

data class LevelData(
    val id: Int,
    val name: String,
    val isUnlocked: Boolean,
    val isCompleted: Boolean,
    val puzzles: List<PuzzleData>
)

data class PuzzleData(
    val id: String,
    val name: String,
    val status: String,
    val attempts: Int,
    val timeSpent: Int,
    var hintsUsed: Int
)
