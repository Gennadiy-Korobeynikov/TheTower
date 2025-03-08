package com.tpu.thetower.models

data class PlayerInfo(
    val uid: String,
    val totalPlayTime: Int,
    val unlockedLevels: Int,
    val currentLevel: Int
)
