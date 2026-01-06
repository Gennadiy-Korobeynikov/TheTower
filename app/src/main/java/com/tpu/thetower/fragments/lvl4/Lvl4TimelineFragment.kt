package com.tpu.thetower.fragments.lvl4

import android.annotation.SuppressLint
import android.content.ClipData
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.DragShadowBuilder
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.tpu.thetower.Hintable
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl4TimelineBinding
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.puzzles.Lvl4PuzzleTimeline
import com.tpu.thetower.utils.CommonAnimationHelper
import com.tpu.thetower.utils.SoundEffect
import com.tpu.thetower.utils.getOrCreateBlur
import com.tpu.thetower.viewmodels.BlurViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl4TimelineFragment : Fragment(R.layout.fragment_lvl4_timeline),
    View.OnDragListener, Hintable {

    @Inject lateinit var soundManager : SoundManager

    private val blurVM: BlurViewModel by navGraphViewModels(R.id.nav_graph)

    private var _binding: FragmentLvl4TimelineBinding? = null
    private val binding get() = _binding!!


    private val originalPositions = mutableMapOf<View, Pair<Float, Float>>()
    private val zoneOccupants = mutableMapOf<View, View?>()
    private val downTranslationY = mutableMapOf<View, Float>()

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

    private companion object {
        const val CLIP_LABEL_TIMELINE = "TIMELINE_ITEM"
        private const val KEY_LVL4_SNAPSHOT = "lvl4_snapshot"
        private const val KEY_LVL4_BLUR = "lvl4_blur"
        private const val TAG = "Lvl4TimelineFragment"
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

        val levelSnapshot = blurVM.getBlur(KEY_LVL4_SNAPSHOT)
            ?: error("Snapshot must be set before opening puzzle")

        val blur = getOrCreateBlur(
            blurVM = blurVM,
            blurKey = KEY_LVL4_BLUR,
            sourceBitmap = levelSnapshot,
            radius = 220f,
            context = requireContext()
        )

        Log.d(TAG, "set blurred bg: snapshot=${levelSnapshot.width}x${levelSnapshot.height} blur=${blur.width}x${blur.height}")
        binding.ivBg.setImageBitmap(blur)

        setListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        targets.forEach {
            it.setOnDragListener(this@Lvl4TimelineFragment)
            zoneOccupants[it] = null
        }

        draggables.forEach { draggable ->
            draggable.post { originalPositions[draggable] = Pair(draggable.x, draggable.y) }
        }

        draggables.forEach { draggable ->
            draggable.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // FIX: запоминаем текущее смещение, а не насильно считаем что оно 0
                        downTranslationY[v] = v.translationY
                        v.animate()
                            .translationY(downTranslationY[v]!! - 20f)
                            .scaleX(1.05f)
                            .scaleY(1.05f)
                            .setDuration(100)
                            .start()
                        false
                    }

                    MotionEvent.ACTION_UP,
                    MotionEvent.ACTION_CANCEL,
                        -> {
                        val baseY = downTranslationY[v] ?: v.translationY
                        v.animate()
                            .translationY(baseY)
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start()
                        false
                    }

                    else -> false
                }
            }

            draggable.setOnLongClickListener { view ->
                val clipData = ClipData.newPlainText(CLIP_LABEL_TIMELINE, "1")
                val shadow = DragShadowBuilder(view)
                view.startDragAndDrop(clipData, shadow, view, 0)
                view.visibility = View.INVISIBLE
                true
            }
        }
    }

    override fun onDrag(targetView: View?, event: DragEvent?): Boolean {
        val e = event ?: return false
        val draggedView = e.localState as? View ?: return false
        val target = targetView ?: return false

        when (e.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                val label = e.clipDescription?.label?.toString()
                label == CLIP_LABEL_TIMELINE
            }

            DragEvent.ACTION_DRAG_ENTERED -> {
                if (target in zoneOccupants.keys) target.alpha = 0.7f
                true
            }

            DragEvent.ACTION_DRAG_EXITED -> {
                if (target in zoneOccupants.keys) target.alpha = 1f
                true
            }

            DragEvent.ACTION_DROP -> {
                if (target in zoneOccupants.keys) target.alpha = 1f
                handleDrop(target, draggedView)
                true
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                if (target in zoneOccupants.keys) target.alpha = 1f

                draggedView.visibility = View.VISIBLE

                if (!e.result) {
                    clearOccupancyForView(draggedView)
                    returnToOriginalPosition(draggedView)
                }

                updateSolution()
                if (puzzle.checkSolution(requireActivity(), saveRepo, String(solution))) {
                    passed()
                }
                true
            }

            else -> true
        }
        return true
    }

    private fun handleDrop(targetZone: View, draggedView: View) {
        val previousZone = zoneOccupants.entries.firstOrNull { it.value == draggedView }?.key
        val currentOccupant = zoneOccupants[targetZone]

        if (currentOccupant != null) {
            if (previousZone != null) {
                placeViewInZone(draggedView, targetZone)
                placeViewInZone(currentOccupant, previousZone)

                zoneOccupants[previousZone] = currentOccupant
                zoneOccupants[targetZone] = draggedView
            } else {
                returnToOriginalPosition(currentOccupant)
                placeViewInZone(draggedView, targetZone)
                zoneOccupants[targetZone] = draggedView
            }
        } else {
            // Empty zone
            previousZone?.let { zoneOccupants[it] = null }
            placeViewInZone(draggedView, targetZone)
            zoneOccupants[targetZone] = draggedView
        }
    }

    private fun returnToOriginalPosition(view: View) {
        originalPositions[view]?.let { pos ->
            view.x = pos.first
            view.y = pos.second
            view.translationY = 0f
            view.visibility = View.VISIBLE
            view.requestLayout()
        }
    }

    private fun clearOccupancyForView(view: View) {
        val occupiedZone = zoneOccupants.entries.firstOrNull { it.value == view }?.key ?: return
        zoneOccupants[occupiedZone] = null
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

    private fun passed() {
        soundManager.playSound(SoundEffect.CROW)

        CommonAnimationHelper.animatePuzzleCompletion(
            fragment = this,
            mainScreen = binding.mainScreen,
            fragmentRoot = binding.root
        )
    }

    override fun useHint() {
        hintManager.useHint(requireActivity())
    }

    override fun skipPuzzle() {
        puzzle.complete(saveRepo)
        passed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        originalPositions.clear()
        zoneOccupants.clear()
        downTranslationY.clear()
        _binding = null
    }

}