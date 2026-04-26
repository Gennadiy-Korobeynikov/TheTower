package com.tpu.thetower.fragments.lvl5

import android.annotation.SuppressLint
import android.content.ClipData
import android.graphics.PointF
import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.DragShadowBuilder
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.tpu.thetower.Hintable
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl5FishRackBinding
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.puzzles.Lvl5PuzzleFishRack
import com.tpu.thetower.utils.CommonAnimationHelper
import com.tpu.thetower.utils.getOrCreateBlur
import com.tpu.thetower.viewmodels.BlurViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl5FishRackFragment : Fragment(R.layout.fragment_lvl5_fish_rack),
    View.OnDragListener, Hintable {

    private var _binding: FragmentLvl5FishRackBinding? = null
    private val binding get() = _binding!!

    private val blurVM: BlurViewModel by navGraphViewModels(R.id.nav_lvl5)

    private val lastValidPositions = mutableMapOf<View, PointF>()

    private val zoneOccupants = mutableMapOf<View, View?>()
    private val downTranslationY = mutableMapOf<View, Float>()

    private lateinit var hintManager: HintManager

    @Inject lateinit var saveRepo : SaveRepository
    @Inject lateinit var soundManager : SoundManager
    @Inject lateinit var hintManagerFactory: HintManager.Factory


    private val puzzle: Puzzle = Lvl5PuzzleFishRack(5, "fish rack")

    private var solution = charArrayOf('-', '-', '-', '-', '-')

    private val draggables by lazy(LazyThreadSafetyMode.NONE) {
        listOf(
            binding.ivFish1,
            binding.ivFish2,
            binding.ivFish3,
            binding.ivFish4,
            binding.ivFish5
        )
    }

    private val targets by lazy(LazyThreadSafetyMode.NONE) {
        listOf(
            binding.ivTarget1,
            binding.ivTarget2,
            binding.ivTarget3,
            binding.ivTarget4,
            binding.ivTarget5
        )
    }

    private companion object {
        const val CLIP_LABEL_FISH_RACK = "FISH_RACK_ITEM"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLvl5FishRackBinding.bind(view)

        hintManager = hintManagerFactory.create(
            hints = listOf(
                "lvl5_fish_rack_hint1",
                "lvl5_fish_rack_hint2",
                "lvl5_fish_rack_hint3"
            ),
            level = 5,
            puzzle = "fish rack"
        )

        val levelSnapshot = blurVM.getBlur(Lvl5Fragment.KEY_LVL5_SNAPSHOT)
            ?: error("Snapshot must be set before opening puzzle")

        val blur = getOrCreateBlur(
            blurVM = blurVM,
            blurKey = Lvl5Fragment.KEY_LVL5_BLUR,
            sourceBitmap = levelSnapshot,
            radius = 220f,
            context = requireContext()
        )

        binding.ivBg.setImageBitmap(blur)

        setListeners()


        // Рыбы уже висят (в соответствии с фоном)
        binding.root.post {
            val initial = listOf(
                binding.ivTarget1 to binding.ivFish3,
                binding.ivTarget2 to binding.ivFish4,
                binding.ivTarget3 to binding.ivFish5,
                binding.ivTarget4 to binding.ivFish2,
                binding.ivTarget5 to binding.ivFish1,
            )

            initial.forEach { (target, fish) ->
                zoneOccupants[target] = fish
                placeViewInZone(fish, target)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        targets.forEach {
            it.setOnDragListener(this@Lvl5FishRackFragment)
            zoneOccupants[it] = null
        }

        draggables.forEach { draggable ->
            draggable.post { lastValidPositions[draggable] = PointF(draggable.x, draggable.y) }
        }

        draggables.forEach { draggable ->
            draggable.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
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
                    MotionEvent.ACTION_CANCEL -> {
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
                val clipData = ClipData.newPlainText(CLIP_LABEL_FISH_RACK, "1")
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
                label == CLIP_LABEL_FISH_RACK
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
                    returnToLastValidPosition(draggedView)
                    return true
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

    private fun passed() {
        //soundManager.playSound(SoundEffect.CROW)

        CommonAnimationHelper.animatePuzzleCompletion(
            fragment = this,
            mainScreen = binding.mainScreen,
            fragmentRoot = binding.root
        )
    }

    private fun returnToLastValidPosition(view: View) {
        val p = lastValidPositions[view]
        if (p != null) {
            view.x = p.x
            view.y = p.y
        }
        view.translationY = 0f
        view.visibility = View.VISIBLE
        view.requestLayout()
    }

    private fun placeViewInZone(view: View, zone: View) {
        view.apply {
            x = zone.x + (zone.width - width) / 2f
            y = zone.y + (zone.height - height) / 2f
            visibility = View.VISIBLE
        }
        lastValidPositions[view] = PointF(view.x, view.y)
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
                returnToLastValidPosition(currentOccupant)
                placeViewInZone(draggedView, targetZone)
                zoneOccupants[targetZone] = draggedView
            }
        } else {
            previousZone?.let { zoneOccupants[it] = null }
            placeViewInZone(draggedView, targetZone)
            zoneOccupants[targetZone] = draggedView
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
                else -> '-'
            }
        }
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
        lastValidPositions.clear()
        zoneOccupants.clear()
        downTranslationY.clear()
        _binding = null
    }
}