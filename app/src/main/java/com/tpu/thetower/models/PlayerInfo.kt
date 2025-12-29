package com.tpu.thetower.models

data class PlayerInfo(
    var uid: String,
    var totalPlayTime: Int,
    var accessLevel: Int,
    var currentLevel: Int,
    var accessCardNumber: Int
)
