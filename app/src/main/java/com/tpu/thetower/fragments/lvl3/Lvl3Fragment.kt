package com.tpu.thetower.fragments.lvl3

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.ClipData
import android.graphics.Color
import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.DragShadowBuilder
import android.view.ViewConfiguration
import android.view.animation.LinearInterpolator
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
import com.tpu.thetower.utils.LongPressCircleDrawable
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

    private var isDragging = false

    private var sleepingPillsAnimator: ValueAnimator? = null
    private var keyAnimator: ValueAnimator? = null

    private val sleepingPillsCircle by lazy {
        LongPressCircleDrawable(
            color = Color.WHITE,
            strokeWidthPx = 3f * resources.displayMetrics.density
        )
    }

    private val keyCircle by lazy {
        LongPressCircleDrawable(
            color = Color.WHITE,
            strokeWidthPx = 3f * resources.displayMetrics.density
        )
    }

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

            // Дропнутый ключ, но не использованный
            else if (keyStatus == PuzzleStatus.IN_PROGRESS.value) {
                binding.vFinalLockTarget.visibility = View.GONE
                binding.ivKeyDraggable.visibility = View.GONE
            }

            // Ключ использован
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
                saveRepo.savePuzzleStatus(
                    3,
                    "donuts after shaking",
                    status = PuzzleStatus.IN_PROGRESS.value
                )
            }

            FragmentNavigation.changeBG(
                this,
                R.id.action_lvl3Fragment_to_lvl3PuzzleDonutsFragment
            )
        }

        binding.btnToPuzzleButtonLock.setOnClickListener {
            FragmentNavigation.changeBG(
                this,
                R.id.action_lvl3Fragment_to_lvl3PuzzleButtonsFragment
            )
        }

        binding.btnToPuzzleHoover.setOnClickListener {
            if (loadManager.getPuzzleStatus(3, "sleeping pills") != PuzzleStatus.COMPLETED.value) {
                dialogManager.startDialog(requireActivity(), "lvl3_computer")
            } else {
                FragmentNavigation.changeBG(
                    this,
                    R.id.action_lvl3Fragment_to_lvl3PuzzleHooverFragment
                )
            }
        }

        binding.btnToMap.setOnClickListener {
            binding.ivMap.visibility = View.VISIBLE

            UiVisibilityController.hide(
                requireActivity(),
                UiVisibilityController.UiContainer.GO_BACK_ARROW
            )
        }

        binding.ivMap.setOnClickListener {
            binding.ivMap.visibility = View.GONE

            UiVisibilityController.show(
                requireActivity(),
                UiVisibilityController.UiContainer.GO_BACK_ARROW
            )
        }

        binding.btnToPuzzleModel.setOnClickListener {
            if (loadManager.getPuzzleStatus(3, "sleeping pills") != PuzzleStatus.COMPLETED.value) {
                dialogManager.startDialog(requireActivity(), "lvl3_computer")
            } else {
                FragmentNavigation.changeBG(
                    this,
                    R.id.action_lvl3Fragment_to_lvl3PuzzleEditorFragment
                )
            }
        }

        binding.btnToVentilation.setOnClickListener {
            dialogManager.startDialog(requireActivity(), "lvl3_ventilation")
        }

        binding.btnToPuzzleFinalLock.setOnClickListener {

            if (loadManager.getPuzzleStatus(3, "sleeping pills") != PuzzleStatus.COMPLETED.value) {
                dialogManager.startDialog(requireActivity(), "lvl3_npc_security_final_lock")
            }

            else if (loadManager.getPuzzleStatus(
                    3,
                    "vacuum cleaner"
                ) != PuzzleStatus.COMPLETED.value
            ) {
                dialogManager.startDialog(requireActivity(), "lvl3_need_key")
            }

            else if (loadManager.getPuzzleStatus(
                    3,
                    "key"
                ) == PuzzleStatus.IN_PROGRESS.value
            ) {
                FragmentNavigation.changeBG(
                    this,
                    R.id.action_lvl3Fragment_to_lvl3PuzzleKeyFragment
                )
            }

            else if (loadManager.getPuzzleStatus(
                    3,
                    "vacuum cleaner"
                ) == PuzzleStatus.COMPLETED.value
            ) {
                dialogManager.startDialog(requireActivity(), "lvl3_need_to_use_key")
            }
        }

        binding.btnToAccessCard.setOnClickListener {

            binding.ivAccessCard.visibility = View.VISIBLE

            UiVisibilityController.hide(
                requireActivity(),
                UiVisibilityController.UiContainer.GO_BACK_ARROW
            )

            loadManager.changeAccessCardNumber(4)
            saveRepo.saveLevelCompletedStatus(3)
        }

        binding.ivAccessCard.setOnClickListener {

            binding.ivAccessCard.visibility = View.GONE
            binding.btnToPuzzleFinalLock.visibility = View.GONE
            binding.btnToAccessCard.visibility = View.GONE

            UiVisibilityController.show(
                requireActivity(),
                UiVisibilityController.UiContainer.GO_BACK_ARROW
            )
        }

        binding.ivCoffeeTarget.setOnDragListener(this@Lvl3Fragment)
        binding.vFinalLockTarget.setOnDragListener(this@Lvl3Fragment)

        setupDraggable(
            draggableView = binding.ivSleepingPillsDraggable,
            circleDrawable = sleepingPillsCircle,
            animatorProvider = { sleepingPillsAnimator },
            animatorSetter = { sleepingPillsAnimator = it },
            clipLabel = CLIP_LABEL_SLEEPING_PILLS
        )

        setupDraggable(
            draggableView = binding.ivKeyDraggable,
            circleDrawable = keyCircle,
            animatorProvider = { keyAnimator },
            animatorSetter = { keyAnimator = it },
            clipLabel = CLIP_LABEL_KEY
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupDraggable(
        draggableView: View,
        circleDrawable: LongPressCircleDrawable,
        animatorProvider: () -> ValueAnimator?,
        animatorSetter: (ValueAnimator?) -> Unit,
        clipLabel: String
    ) {

        draggableView.foreground = circleDrawable

        draggableView.setOnTouchListener { v, event ->

            when (event.action) {

                MotionEvent.ACTION_DOWN -> {

                    v.animate()
                        .translationY(-20f)
                        .scaleX(1.05f)
                        .scaleY(1.05f)
                        .setDuration(100)
                        .start()

                    startCircleAnimation(
                        circleDrawable,
                        animatorProvider,
                        animatorSetter
                    )

                    false
                }

                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> {

                    if (!isDragging) {

                        cancelCircleAnimation(
                            circleDrawable,
                            animatorProvider,
                            animatorSetter
                        )

                        v.animate()
                            .translationY(0f)
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start()
                    }

                    false
                }

                else -> false
            }
        }

        draggableView.setOnLongClickListener { view ->

            val clipData = ClipData.newPlainText(clipLabel, "1")

            isDragging = true

            circleDrawable.isActive = false
            circleDrawable.progress = 0f

            view.startDragAndDrop(
                clipData,
                DragShadowBuilder(view),
                view,
                0
            )

            view.visibility = View.INVISIBLE
            true
        }
    }

    override fun onDrag(targetView: View, event: DragEvent): Boolean {

        val draggedView = event.localState as? View ?: return false

        when (event.action) {

            DragEvent.ACTION_DRAG_STARTED -> {

                val label = event.clipDescription?.label?.toString()

                return label == CLIP_LABEL_SLEEPING_PILLS ||
                        label == CLIP_LABEL_KEY
            }

            DragEvent.ACTION_DRAG_ENTERED -> {
                targetView.alpha = 0.7f
            }

            DragEvent.ACTION_DRAG_EXITED -> {
                targetView.alpha = 1f
            }

            DragEvent.ACTION_DROP -> {

                targetView.alpha = 1f

                when (targetView) {

                    binding.ivCoffeeTarget -> {
                        onPillsDropped(draggedView)
                    }

                    binding.vFinalLockTarget -> {
                        onKeyDropped(draggedView)
                    }

                    else -> {
                        returnToOriginalState(draggedView)
                    }
                }
            }

            DragEvent.ACTION_DRAG_ENDED -> {

                targetView.alpha = 1f

                if (!event.result) {
                    returnToOriginalState(draggedView)
                } else {
                    isDragging = false
                }
            }
        }

        return true
    }

    private fun onPillsDropped(draggedView: View) {

        if (draggedView != binding.ivSleepingPillsDraggable) {
            returnToOriginalState(draggedView)
            return
        }

        binding.ivBg.setImageResource(R.drawable.lvl3_bg_guard_sleeping)
        binding.ivCoffeeTarget.visibility = View.GONE
        binding.btnToMap.visibility = View.VISIBLE

        saveRepo.savePuzzleStatus(3, "sleeping pills")

        soundManager.playSound(SoundEffect.GUARD_SNORING, repeat = -1)
        soundManager.playSound(SoundEffect.PILLS_DROP)


        draggedView.visibility = View.GONE
    }

    private fun onKeyDropped(draggedView: View) {

        if (draggedView != binding.ivKeyDraggable) {
            returnToOriginalState(draggedView)
            return
        }

        FragmentNavigation.changeBG(
            this,
            R.id.action_lvl3Fragment_to_lvl3PuzzleKeyFragment
        )

        binding.ivKeyDraggable.visibility = View.GONE
        binding.vFinalLockTarget.visibility = View.GONE

        saveRepo.savePuzzleStatus(
            3,
            "key",
            PuzzleStatus.IN_PROGRESS.value
        )

        draggedView.visibility = View.GONE
    }

    private fun startCircleAnimation(
        circleDrawable: LongPressCircleDrawable,
        animatorProvider: () -> ValueAnimator?,
        animatorSetter: (ValueAnimator?) -> Unit
    ) {

        animatorProvider()?.cancel()

        circleDrawable.progress = 0f
        circleDrawable.isActive = true

        val animator = ValueAnimator.ofFloat(0f, 1f).apply {

            duration = ViewConfiguration.getLongPressTimeout().toLong()
            interpolator = LinearInterpolator()

            addUpdateListener {
                circleDrawable.progress = it.animatedValue as Float
            }

            start()
        }

        animatorSetter(animator)
    }

    private fun cancelCircleAnimation(
        circleDrawable: LongPressCircleDrawable,
        animatorProvider: () -> ValueAnimator?,
        animatorSetter: (ValueAnimator?) -> Unit
    ) {

        animatorProvider()?.cancel()

        animatorSetter(null)

        circleDrawable.isActive = false
        circleDrawable.progress = 0f
    }

    private fun returnToOriginalState(view: View) {

        isDragging = false

        when (view) {

            binding.ivSleepingPillsDraggable -> {
                cancelCircleAnimation(
                    sleepingPillsCircle,
                    { sleepingPillsAnimator },
                    { sleepingPillsAnimator = it }
                )
            }

            binding.ivKeyDraggable -> {
                cancelCircleAnimation(
                    keyCircle,
                    { keyAnimator },
                    { keyAnimator = it }
                )
            }
        }

        view.visibility = View.VISIBLE

        view.animate()
            .translationY(0f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(150)
            .start()
    }

    override fun onResume() {
        super.onResume()
        saveRepo.saveCurrentLevel(3)
    }

    override fun useHint() {

        if (loadManager.getPuzzleStatus(
                3,
                "sleeping pills"
            ) == PuzzleStatus.COMPLETED.value
        ) {
            dialogManager.startDialog(requireActivity(), "hint_is_not_here")
        } else {
            hintManager.useHint(requireActivity())
        }
    }

    override fun skipPuzzle() {
        Snackbar.make(
            requireView(),
            "?Пончики; Шкаф; Кофе: ?Пылесос; Замок",
            Snackbar.LENGTH_SHORT
        ).show()

        loadManager.changeAccessCardNumber(4)
        saveRepo.saveLevelCompletedStatus(3)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null

        soundManager.stopSound(SoundEffect.GUARD_SNORING)
    }
}