package com.tpu.thetower.fragments.lvl4

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.DragShadowBuilder
import androidx.fragment.app.Fragment
import com.tpu.thetower.Hintable
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl4TimelineBinding
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.puzzles.Lvl4PuzzleTimeline
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl4TimelineFragment : Fragment(R.layout.fragment_lvl4_timeline),
    View.OnTouchListener,
    View.OnDragListener,
    Hintable{

    private var _binding: FragmentLvl4TimelineBinding? = null
    private val binding get() = _binding!!

    private val originalPositions = mutableMapOf<View, Pair<Float, Float>>()
    private val zoneOccupants = mutableMapOf<View, View?>()

    private val puzzle: Puzzle = Lvl4PuzzleTimeline(4, "timeline")

    private var solution = charArrayOf('-', '-', '-', '-', '-', '-', '-', '-', '-', '-')

    private val draggables by lazy(LazyThreadSafetyMode.NONE) {
        listOf(
            binding.ivDraggable1,
            binding.ivDraggable2,
            binding.ivDraggable3,
            binding.ivDraggable4,
            binding.ivDraggable5,
            binding.ivDraggable6,
            binding.ivDraggable7,
            binding.ivDraggable8,
            binding.ivDraggable9,
            binding.ivDraggable10
        )
    }

    private val targets by lazy(LazyThreadSafetyMode.NONE) {
        listOf(
            binding.ivTarget1,
            binding.ivTarget2,
            binding.ivTarget3,
            binding.ivTarget4,
            binding.ivTarget5,
            binding.ivTarget6,
            binding.ivTarget7,
            binding.ivTarget8,
            binding.ivTarget9,
            binding.ivTarget10
        )
    }

    private lateinit var hintManager: HintManager
    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var hintManagerFactory: HintManager.Factory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLvl4TimelineBinding.bind(view)

        hintManager = hintManagerFactory.create(
            hints = listOf(
                "lvl4_timeline_hint1", "lvl4_timeline_hint2"
            ),
            level = 4,
            puzzle = "timeline"
        )

        setListeners()
    }

    private fun setListeners() {
        draggables.forEach { it.setOnTouchListener(this@Lvl4TimelineFragment) }

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
                                currentOccupant.requestLayout()
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
                if (puzzle.checkSolution(requireActivity(), saveRepo, String(solution))) {
                    FragmentNavigation.changeBG(this, R.id.elevatorFragment) // Надо так , иначе кнопка назад не сработает
                    FragmentNavigation.changeBG(this, R.id.lvl4Fragment)
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
        targets.forEachIndexed { index, target ->
            val draggable = zoneOccupants[target]
            solution[index] = when (draggable) {
                draggables.getOrNull(0) -> '1'
                draggables.getOrNull(1) -> '2'
                draggables.getOrNull(2) -> '3'
                draggables.getOrNull(3) -> '4'
                draggables.getOrNull(4) -> '5'
                draggables.getOrNull(5) -> '6'
                draggables.getOrNull(6) -> '7'
                draggables.getOrNull(7) -> '8'
                draggables.getOrNull(8) -> '9'
                draggables.getOrNull(9) -> '0'
                else -> '-'
            }
        }
    }

    override fun useHint() {
        hintManager.useHint(requireActivity())
    }

    override fun skipPuzzle() {
        puzzle.complete(saveRepo)
        FragmentNavigation.changeBG(this, R.id.elevatorFragment)
        FragmentNavigation.changeBG(this, R.id.lvl4Fragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Важно: lazy-списки держат ссылки на binding.*; чистим состояние, затем обнуляем binding
        originalPositions.clear()
        zoneOccupants.clear()
        _binding = null
    }

}