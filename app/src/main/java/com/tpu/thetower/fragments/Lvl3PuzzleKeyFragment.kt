package com.tpu.thetower.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.LoadManager
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.databinding.FragmentLvl3PuzzleKeyBinding
import com.tpu.thetower.puzzles.Lvl3PuzzleKey

data class KeyPin(
    val initialPosition: Float,
    var currentPosition: Float,
    val minPosition: Float,
    val maxPosition: Float
)

class Lvl3PuzzleKeyFragment : Fragment(R.layout.fragment_lvl3_puzzle_key), Hintable {

    private lateinit var binding: FragmentLvl3PuzzleKeyBinding

    private lateinit var keyView: KeyView
    private lateinit var btnLockToCopy: Button
    private lateinit var btnCopy: Button
    private lateinit var btnApply: Button

    private lateinit var ivBg: ImageView

    private lateinit var hintManager: HintManager
    private lateinit var saveManager: SaveManager

    private lateinit var puzzle: Puzzle
    var longPressRunnable: Runnable? = null
    var isLongPressHandled = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl3PuzzleKeyBinding.bind(view)

        bindView()
        setListeners()

        saveManager = SaveManager.getInstance()
        puzzle = Lvl3PuzzleKey(3, "key")

        FragmentManager.showGoBackArrow(requireActivity())


