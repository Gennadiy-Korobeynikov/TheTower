package com.tpu.thetower.models

import com.google.gson.JsonObject

data class LevelData(
    val id: Int,
    val name: String,
    var isCompleted: Boolean,
    val puzzles: List<PuzzleData>,
    val dialogs: List<DialogData>,
    var extraState: JsonObject? = null
)

data class PuzzleData(
    val id: Int,
    val name: String,
    var status: String,
    var attempts: Int,
    var timeSpent: Int,
    var hintsUsed: Int
)

data class DialogData(
    val dialogKey: String,
    var currentDialogIndex: Int
)
