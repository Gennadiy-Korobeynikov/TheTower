package com.tpu.thetower.models

data class PlayerInfo(
    val uid: String,
    val totalPlayTime: Int,
    val lastUnlockedModule: Int,
    val currentLevel: Int
)
