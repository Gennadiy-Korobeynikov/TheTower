package com.tpu.thetower.fragments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tpu.thetower.R
import kotlin.math.roundToInt

data class KeyPin(
    val initialPosition: Float,
    var currentPosition: Float,
    val minPosition: Float,
    val maxPosition: Float
)

class KeyFragment : Fragment(R.layout.fragment_key) {

    private lateinit var keyView: KeyView
    private lateinit var tvKeyPosition: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_key, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        keyView = view.findViewById(R.id.keyView)
        tvKeyPosition = view.findViewById(R.id.tvKeyPosition)
        val btnReset = view.findViewById<Button>(R.id.btnReset)

        keyView.setOnKeyPinsChangedListener { pins ->
            val positionsText = pins.joinToString(", ") { "${it.currentPosition.roundToInt()}" }
            tvKeyPosition.text = positionsText
        }

        btnReset.setOnClickListener {
            keyView.resetPins()
        }
    }
}

// Пользовательский View для отображения и взаимодействия с ключом
class KeyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val keyBasePaint = Paint().apply {
        color = Color.parseColor("#808080") // Серый цвет
        style = Paint.Style.FILL
    }

    private val pinPaint = Paint().apply {
        color = Color.parseColor("#404040") // Темно-серый цвет
        style = Paint.Style.FILL
    }

    private val activePinPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    private var keyBaseWidth = 40f
    private var keyBaseHeight = 120f
    private var keyPinWidth = 20f

    private val positionStep = 50f

    private val pins = mutableListOf(
        KeyPin(initialPosition = 20f, currentPosition = 50f, minPosition = 50f, maxPosition = 400f),
        KeyPin(initialPosition = 30f, currentPosition = 50f, minPosition = 50f, maxPosition = 500f),
        KeyPin(initialPosition = 15f, currentPosition = 50f, minPosition = 50f, maxPosition = 350f),
        KeyPin(initialPosition = 25f, currentPosition = 50f, minPosition = 50f, maxPosition = 450f),
        KeyPin(initialPosition = 35f, currentPosition = 50f, minPosition = 50f, maxPosition = 600f)
    )

    private var activePin: Int? = null
    private var lastTouchX = 0f
    private var accumulatedDelta = 0f

    private var onKeyPinsChangedListener: ((List<KeyPin>) -> Unit)? = null

    fun setOnKeyPinsChangedListener(listener: (List<KeyPin>) -> Unit) {
        onKeyPinsChangedListener = listener
        listener(pins)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        keyBaseWidth = w * 0.15f
        keyBaseHeight = h * 0.7f
        keyPinWidth = w * 0.1f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val keyBaseX = width / 2 - keyBaseWidth / 2
        val keyBaseY = height / 2 - keyBaseHeight / 2

        canvas.drawRect(
            keyBaseX,
            keyBaseY,
            keyBaseX + keyBaseWidth,
            keyBaseY + keyBaseHeight,
            keyBasePaint
        )

        pins.forEachIndexed { index, pin ->
            val pinY = keyBaseY + index * (keyBaseHeight / pins.size)
            val pinHeight = keyBaseHeight / pins.size

            canvas.drawRect(
                keyBaseX + keyBaseWidth,
                pinY,
                keyBaseX + keyBaseWidth + pin.currentPosition,
                pinY + pinHeight,
                pinPaint
            )

            if (activePin == index) {
                canvas.drawRect(
                    keyBaseX + keyBaseWidth,
                    pinY,
                    keyBaseX + keyBaseWidth + pin.currentPosition,
                    pinY + pinHeight,
                    activePinPaint
                )
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val keyBaseX = width / 2 - keyBaseWidth / 2
                val keyBaseY = height / 2 - keyBaseHeight / 2

                activePin = null
                accumulatedDelta = 0f

                pins.forEachIndexed { index, _ ->
                    val pinX = keyBaseX + keyBaseWidth
                    val pinY = keyBaseY + index * (keyBaseHeight / pins.size)
                    val pinHeight = keyBaseHeight / pins.size

                    val pinRect = RectF(
                        pinX,
                        pinY,
                        pinX + keyPinWidth * 3,
                        pinY + pinHeight
                    )

                    if (pinRect.contains(x, y)) {
                        activePin = index
                        lastTouchX = x
                        invalidate()
                        return true
                    }
                }
                return activePin != null
            }

            MotionEvent.ACTION_MOVE -> {
                activePin?.let { index ->
                    val deltaX = x - lastTouchX
                    lastTouchX = x

                    accumulatedDelta += deltaX * 0.5f

                    if (Math.abs(accumulatedDelta) >= positionStep) {
                        val steps = (accumulatedDelta / positionStep).toInt()
                        val stepDelta = steps * positionStep

                        val pin = pins[index]
                        val newPosition = pin.currentPosition + stepDelta

                        val roundedPosition =
                            (newPosition / positionStep).roundToInt() * positionStep

                        pins[index] = pin.copy(
                            currentPosition = roundedPosition.coerceIn(
                                pin.minPosition,
                                pin.maxPosition
                            )
                        )

                        accumulatedDelta -= stepDelta

                        onKeyPinsChangedListener?.invoke(pins)
                        invalidate()
                    }
                    return true
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (activePin != null) {
                    if (Math.abs(accumulatedDelta) > 0) {
                        activePin?.let { index ->
                            val pin = pins[index]
                            if (Math.abs(accumulatedDelta) >= positionStep / 2) {
                                val direction = if (accumulatedDelta > 0) 1 else -1
                                val newPosition = pin.currentPosition + direction * positionStep

                                pins[index] = pin.copy(
                                    currentPosition = newPosition.coerceIn(
                                        pin.minPosition,
                                        pin.maxPosition
                                    )
                                )

                                onKeyPinsChangedListener?.invoke(pins)
                                invalidate()
                            }
                        }
                    }

                    activePin = null
                    accumulatedDelta = 0f
                    invalidate()
                    return true
                }
            }
        }

        return super.onTouchEvent(event)
    }

    fun resetPins() {
        for (i in pins.indices) {
            pins[i] = pins[i].copy(currentPosition = pins[i].initialPosition)
        }
        onKeyPinsChangedListener?.invoke(pins)
        invalidate()
    }
}