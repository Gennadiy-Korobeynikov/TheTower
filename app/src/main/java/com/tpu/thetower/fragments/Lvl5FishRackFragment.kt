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
import com.tpu.thetower.databinding.FragmentLvl5FishRackBinding
import com.tpu.thetower.fragments.Lvl4TimelineFragment
import com.tpu.thetower.puzzles.Lvl4PuzzleTimeline

class Lvl5FishRackFragment : Fragment(R.layout.fragment_lvl5_fish_rack),
    View.OnTouchListener,
    View.OnDragListener {

    private lateinit var binding: FragmentLvl5FishRackBinding
    private val originalPositions = mutableMapOf<View, Pair<Float, Float>>()
    private val zoneOccupants = mutableMapOf<View, View?>()

//    private val puzzle: Puzzle = Lvl4PuzzleFishes(5, "fishes")

    private var solution = charArrayOf('-', '-', '-', '-', '-', '-', '-', '-', '-', '-')



    private lateinit var ivFish1: ImageView
    private lateinit var ivFish2: ImageView
    private lateinit var ivFish3: ImageView
    private lateinit var ivFish4: ImageView
    private lateinit var ivFish5: ImageView
    private lateinit var ivTarget1: ImageView
    private lateinit var ivTarget2: ImageView
    private lateinit var ivTarget3: ImageView
    private lateinit var ivTarget4: ImageView
    private lateinit var ivTarget5: ImageView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl5FishRackBinding.bind(view)

        bindView()
        setListeners()
    }

    private fun bindView() {
        ivFish1 = binding.ivFish1
        ivFish2 = binding.ivFish2
        ivFish3 = binding.ivFish3
        ivFish4 = binding.ivFish4
        ivFish5 = binding.ivFish5
        ivTarget1 = binding.ivTarget1
        ivTarget2 = binding.ivTarget2
        ivTarget3 = binding.ivTarget3
        ivTarget4 = binding.ivTarget4
        ivTarget5 = binding.ivTarget5
    }

    private fun setListeners() {
        val draggables = listOf(
            ivFish1,
            ivFish2,
            ivFish3,
            ivFish4,
            ivFish5
        )
        draggables.forEach { it.setOnTouchListener(this@Lvl5FishRackFragment) }

        val targets = listOf(
            ivTarget1,
            ivTarget2,
            ivTarget3,
            ivTarget4,
            ivTarget5
        )
        targets.forEachIndexed {index, it ->
            it.setOnDragListener(this@Lvl5FishRackFragment)
            zoneOccupants[it] = targets[index]
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
//                if (puzzle.checkSolution(requireActivity(), String(solution))) {
//                    FragmentManager.changeBG(this, R.id.elevatorFragment) // Надо так , иначе кнопка назад не сработает
//                    FragmentManager.changeBG(this, R.id.lvl5Fragment)
//                }
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
            ivTarget5
        )
        targets.forEachIndexed { index, target ->
            val draggable = zoneOccupants[target]
            solution[index] = when (draggable?.id) {
                R.id.iv_fish1 -> '1'
                R.id.iv_fish2 -> '2'
                R.id.iv_fish3 -> '3'
                R.id.iv_fish4 -> '4'
                R.id.iv_fish5 -> '5'
                else -> '-'
            }
        }
    }
}