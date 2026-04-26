package com.tpu.thetower.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.createBitmap
import com.tpu.thetower.viewmodels.BlurViewModel
import jp.wasabeef.blurry.Blurry

object BlurUtils {

    private const val DEFAULT_DARKNESS = 0.6f

    fun blurBitmap(context: Context, source: Bitmap, radius: Float = 20f): Bitmap {
        val r = radius.coerceAtLeast(0f)
        if (r == 0f) return source

        val iv = ImageView(context).apply {
            scaleType = ImageView.ScaleType.FIT_XY
            setImageBitmap(source)
            measure(
                View.MeasureSpec.makeMeasureSpec(source.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(source.height, View.MeasureSpec.EXACTLY),
            )
            layout(0, 0, measuredWidth, measuredHeight)
        }

        val out = Blurry.with(context)
            .radius(r.toInt().coerceIn(1, 25))
            .sampling(1)
            .capture(iv)
            .get()

        return out
    }

    fun darkenBitmap(source: Bitmap, darkness: Float = DEFAULT_DARKNESS): Bitmap {
        val d = darkness.coerceIn(0f, 1f)
        if (d == 0f) return source

        val canvas = Canvas(source)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            alpha = (255 * d).toInt().coerceIn(0, 255)
        }
        canvas.drawRect(0f, 0f, source.width.toFloat(), source.height.toFloat(), paint)
        return source
    }

    fun blurAndDarkenBitmap(
        context: Context,
        source: Bitmap,
        radius: Float = 20f,
    ): Bitmap {
        val blurred = blurBitmap(context, source, radius)
        // Blurry возвращает новый bitmap, его можно безопасно модифицировать in-place
        return darkenBitmap(blurred, DEFAULT_DARKNESS)
    }

    fun captureSnapshot(view: View): Bitmap {
        val w = view.width
        val h = view.height
        require(w > 0 && h > 0) { "View is not laid out yet (width=$w, height=$h)" }

        val bitmap = createBitmap(w, h)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}

fun getOrCreateBlur(
    blurVM: BlurViewModel,
    blurKey: String,
    sourceBitmap: Bitmap,
    radius: Float = 20f,
    context: Context,
): Bitmap {
    blurVM.getBlur(blurKey)?.let { return it }

    val blurredDark = BlurUtils.blurAndDarkenBitmap(context, sourceBitmap, radius)
    blurVM.setBlur(blurKey, blurredDark)
    return blurredDark
}
