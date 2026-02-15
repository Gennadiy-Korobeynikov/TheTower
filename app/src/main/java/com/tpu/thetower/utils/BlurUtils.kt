package com.tpu.thetower.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.createBitmap
import com.tpu.thetower.viewmodels.BlurViewModel
import jp.wasabeef.blurry.Blurry

object BlurUtils {

    private const val TAG = "BlurUtils"

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

    val blurred = BlurUtils.blurBitmap(context, sourceBitmap, radius)
    blurVM.setBlur(blurKey, blurred)
    return blurred
}
