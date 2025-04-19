package com.tpu.thetower.utils

import androidx.recyclerview.widget.LinearSnapHelper

class LimitedSpeedLinearSnapHelper(
    private val maxFlingVelocity: Int = 3000
) : LinearSnapHelper() {


    override fun onFling(velocityX: Int, velocityY: Int): Boolean {
        val limitedVelocityX = velocityX.coerceIn(-maxFlingVelocity, maxFlingVelocity)
        val limitedVelocityY = velocityY.coerceIn(-maxFlingVelocity, maxFlingVelocity)
        super.onFling(limitedVelocityX, limitedVelocityY)
        return true
    }
}