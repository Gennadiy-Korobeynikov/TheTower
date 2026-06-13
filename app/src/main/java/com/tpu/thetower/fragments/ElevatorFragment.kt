package com.tpu.thetower.fragments

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
import com.tpu.thetower.AppPreferences
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentElevatorBinding
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.utils.LongPressCircleDrawable
import com.tpu.thetower.utils.SoundEffect
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ElevatorFragment : Fragment(R.layout.fragment_elevator), View.OnDragListener {

    private lateinit var binding: FragmentElevatorBinding

    @Inject lateinit var soundManager: SoundManager
    @Inject lateinit var musicManager: MusicManager
    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var dialogManager: DialogManager
    @Inject lateinit var saveRepo: SaveRepository

    private lateinit var prefs: AppPreferences
    private lateinit var originalPosition: Pair<Float, Float>

    private var currAccessLevel: Int = 0

    private var isDragging = false
    private var longPressAnimator: ValueAnimator? = null

    private val longPressCircle by lazy {
        LongPressCircleDrawable(
            color = Color.WHITE,
            strokeWidthPx = 3f * resources.displayMetrics.density
        )
    }


    private val openedLvlButtons: MutableList<View> = mutableListOf()

    private val lvlActions: List<Int> = listOf(
        R.id.action_elevatorFragment_to_lvl0Fragment,
        R.id.action_elevatorFragment_to_lvl1Fragment,
        R.id.action_elevatorFragment_to_lvl2Fragment,
        R.id.action_elevatorFragment_to_lvl3Fragment,
        R.id.action_elevatorFragment_to_lvl4Fragment,
        R.id.action_elevatorFragment_to_lvl5Fragment,
        R.id.action_elevatorFragment_to_lvl6Fragment
    )

    private val lvlButtons: List<View>
        get() = listOf(
            binding.btnElevatorToLvl0,
            binding.btnElevatorToLvl1,
            binding.btnElevatorToLvl2,
            binding.btnElevatorToLvl3,
            binding.btnElevatorToLvl4,
            binding.btnElevatorToLvl5,
            binding.btnElevatorToLvl6
        )

    private val btnImages: List<Int>
        get() = listOf(
            R.drawable.elevator_btn_0,
            R.drawable.elevator_btn_1,
            R.drawable.elevator_btn_2,
            R.drawable.elevator_btn_3,
            R.drawable.elevator_btn_4,
            R.drawable.elevator_btn_5,
            R.drawable.elevator_btn_6
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentElevatorBinding.bind(view)
        prefs = AppPreferences(requireContext())
        currAccessLevel = if (prefs.isMaxAccessLvl) 6 else loadManager.getAccessLevel()

        setListeners()

        val receivedData = arguments?.getString("saved_level")
        if (receivedData != null) {
            FragmentNavigation.changeBG(this, receivedData.toInt())
            arguments = Bundle()
        }

        musicManager.stopMusic()

        binding.ivAccessCardDraggable.post {
            originalPosition = Pair(binding.ivAccessCardDraggable.x, binding.ivAccessCardDraggable.y)
        }

        if (loadManager.getCurrentAccessCardNumber() != 0) {
            binding.ivAccessCardDraggable.visibility = View.VISIBLE
            binding.ivAccessCardDraggable.setImageResource(
                loadManager.getCardImage(loadManager.getCurrentAccessCardNumber())
            )
        }

        unlockLvls()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {


        lvlButtons.forEachIndexed { index, btn ->
            btn.setOnClickListener {
                if (loadManager.getCurrentDialogIndex(0, "elevator") == 0
                    && index != 1 && currAccessLevel == 0) // В первый раз только на нулевой этаж
                    dialogManager.startDialog(requireActivity(), "lvl0_elevator_other_floor_first")

                if (btn !in openedLvlButtons) return@setOnClickListener

                FragmentNavigation.changeBG(this, lvlActions[index])
                UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
                soundManager.playSound(SoundEffect.ELEVATOR_DOOR)
                soundManager.playSound(SoundEffect.STEPS)
            }
        }


        binding.ivAccessCardDraggable.foreground = longPressCircle
        binding.ivAccessCardDraggable.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.animate()
                        .translationY(-20f).scaleX(1.05f).scaleY(1.05f)
                        .setDuration(100).start()
                    startCircleAnimation()
                    false
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (!isDragging) {
                        cancelCircleAnimation()
                        v.animate()
                            .translationY(0f).scaleX(1f).scaleY(1f)
                            .setDuration(100).start()
                    }
                    false
                }

                else -> false
            }
        }

        binding.ivAccessCardDraggable.setOnLongClickListener { view ->
            if (loadManager.getCurrentAccessCardNumber() == 0) return@setOnLongClickListener false

            val clipData = ClipData.newPlainText(
                "ACCESS_CARD",
                loadManager.getCurrentAccessCardNumber().toString()
            )

            isDragging = true
            longPressCircle.isActive = false  // Прячем круг вместе с view
            longPressCircle.progress = 0f

            view.startDragAndDrop(clipData, DragShadowBuilder(view), view, 0)
            view.visibility = View.INVISIBLE
            true
        }
        binding.ivCardReader.setOnDragListener(this@ElevatorFragment)
    }

    private fun unlockLvls() {
        val unlockingLvls = (0..currAccessLevel)
        unlockingLvls.forEach { i ->
            openedLvlButtons.add(lvlButtons[i])
            lvlButtons[i].visibility = View.VISIBLE
            lvlButtons[i].isClickable = true
        }
    }



    override fun onDrag(targetView: View, event: DragEvent): Boolean {
        val draggedView = event.localState as? View ?: return false

        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                // Возвращаем true только для нашего типа данных
                return event.clipDescription?.label == "ACCESS_CARD"
            }

            DragEvent.ACTION_DRAG_ENTERED -> {
                targetView.alpha = 0.7f
            }

            DragEvent.ACTION_DRAG_EXITED -> {
                targetView.alpha = 1f
            }

            DragEvent.ACTION_DROP -> {
                targetView.alpha = 1f
                val cardNumber = event.clipData.getItemAt(0).text.toString().toInt()

                if (targetView == binding.ivCardReader) {
                    onCardInserted(cardNumber, draggedView)
                } else {
                    returnToOriginalPosition(draggedView)
                }
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                // event.result == false: дроп не был принят никем (мимо)
                if (!event.result) {
                    returnToOriginalPosition(draggedView)
                } else {
                    isDragging = false
                }
            }
        }
        return true
    }

    private fun onCardInserted(cardNumber: Int, cardView: View) {
        currAccessLevel = loadManager.updateAccessLvl(cardNumber)
        soundManager.playSound(SoundEffect.ACCESS_CARD_INSERT)
        cardView.visibility = View.VISIBLE
        unlockLvls()
    }

    private fun startCircleAnimation() {
        longPressAnimator?.cancel()
        longPressCircle.progress = 0f
        longPressCircle.isActive = true  // Трек появляется сразу

        longPressAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = ViewConfiguration.getLongPressTimeout().toLong()
            interpolator = LinearInterpolator()
            addUpdateListener { longPressCircle.progress = it.animatedValue as Float }
            start()
        }
    }

    private fun cancelCircleAnimation() {
        longPressAnimator?.cancel()
        longPressAnimator = null
        longPressCircle.isActive = false
        longPressCircle.progress = 0f
    }

    private fun returnToOriginalPosition(view: View) {
        isDragging = false
        cancelCircleAnimation()
        view.visibility = View.VISIBLE
        view.animate()
            .translationY(0f).scaleX(1f).scaleY(1f)
            .setDuration(150).start()
    }

}