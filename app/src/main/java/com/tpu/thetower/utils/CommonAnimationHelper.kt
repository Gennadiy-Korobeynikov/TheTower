package com.tpu.thetower.utils

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.UiVisibilityController

object CommonAnimationHelper {

    @SuppressLint("ClickableViewAccessibility")
    fun animatePuzzleCompletion(
        fragment: Fragment,
        mainScreen: View,
        fragmentRoot: ViewGroup,
        durationMs: Long = 2000L,
        targetAlpha: Float = 0.2f,
    ) {
        UiVisibilityController.hide(
            fragment.requireActivity(),
            UiVisibilityController.UiContainer.GO_BACK_ARROW
        )

        // Отключаем взаимодействие с фрагментом во время анимации
        val blocker = View(fragment.requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                fragmentRoot.width,
                fragmentRoot.height
            )
            isClickable = true
            isFocusable = true
            setOnTouchListener { _, _ -> true }
        }

        fragmentRoot.addView(blocker)

        mainScreen.animate()
            .alpha(targetAlpha)
            .setDuration(durationMs)
            .withEndAction {
                fragmentRoot.removeView(blocker)
                FragmentNavigation.goBack(fragment)
            }
            .start()
    }

}