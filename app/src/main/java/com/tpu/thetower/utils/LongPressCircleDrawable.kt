package com.tpu.thetower.utils

import android.graphics.Canvas
import android.graphics.Color.WHITE
import android.graphics.Color.argb
import android.graphics.Color.blue
import android.graphics.Color.green
import android.graphics.Color.red
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Drawable

class LongPressCircleDrawable(
    private val color: Int = WHITE,
    private val strokeWidthPx: Float = 6f
) : Drawable() {

    var progress: Float = 0f
        set(value) { field = value.coerceIn(0f, 1f); invalidateSelf() }

    var isActive: Boolean = false
        set(value) { field = value; invalidateSelf() }

    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = strokeWidthPx
        this.color = this@LongPressCircleDrawable.color
        strokeCap = Paint.Cap.ROUND
    }

    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = strokeWidthPx
        color = argb(50, red(color), green(color), blue(color))
    }

    private val oval = RectF()

    override fun draw(canvas: Canvas) {
        if (!isActive) return

        val b = bounds
        val r = minOf(b.width(), b.height()) / 2f - strokeWidthPx
        val cx = b.exactCenterX()
        val cy = b.exactCenterY()
        oval.set(cx - r, cy - r, cx + r, cy + r)

        // Полупрозрачный трек — сразу показывает куда заполнится
        canvas.drawArc(oval, -90f, 360f, false, trackPaint)

        // Заполняющаяся дуга
        if (progress > 0f) {
            canvas.drawArc(oval, -90f, 360f * progress, false, arcPaint)
        }
    }

    override fun setAlpha(alpha: Int) { arcPaint.alpha = alpha; invalidateSelf() }
    override fun setColorFilter(cf: ColorFilter?) { arcPaint.colorFilter = cf; invalidateSelf() }
    @Deprecated("Deprecated") override fun getOpacity() = PixelFormat.TRANSLUCENT
}