package com.tpu.thetower.models

data class Level(
    val id: Int,
    val name: String,
    val isUnlocked: Boolean,
    val isCompleted: Boolean,
    val puzzles: List<Puzzle>
)

data class Puzzle(
    val id: String,
    val name: String,
    val status: String,
    val attempts: Int,
    val timeSpent: Int,
    val hintsUsed: Int
)
