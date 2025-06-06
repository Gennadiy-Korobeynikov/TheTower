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
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.LoadManager
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.databinding.FragmentLvl3PuzzleKeyBinding
import com.tpu.thetower.puzzles.Lvl3PuzzleKey
import kotlin.math.roundToInt

data class KeyPin(
    val initialPosition: Float,
    var currentPosition: Float,
    val minPosition: Float,
    val maxPosition: Float
)


class Lvl3PuzzleKeyFragment : Fragment(R.layout.fragment_lvl3_puzzle_key), Hintable {

    private lateinit var binding: FragmentLvl3PuzzleKeyBinding

    private lateinit var keyView: KeyView
    private lateinit var tvKeyPosition: TextView
    private lateinit var main: ConstraintLayout
    private lateinit var ivLock: ImageView
    private lateinit var btnCopy: Button

    private lateinit var hintManager: HintManager
    private lateinit var saveManager: SaveManager

    private lateinit var puzzle: Puzzle
    private var solution = listOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lvl3_puzzle_key, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl3PuzzleKeyBinding.bind(view)

        keyView = binding.keyView
        tvKeyPosition = binding.tvKeyPosition
        main = binding.main
        ivLock = binding.ivLock
        btnCopy = binding.btnCopy

        saveManager = SaveManager.getInstance()

        if (LoadManager.getPuzzleStatus(requireActivity(), 3, "vacuum cleaner") == "completed") {
            ivLock.layoutParams = RelativeLayout.LayoutParams(
                200,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            keyView.visibility = View.VISIBLE
        }
        puzzle = Lvl3PuzzleKey(3, "key")

        keyView.setOnKeyPinsChangedListener { pins ->
            val positionsText = pins.joinToString(", ") { "${it.currentPosition.roundToInt()}" }
            solution = pins.map {it.currentPosition.toInt()}
            tvKeyPosition.text = positionsText
            if (puzzle.checkSolution(requireActivity(), solution.joinToString(""))) {
                main.animate()
                    .alpha(0.2f)
                    .setDuration(2500)
                    .withEndAction {
                        FragmentManager.goBack(this)
                    }
                    .start()
            }
        }

        btnCopy.setOnClickListener {
            saveManager.savePuzzleData(requireContext(), 3, "lock model", status = "in_progress")

        }

        if (LoadManager.isPuzzleCompleted(requireActivity(), 3, "lock model")) // Замок вставлен в комп
            hintManager = HintManager(
                listOf("lvl3_puzzle4_hint3"),
                LoadManager.getPuzzleUsedHintsCount(requireActivity(), 3, "key"),
                3, "key"
            )
        else
            hintManager = HintManager(
                listOf("lvl3_puzzle4_hint1", "lvl3_puzzle4_hint2", "lvl3_puzzle4_hint3"),
                LoadManager.getPuzzleUsedHintsCount(requireActivity(), 3, "key"),
                3, "key"
            )
    }

    override fun useHint() {
        hintManager.useHint(requireActivity())
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

    private val borderPinPaint = Paint().apply {
        color = Color.parseColor("#808080")
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

    private var keyBaseWidth = 40f
    private var keyBaseHeight = 120f
    private var keyPinWidth = 20f

    private val positionStep = 50f

    private val pins = mutableListOf(
        KeyPin(initialPosition = 0f, currentPosition = 50f, minPosition = 50f, maxPosition = 500f),
        KeyPin(initialPosition = 0f, currentPosition = 50f, minPosition = 50f, maxPosition = 500f),
        KeyPin(initialPosition = 0f, currentPosition = 50f, minPosition = 50f, maxPosition = 550f),
        KeyPin(initialPosition = 0f, currentPosition = 50f, minPosition = 50f, maxPosition = 500f),
        KeyPin(initialPosition = 0f, currentPosition = 50f, minPosition = 50f, maxPosition = 500f)
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
            val pinXStart = keyBaseX + keyBaseWidth

            canvas.drawRect(
                pinXStart,
                pinY,
                pinXStart + pin.currentPosition,
                pinY + pinHeight,
                pinPaint
            )

            val numberOfSteps = (pin.currentPosition / positionStep).toInt()
            for (i in 1..numberOfSteps) {
                val lineX = pinXStart + i * positionStep
                canvas.drawLine(
                    lineX,
                    pinY,
                    lineX,
                    pinY + pinHeight,
                    borderPinPaint
                )
            }

            canvas.drawRect(
                pinXStart,
                pinY,
                pinXStart + pin.currentPosition,
                pinY + pinHeight,
                borderPinPaint
            )

            if (activePin == index) {
                canvas.drawRect(
                    pinXStart,
                    pinY,
                    pinXStart + pin.currentPosition,
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