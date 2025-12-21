package com.tpu.thetower.models

data class SaveData(
    val playerInfo: PlayerInfo,
    val levels: List<LevelData>,
    val gameSettings: GameSettings
)