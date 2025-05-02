package com.tpu.thetower.fragments

import android.annotation.SuppressLint
import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.DragShadowBuilder
import androidx.fragment.app.Fragment
import com.tpu.thetower.DialogManager
import com.tpu.thetower.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.LoadManager
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.databinding.FragmentLvlTestBinding

class LvlTestFragment : Fragment(R.layout.fragment_lvl_test),
    View.OnTouchListener,
    View.OnDragListener {

    private lateinit var saveManager: SaveManager


    private lateinit var binding: FragmentLvlTestBinding
    private val originalPositions = mutableMapOf<View, Pair<Float, Float>>()
    private val zoneOccupants = mutableMapOf<View, View?>()


//    private val puzzle: Puzzle = Lvl0Puzzle1("Lvl0Puzzle1")

    private var solution = charArrayOf('0', '0', '0', '0')

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLvlTestBinding.bind(view)


        with(binding) {

            val draggables = listOf(ivDraggable1, ivDraggable2, ivDraggable3, ivDraggable4)
            draggables.forEach { it.setOnTouchListener(this@LvlTestFragment) }

            val targets = listOf(ivTarget1, ivTarget2, ivTarget3, ivTarget4)
            targets.forEach {
                it.setOnDragListener(this@LvlTestFragment)
                zoneOccupants[it] = null
            }
            root.setOnDragListener(this@LvlTestFragment)

            draggables.forEach { draggable ->
                draggable.post {
                    originalPositions[draggable] = Pair(draggable.x, draggable.y)
                }
            }
        }
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        return if (event?.action == MotionEvent.ACTION_DOWN) {
            view?.visibility = View.INVISIBLE

            val data = ClipData.newPlainText("", "")
            val shadowBuilder = DragShadowBuilder(view)
            view?.startDragAndDrop(data, shadowBuilder, view, 0)
            true
        } else {
            false
        }
    }

    override fun onDrag(targetView: View?, event: DragEvent?): Boolean {
        val draggedView = event?.localState as? View ?: return false

        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED,
            DragEvent.ACTION_DRAG_ENTERED,
            DragEvent.ACTION_DRAG_LOCATION,
            DragEvent.ACTION_DRAG_EXITED -> return true

            DragEvent.ACTION_DROP -> {
                val previousZone =
                    zoneOccupants.entries.firstOrNull { it.value == draggedView }?.key

                if (targetView != null && targetView in zoneOccupants.keys) {
                    val currentOccupant = zoneOccupants[targetView]

                    if (currentOccupant != null) {
                        // Если в целевой зоне уже есть фигура - меняем их местами
                        if (previousZone != null) {
                            placeViewInZone(draggedView, targetView)
                            placeViewInZone(currentOccupant, previousZone)

                            zoneOccupants[previousZone] = currentOccupant
                            zoneOccupants[targetView] = draggedView
                        } else {
                            // Перемещение из исходной позиции в занятую зону
                            originalPositions[currentOccupant]?.let { pos ->
                                currentOccupant.x = pos.first
                                currentOccupant.y = pos.second
                                currentOccupant.visibility = View.VISIBLE
                            }
                            placeViewInZone(draggedView, targetView)
                            zoneOccupants[targetView] = draggedView
                        }
                    } else {
                        // Перемещение в пустую зону
                        previousZone?.let { zoneOccupants[it] = null }
                        placeViewInZone(draggedView, targetView)
                        zoneOccupants[targetView] = draggedView
                    }
                } else {
                    // Возврат в исходную позицию
                    previousZone?.let { zoneOccupants[it] = null }
                    originalPositions[draggedView]?.let { pos ->
                        draggedView.x = pos.first
                        draggedView.y = pos.second
                        draggedView.visibility = View.VISIBLE
                    }
                }
                return true
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                draggedView.visibility = View.VISIBLE
                if (!event.result) {
                    originalPositions[draggedView]?.let { pos ->
                        draggedView.x = pos.first
                        draggedView.y = pos.second
                        draggedView.visibility = View.VISIBLE
                    }
                }
                updateSolution()
//                puzzle.checkSolution(requireContext(), String(solution))
                return true
            }
        }
        return false
    }

        private fun placeViewInZone(view: View, zone: View) {
            view.apply {
                x = zone.x + (zone.width - width) / 2f
                y = zone.y + (zone.height - height) / 2f
                visibility = View.VISIBLE
            }
        }

    private fun updateSolution() {
        val targets =
            listOf(binding.ivTarget1, binding.ivTarget2, binding.ivTarget3, binding.ivTarget4)
        targets.forEachIndexed { index, target ->
            val draggable = zoneOccupants[target]
            solution[index] = when (draggable?.id) {
                R.id.ivDraggable1 -> '1'
                R.id.ivDraggable2 -> '2'
                R.id.ivDraggable3 -> '3'
                R.id.ivDraggable4 -> '4'
                else -> '0'
            }
        }
    }

    override fun onResume() {
        super.onResume()

        saveManager = SaveManager.getInstance()
        saveManager.saveCurrentLevel(requireContext(), -1)
    }


}