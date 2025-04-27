package com.tpu.thetower.utils

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tpu.thetower.Puzzle
import com.tpu.thetower.SoundManager
import com.tpu.thetower.adapters.ImageCodeAdapter

object WheelSetupHelper {

    interface WheelSolvedListener {
        fun onPuzzleSolved()
    }

    fun setupWheel(
        rv: RecyclerView,
        data: Array<Int>,
        rvIndex: Int,
        solution: CharArray,
        context: Context,
        puzzle: Puzzle,
        soundManager: SoundManager?,
        rotationSoundResId: Int?,
        isSolvedRef: () -> Boolean,
        onSolvedListener: WheelSolvedListener,
    ) {
        val adapter = ImageCodeAdapter(data)
        rv.adapter = adapter

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv.layoutManager = layoutManager

        val snapHelper = LimitedSpeedLinearSnapHelper()
        snapHelper.attachToRecyclerView(rv)

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var lastPosition = -1
            override fun onScrollStateChanged(rv: RecyclerView, newState: Int) {
                super.onScrollStateChanged(rv, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val centerView = snapHelper.findSnapView(layoutManager) ?: return
                    val position = layoutManager.getPosition(centerView)
                    if (position != RecyclerView.NO_POSITION && position != lastPosition) {
                        lastPosition = position
                        if (rotationSoundResId != null) {
                            soundManager?.playSound(rotationSoundResId)
                        }
                        val digit = position % data.size
                        solution[rvIndex] = digit.digitToChar()

                        val isCorrectSolution = puzzle.checkSolution(context, String(solution))

                        if (isCorrectSolution && !isSolvedRef()) {
                            onSolvedListener.onPuzzleSolved()
                        }
                    }
                }
            }
        })

        rv.post {
            val targetDigit = Character.getNumericValue(solution[rvIndex])
            val startPosition = Int.MAX_VALUE / 2 + targetDigit - (Int.MAX_VALUE / 2) % data.size
            layoutManager.scrollToPosition(startPosition)
        }
    }
}