        if (LoadManager.isPuzzleCompleted(requireActivity(), 3, "lock model")) { // Замок вставлен в комп
            hintManager = HintManager(
                listOf("lvl3_puzzle4_hint3"),
                LoadManager.getPuzzleUsedHintsCount(requireActivity(), 3, "key"),
                3, "key"
            )

        }
        else
            hintManager = HintManager(
                listOf("lvl3_puzzle4_hint1", "lvl3_puzzle4_hint2", "lvl3_puzzle4_hint3"),
                LoadManager.getPuzzleUsedHintsCount(requireActivity(), 3, "key"),
                3, "key"
            )
    }

    private fun bindView() {
        btnLockToCopy = binding.btnLockToCopy
        btnCopy = binding.btnCopy
        btnApply = binding.btnApply
        ivBg = binding.ivBg
        keyView = binding.keyView
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {

        btnCopy.setOnClickListener {
            saveManager.savePuzzleData(requireContext(), 3, "lock model", status = "in_progress")
        }

        btnCopy.setOnClickListener {
            saveManager.savePuzzleData(requireContext(), 3, "lock model", status = "in_progress")
            btnCopy.visibility = View.GONE
            ivBg.setImageResource(R.drawable.lvl3_lock)

            val snackBar = Snackbar.make(
                ivBg,
                getString(R.string.lvl3_copy),
                Toast.LENGTH_SHORT
            )
            snackBar.show()
        }

        btnLockToCopy.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    isLongPressHandled = false
                    longPressRunnable = Runnable {
                        ivBg.setImageResource(R.drawable.lvl3_lock_copy)
                        btnLockToCopy.visibility = View.GONE
                        btnCopy.visibility = View.VISIBLE
                        isLongPressHandled = true
                    }
                    view.postDelayed(longPressRunnable, 1000)
                    true
                }

                MotionEvent.ACTION_UP -> {
                    longPressRunnable?.let { view.removeCallbacks(it) }
                    true
                }

                MotionEvent.ACTION_CANCEL -> {
                    longPressRunnable?.let { view.removeCallbacks(it) }
                    true
                }

                else -> false
            }
        }

        btnApply.setOnClickListener {
            val pinsPositions = keyView.getPinsPositions()
            if (puzzle.checkSolution(requireActivity(), pinsPositions.joinToString(""))) {
                ivBg.animate()
                    .alpha(0.2f)
                    .setDuration(2500)
                    .withEndAction {
                        FragmentManager.goBack(this)
                    }
                    .start()
            }
        }

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

    private val pinPaint = Paint().apply {
        color = Color.parseColor("#996b2f") // Темно-серый цвет
        style = Paint.Style.FILL
    }

    private val borderPinPaint = Paint().apply {
        color = Color.parseColor("#000000")
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

    private var keyBaseWidth = 0f
    private var keyBaseHeight = 0f
    private var keyPinHeight = 20f

    private val positionStep = 50f

    private val pins = mutableListOf(
        KeyPin(initialPosition = 50f, currentPosition = 50f, minPosition = 50f, maxPosition = 150f),
        KeyPin(initialPosition = 50f, currentPosition = 50f, minPosition = 50f, maxPosition = 150f),
        KeyPin(initialPosition = 50f, currentPosition = 50f, minPosition = 50f, maxPosition = 150f),
        KeyPin(initialPosition = 50f, currentPosition = 50f, minPosition = 50f, maxPosition = 150f),
        KeyPin(initialPosition = 50f, currentPosition = 50f, minPosition = 50f, maxPosition = 150f)
    )

    // Возможные позиции для зубчиков
    private val validPositions = listOf(50f, 100f, 150f)

    private var activePin: Int? = null
    private var lastTouchY = 0f
    private var accumulatedDelta = 0f

    private var onKeyPinsChangedListener: ((List<KeyPin>) -> Unit)? = null

    fun setOnKeyPinsChangedListener(listener: (List<KeyPin>) -> Unit) {
        onKeyPinsChangedListener = listener
        listener(pins)
    }

    fun getPinsPositions(): List<Int> {
        return pins.map { it.currentPosition.toInt() }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        keyBaseWidth = w * 1f
        keyBaseHeight = h * 0f
        keyPinHeight = h * 0.1f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val keyBaseX = width / 2 - keyBaseWidth / 2
        val keyBaseY = 0f

        pins.forEachIndexed { index, pin ->
            val pinX = keyBaseX + index * (keyBaseWidth / pins.size)
            val pinWidth = keyBaseWidth / pins.size
            val pinYStart = keyBaseY + keyBaseHeight

            canvas.drawRect(
                pinX,
                pinYStart,
                pinX + pinWidth,
                pinYStart + pin.currentPosition,
                pinPaint
            )

            // Граница зубца
            canvas.drawRect(
                pinX,
                pinYStart,
                pinX + pinWidth,
                pinYStart + pin.currentPosition,
                borderPinPaint
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val keyBaseX = width / 2 - keyBaseWidth / 2
                val keyBaseY = 0f

                activePin = null
                accumulatedDelta = 0f

                pins.forEachIndexed { index, _ ->
                    val pinX = keyBaseX + index * (keyBaseWidth / pins.size)
                    val pinWidth = keyBaseWidth / pins.size
                    val pinY = keyBaseY + keyBaseHeight

                    val pinRect = RectF(
                        pinX,
                        pinY,
                        pinX + pinWidth,
                        pinY + pins[index].maxPosition
                    )

                    if (pinRect.contains(x, y)) {
                        activePin = index
                        lastTouchY = y
                        invalidate()
                        return true
                    }
                }
                return activePin != null
            }

            MotionEvent.ACTION_MOVE -> {
                activePin?.let { index ->
                    val deltaY = y - lastTouchY
                    lastTouchY = y

                    accumulatedDelta += deltaY

                    if (Math.abs(accumulatedDelta) >= positionStep) {
                        val pin = pins[index]
                        val currentPosIndex = validPositions.indexOf(pin.currentPosition)

                        val newIndex = if (accumulatedDelta > 0) {
                            // Движение вниз - увеличиваем индекс
                            (currentPosIndex + 1).coerceAtMost(validPositions.size - 1)
                        } else {
                            // Движение вверх - уменьшаем индекс
                            (currentPosIndex - 1).coerceAtLeast(0)
                        }

                        if (newIndex != currentPosIndex) {
                            pins[index] = pin.copy(currentPosition = validPositions[newIndex])
                            onKeyPinsChangedListener?.invoke(pins)
                            invalidate()
                        }

                        accumulatedDelta = 0f
                    }
                    return true
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (activePin != null) {
                    activePin?.let { index ->
                        val pin = pins[index]
                        val currentPosIndex = validPositions.indexOf(pin.currentPosition)

                        if (Math.abs(accumulatedDelta) >= positionStep / 2) {
                            val newIndex = if (accumulatedDelta > 0) {
                                // Движение вниз - увеличиваем индекс
                                (currentPosIndex + 1).coerceAtMost(validPositions.size - 1)
                            } else {
                                // Движение вверх - уменьшаем индекс
                                (currentPosIndex - 1).coerceAtLeast(0)
                            }

                            if (newIndex != currentPosIndex) {
                                pins[index] = pin.copy(currentPosition = validPositions[newIndex])
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
}