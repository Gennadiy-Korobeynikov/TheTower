package com.tpu.thetower.fragments.lvl3

import android.annotation.SuppressLint
import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.DragShadowBuilder
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl3Binding
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.models.PuzzleStatus
import com.tpu.thetower.utils.SoundEffect
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl3Fragment : Fragment(R.layout.fragment_lvl3), View.OnDragListener, Hintable {

    private var _binding: FragmentLvl3Binding? = null
    private val binding get() = _binding!!

    @Inject lateinit var musicManager: MusicManager
    @Inject lateinit var soundManager: SoundManager
    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var dialogManager: DialogManager
    @Inject lateinit var hintManagerFactory: HintManager.Factory

    private lateinit var hintManager: HintManager
    private lateinit var originalPosition: Pair<Float, Float>
    private lateinit var originalKeyPosition: Pair<Float, Float>

    private companion object {
        const val CLIP_LABEL_SLEEPING_PILLS = "SLEEPING_PILLS"
        const val CLIP_LABEL_KEY = "KEY"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLvl3Binding.bind(view)

        setListeners()

        hintManager = hintManagerFactory.create(
            hints = listOf("lvl3_to_puzzle0_hint1", "lvl3_to_puzzle0_hint2"),
            level = 3,
            puzzle = "sleeping pills"
        )

        binding.ivSleepingPillsDraggable.post {
            originalPosition = Pair(binding.ivSleepingPillsDraggable.x, binding.ivSleepingPillsDraggable.y)
        }

        binding.ivKeyDraggable.post {
            originalKeyPosition = Pair(binding.ivKeyDraggable.x, binding.ivKeyDraggable.y)
        }

        val donutsStatus = loadManager.getPuzzleStatus(3, "donuts")
        val buttonsStatus = loadManager.getPuzzleStatus(3, "buttons")
        val sleepingPillsStatus = loadManager.getPuzzleStatus(3, "sleeping pills")
        val vacuumCleanerStatus = loadManager.getPuzzleStatus(3, "vacuum cleaner")
        val keyStatus = loadManager.getPuzzleStatus(3, "key")

        // Первый диалог
        if (donutsStatus == PuzzleStatus.LOCKED.value) {
            dialogManager.startDialog(requireActivity(), "lvl3_security")
            saveRepo.savePuzzleStatus(3, "donuts", status = PuzzleStatus.IN_PROGRESS.value)
        }

        // Шкаф со снатворным открыт
        if (buttonsStatus == PuzzleStatus.COMPLETED.value) {
            binding.btnToPuzzleDonuts.visibility = View.GONE
            binding.btnToPuzzleButtonLock.visibility = View.GONE
            binding.ivClosetOpened.visibility = View.VISIBLE
            if (sleepingPillsStatus != PuzzleStatus.COMPLETED.value) {
                binding.ivSleepingPillsDraggable.visibility = View.VISIBLE
                binding.ivBg.setImageResource(R.drawable.lvl3_bg_no_sleeping_pills)
            }
        }

        // Получили снотворное
        if (sleepingPillsStatus == PuzzleStatus.IN_PROGRESS.value) {
            hintManager = hintManagerFactory.create(
                hints = listOf("lvl3_to_coffee_hint1"),
                level = 3,
                puzzle = "sleeping pills"
            )
        }

        // Охранник уснул
        if (sleepingPillsStatus == PuzzleStatus.COMPLETED.value) {
            soundManager.playSound(SoundEffect.GUARD_SNORING, repeat = -1)
            hintManager = hintManagerFactory.create(
                hints = listOf("lvl3_to_coffee_hint1"),
                level = 3,
                puzzle = "sleeping pills"
            )
            binding.ivBg.setImageResource(R.drawable.lvl3_bg_guard_sleeping)
            binding.ivCoffeeTarget.visibility = View.GONE
            binding.btnToMap.visibility = View.VISIBLE
        }

        // Пылесос приехал с ключом
        if (vacuumCleanerStatus == PuzzleStatus.COMPLETED.value) {
            binding.ivBg.setImageResource(R.drawable.lvl3_bg_hoover_no_key)
            binding.btnToPuzzleHoover.visibility = View.GONE
            binding.btnToMap.visibility = View.GONE
            binding.btnToVentilation.visibility = View.GONE

            // Ключ ещё не дропнут
            if (keyStatus == PuzzleStatus.LOCKED.value) {
                binding.ivKeyDraggable.visibility = View.VISIBLE
                binding.vFinalLockTarget.visibility = View.VISIBLE
            }
            // Дропунтый ключ, но не использованный
            else if (keyStatus == PuzzleStatus.IN_PROGRESS.value) {
                binding.vFinalLockTarget.visibility = View.GONE
                binding.ivKeyDraggable.visibility = View.GONE
            }
            // Ключ настроен и использован
            else if (keyStatus == PuzzleStatus.COMPLETED.value) {
                binding.ivBg.setImageResource(R.drawable.lvl3_bg_last)
                binding.vFinalLockTarget.visibility = View.GONE
                binding.ivKeyDraggable.visibility = View.GONE
                binding.btnToPuzzleModel.visibility = View.GONE
                binding.btnToPuzzleFinalLock.visibility = View.GONE
                binding.btnToAccessCard.visibility = View.VISIBLE
            }
        }

        if (loadManager.getCurrentAccessCardNumber() >= 4)
            binding.btnToPuzzleFinalLock.visibility = View.GONE

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        binding.btnToPuzzleDonuts.setOnClickListener {
            if (loadManager.getPuzzleStatus(3, "donuts after shaking") == PuzzleStatus.LOCKED.value) {
                dialogManager.startDialog(requireActivity(), "lvl3_donuts")
                saveRepo.savePuzzleStatus(3, "donuts after shaking", status = PuzzleStatus.IN_PROGRESS.value)
            }
            FragmentNavigation.changeBG(this, R.id.action_lvl3Fragment_to_lvl3PuzzleDonutsFragment)
        }

        binding.btnToPuzzleButtonLock.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl3Fragment_to_lvl3PuzzleButtonsFragment)
        }

        binding.btnToPuzzleHoover.setOnClickListener {
            if (loadManager.getPuzzleStatus(3, "sleeping pills") != PuzzleStatus.COMPLETED.value) {
                dialogManager.startDialog(requireActivity(), "lvl3_computer")
            } else {
                FragmentNavigation.changeBG(this, R.id.action_lvl3Fragment_to_lvl3PuzzleHooverFragment)
            }
        }

        binding.btnToMap.setOnClickListener {
            binding.ivMap.visibility = View.VISIBLE
            UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
        }

        binding.ivMap.setOnClickListener {
            binding.ivMap.visibility = View.GONE
            UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
        }

        binding.btnToPuzzleModel.setOnClickListener {
            if (loadManager.getPuzzleStatus(3, "sleeping pills") != PuzzleStatus.COMPLETED.value) {
                dialogManager.startDialog(requireActivity(), "lvl3_computer")
            } else {
                FragmentNavigation.changeBG(this, R.id.action_lvl3Fragment_to_lvl3PuzzleEditorFragment)
            }
        }
        binding.btnToVentilation.setOnClickListener {
            dialogManager.startDialog(requireActivity(), "lvl3_ventilation")
        }

        binding.btnToPuzzleFinalLock.setOnClickListener {
            if (loadManager.getPuzzleStatus(3, "sleeping pills") != PuzzleStatus.COMPLETED.value) {
                dialogManager.startDialog(requireActivity(), "lvl3_npc_security_final_lock")
            }
            else if (loadManager.getPuzzleStatus(3, "vacuum cleaner") != PuzzleStatus.COMPLETED.value) {
                dialogManager.startDialog(requireActivity(), "lvl3_need_key")
            }
            else if (loadManager.getPuzzleStatus(3, "key") == PuzzleStatus.IN_PROGRESS.value) {
                FragmentNavigation.changeBG(this, R.id.action_lvl3Fragment_to_lvl3PuzzleKeyFragment)
            }
            else if (loadManager.getPuzzleStatus(3, "vacuum cleaner") == PuzzleStatus.COMPLETED.value) {
                dialogManager.startDialog(requireActivity(), "lvl3_need_to_use_key")
            }

        }

        binding.btnToAccessCard.setOnClickListener {
            binding.ivAccessCard.visibility = View.VISIBLE
            UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
            loadManager.changeAccessCardNumber(4)
            saveRepo.saveLevelCompletedStatus(3)
        }

        binding.ivAccessCard.setOnClickListener {
            binding.ivAccessCard.visibility = View.GONE
            binding.btnToPuzzleFinalLock.visibility = View.GONE
            binding.btnToAccessCard.visibility = View.GONE
            UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
        }

        binding.ivCoffeeTarget.setOnDragListener(this@Lvl3Fragment)
        binding.vFinalLockTarget.setOnDragListener(this@Lvl3Fragment)

        binding.ivSleepingPillsDraggable.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.animate()
                        .translationY(-20f)
                        .scaleX(1.05f)
                        .scaleY(1.05f)
                        .setDuration(100)
                        .start()
                    false
                }

                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> {
                    v.animate()
                        .translationY(0f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                    false
                }

                else -> false
            }
        }

        binding.ivSleepingPillsDraggable.setOnLongClickListener { view ->
            val clipData = ClipData.newPlainText(CLIP_LABEL_SLEEPING_PILLS, "1")
            val shadow = DragShadowBuilder(view)

            view.startDragAndDrop(clipData, shadow, view, 0)
            view.visibility = View.INVISIBLE
            true
        }

        binding.ivKeyDraggable.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.animate()
                        .translationY(-20f)
                        .scaleX(1.05f)
                        .scaleY(1.05f)
                        .setDuration(100)
                        .start()
                    false
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    v.animate()
                        .translationY(0f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                    false
                }
                else -> false
            }
        }

        binding.ivKeyDraggable.setOnLongClickListener { view ->
            val clipData = ClipData.newPlainText(CLIP_LABEL_KEY, "1")
            val shadow = DragShadowBuilder(view)
            view.startDragAndDrop(clipData, shadow, view, 0)
            view.visibility = View.INVISIBLE
            true
        }
    }

    override fun onDrag(targetView: View, event: DragEvent): Boolean {
        val draggedView = event.localState as? View ?: return false

        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                val label = event.clipDescription?.label?.toString()
                label == CLIP_LABEL_SLEEPING_PILLS || label == CLIP_LABEL_KEY
            }

            DragEvent.ACTION_DRAG_ENTERED -> {
                targetView.alpha = 0.7f
                true
            }

            DragEvent.ACTION_DRAG_EXITED -> {
                targetView.alpha = 1f
                true
            }

            DragEvent.ACTION_DROP -> {
                targetView.alpha = 1f

                when (targetView) {
                    binding.ivCoffeeTarget -> onPillsDropped(draggedView)
                    binding.vFinalLockTarget -> onKeyDropped(draggedView)
                    else -> returnDraggedToOriginalPosition(draggedView)
                }
                true
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                targetView.alpha = 1f
                if (!event.result) returnDraggedToOriginalPosition(draggedView)
                true
            }

            else -> true
        }
        return true
    }

    private fun onPillsDropped(draggedView: View) {
        binding.ivBg.setImageResource(R.drawable.lvl3_bg_guard_sleeping)
        binding.ivCoffeeTarget.visibility = View.GONE
        binding.btnToMap.visibility = View.VISIBLE
        saveRepo.savePuzzleStatus(3, "sleeping pills")
        soundManager.playSound(SoundEffect.GUARD_SNORING, repeat = -1)

        draggedView.visibility = View.GONE
    }

    private fun onKeyDropped(draggedView: View) {
        if (draggedView != binding.ivKeyDraggable) {
            returnDraggedToOriginalPosition(draggedView)
            return
        }
        FragmentNavigation.changeBG(this, R.id.action_lvl3Fragment_to_lvl3PuzzleKeyFragment)
        binding.ivKeyDraggable.visibility = View.GONE
        binding.vFinalLockTarget.visibility = View.GONE

        saveRepo.savePuzzleStatus(3, "key", PuzzleStatus.IN_PROGRESS.value)
        draggedView.visibility = View.GONE
    }

    private fun returnDraggedToOriginalPosition(view: View) {
        when (view) {
            binding.ivSleepingPillsDraggable -> {
                view.x = originalPosition.first
                view.y = originalPosition.second
            }
            binding.ivKeyDraggable -> {
                view.x = originalKeyPosition.first
                view.y = originalKeyPosition.second
            }
            else -> return
        }
        view.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        saveRepo.saveCurrentLevel(3)
    }


    override fun useHint() {
        if (loadManager.getPuzzleStatus(3, "sleeping pills") == PuzzleStatus.COMPLETED.value)
            dialogManager.startDialog(requireActivity(), "hint_is_not_here")
        else
            hintManager.useHint(requireActivity())
    }

    override fun skipPuzzle() {
        Snackbar.make(requireView(), "?Пончики; Шкаф; Кофе: ?Пылесос; Замок", Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        soundManager.stopSound(SoundEffect.GUARD_SNORING)
    }
}