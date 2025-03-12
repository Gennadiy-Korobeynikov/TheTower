package com.tpu.thetower.fragments

import android.annotation.SuppressLint
import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.DragEvent.ACTION_DROP
import android.view.MotionEvent
import android.view.View
import android.view.View.DragShadowBuilder
import androidx.fragment.app.Fragment
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvlTestBinding

class LvlTestFragment : Fragment(R.layout.fragment_lvl_test),
    View.OnTouchListener,
    View.OnDragListener {

    private lateinit var binding: FragmentLvlTestBinding
    private var originalX = 0f
    private var originalY = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLvlTestBinding.bind(view)

        with(binding) {
            ivDraggable.setOnTouchListener(this@LvlTestFragment)
            ivTarget.setOnDragListener(this@LvlTestFragment)

            // Сохраняем начальные координаты только один раз
            ivDraggable.post {
                originalX = ivDraggable.x
                originalY = ivDraggable.y
            }
        }
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        return if (event?.action == MotionEvent.ACTION_DOWN) {
            view?.visibility = View.INVISIBLE // Скрываем оригинал при начале перетаскивания
            val data = ClipData.newPlainText("", "")
            val shadowBuilder = DragShadowBuilder(view)
            view?.startDragAndDrop(data, shadowBuilder, view, 0)
            true
        } else {
            false
        }
    }

    override fun onDrag(targetView: View?, event: DragEvent?): Boolean {
        when (event?.action) {
            DragEvent.ACTION_DRAG_ENDED -> {
                if (!event.result) {
                    // Возврат на место только при неудаче
                    binding.ivDraggable.apply {
                        x = originalX
                        y = originalY
                        visibility = View.VISIBLE
                    }
                }
            }
            ACTION_DROP -> {
                if (isInsideTarget(event)) {
                    binding.ivDraggable.apply {
                        x = binding.ivTarget.x + (binding.ivTarget.width - width) / 2
                        y = binding.ivTarget.y + (binding.ivTarget.height - height) / 2
                        visibility = View.VISIBLE
                    }
                }
            }
        }
        return true
    }

    private fun isInsideTarget(event: DragEvent): Boolean {
        val target = binding.ivTarget
        val (x, y) = event.x to event.y

        return x in 0f..target.width.toFloat() &&
                y in 0f..target.height.toFloat()
    }
}