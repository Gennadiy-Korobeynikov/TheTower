package com.tpu.thetower.utils

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tpu.thetower.Puzzle
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.adapters.ImageCodeAdapter
import com.tpu.thetower.managers.SaveRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

object WheelSetupHelper {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WheelEntryPoint {
        fun saveRepository(): SaveRepository
    }

    private fun saveRepo(activity: Activity): SaveRepository {
        return EntryPointAccessors.fromApplication(
            activity.applicationContext,
            WheelEntryPoint::class.java
        ).saveRepository()
    }

    interface WheelSolvedListener {
        fun onPuzzleSolved()
    }

    fun setupWheel(
        rv: RecyclerView,
        data: Array<Int>,
        layoutImage: Int,
        orientation: Int,
        rvIndex: Int,
        solution: CharArray,
        activity: Activity,
        puzzle: Puzzle,
        soundManager: SoundManager?,
        soundEffect: SoundEffect?,
        isSolvedRef: () -> Boolean,
        onSolvedListener: WheelSolvedListener,
    ) {
        val adapter = ImageCodeAdapter(data, layoutImage)
        rv.adapter = adapter

        val layoutManager = LinearLayoutManager(activity, orientation, false)
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
                        if (soundEffect != null) {
                            soundManager?.playSound(soundEffect)
                        }
                        val digit = position % data.size
                        solution[rvIndex] = digit.digitToChar()

                        val isCorrectSolution = puzzle.checkSolution(activity, saveRepo(activity), String(solution))

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