package com.tpu.thetower.fragments

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.DragShadowBuilder
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl4TimelineBinding
import com.tpu.thetower.puzzles.Lvl4PuzzleTimeline

class Lvl4TimelineFragment : Fragment(R.layout.fragment_lvl4_timeline),
    View.OnTouchListener,
    View.OnDragListener {

    private lateinit var binding: FragmentLvl4TimelineBinding
    private val originalPositions = mutableMapOf<View, Pair<Float, Float>>()
    private val zoneOccupants = mutableMapOf<View, View?>()

    private val puzzle: Puzzle = Lvl4PuzzleTimeline(4, "timeline")

    private var solution = charArrayOf('-', '-', '-', '-', '-', '-', '-', '-', '-', '-')


    private lateinit var ivDraggable1: ImageView
    private lateinit var ivDraggable2: ImageView
    private lateinit var ivDraggable3: ImageView
    private lateinit var ivDraggable4: ImageView
    private lateinit var ivDraggable5: ImageView
    private lateinit var ivDraggable6: ImageView
    private lateinit var ivDraggable7: ImageView
    private lateinit var ivDraggable8: ImageView
    private lateinit var ivDraggable9: ImageView
    private lateinit var ivDraggable10: ImageView
    private lateinit var ivTarget1: ImageView
    private lateinit var ivTarget2: ImageView
    private lateinit var ivTarget3: ImageView
    private lateinit var ivTarget4: ImageView
    private lateinit var ivTarget5: ImageView
    private lateinit var ivTarget6: ImageView
    private lateinit var ivTarget7: ImageView
    private lateinit var ivTarget8: ImageView
    private lateinit var ivTarget9: ImageView
    private lateinit var ivTarget10: ImageView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl4TimelineBinding.bind(view)

        bindView()
        setListeners()

    }

    private fun bindView() {
        ivDraggable1 = binding.ivDraggable1
        ivDraggable2 = binding.ivDraggable2
        ivDraggable3 = binding.ivDraggable3
        ivDraggable4 = binding.ivDraggable4
        ivDraggable5 = binding.ivDraggable5
        ivDraggable6 = binding.ivDraggable6
        ivDraggable7 = binding.ivDraggable7
        ivDraggable8 = binding.ivDraggable8
        ivDraggable9 = binding.ivDraggable9
        ivDraggable10 = binding.ivDraggable10
        ivTarget1 = binding.ivTarget1
        ivTarget2 = binding.ivTarget2
        ivTarget3 = binding.ivTarget3
        ivTarget4 = binding.ivTarget4
        ivTarget5 = binding.ivTarget5
        ivTarget6 = binding.ivTarget6
        ivTarget7 = binding.ivTarget7
        ivTarget8 = binding.ivTarget8
        ivTarget9 = binding.ivTarget9
        ivTarget10 = binding.ivTarget10
    }

    private fun setListeners() {
        val draggables = listOf(
            ivDraggable1,
            ivDraggable2,
            ivDraggable3,
            ivDraggable4,
            ivDraggable5,
            ivDraggable6,
            ivDraggable7,
            ivDraggable8,
            ivDraggable9,
            ivDraggable10
        )
        draggables.forEach { it.setOnTouchListener(this@Lvl4TimelineFragment) }

        val targets = listOf(
            ivTarget1,
            ivTarget2,
            ivTarget3,
            ivTarget4,
            ivTarget5,
            ivTarget6,
            ivTarget7,
            ivTarget8,
            ivTarget9,
            ivTarget10
        )
        targets.forEach {
            it.setOnDragListener(this@Lvl4TimelineFragment)
            zoneOccupants[it] = null
        }

        draggables.forEach { draggable ->
            draggable.post {
                originalPositions[draggable] = Pair(draggable.x, draggable.y)
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
                if (puzzle.checkSolution(requireActivity(), String(solution))) {
                    FragmentManager.changeBG(this, R.id.elevatorFragment) // Надо так , иначе кнопка назад не сработает
                    FragmentManager.changeBG(this, R.id.lvl4Fragment)
                }
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
        val targets = listOf(
            ivTarget1,
            ivTarget2,
            ivTarget3,
            ivTarget4,
            ivTarget5,
            ivTarget6,
            ivTarget7,
            ivTarget8,
            ivTarget9,
            ivTarget10
        )
        targets.forEachIndexed { index, target ->
            val draggable = zoneOccupants[target]
            solution[index] = when (draggable?.id) {
                R.id.iv_draggable1 -> '1'
                R.id.iv_draggable2 -> '2'
                R.id.iv_draggable3 -> '3'
                R.id.iv_draggable4 -> '4'
                R.id.iv_draggable5 -> '5'
                R.id.iv_draggable6 -> '6'
                R.id.iv_draggable7 -> '7'
                R.id.iv_draggable8 -> '8'
                R.id.iv_draggable9 -> '9'
                R.id.iv_draggable10 -> '0'
                else -> '-'
            }
        }
    }
}