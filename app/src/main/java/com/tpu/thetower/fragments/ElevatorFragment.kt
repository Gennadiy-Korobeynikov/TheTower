package com.tpu.thetower.fragments

import android.annotation.SuppressLint
import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.DragShadowBuilder
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
        // TODO Также тут можно "достать" карту доступа из пустоты, если попробовать перетащить. Вроде баг, надо фиксить
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {

        binding.ivPanel.setOnClickListener {
            binding.ivBgBlurred.visibility = View.VISIBLE
            binding.ivOpenedPanel.visibility = View.VISIBLE
            binding.clPanel.visibility = View.VISIBLE
            binding.ivPanel.visibility = View.GONE
            binding.ivCardReader.visibility = View.GONE
            UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)

            unlockLvls()
        }

        binding.ivBgBlurred.setOnClickListener {
            binding.ivBgBlurred.visibility = View.GONE
            binding.ivOpenedPanel.visibility = View.GONE
            binding.clPanel.visibility = View.GONE
            binding.ivPanel.visibility = View.VISIBLE
            binding.ivCardReader.visibility = View.VISIBLE
            UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
        }

        lvlButtons.forEachIndexed { index, btn ->
            btn.setOnClickListener {
                if (btn !in openedLvlButtons) return@setOnClickListener

                if (btn == binding.btnElevatorToLvl2 &&
                    !prefs.isMaxAccessLvl &&
                    !loadManager.isLevelCompleted(1)
                ) {
                    dialogManager.startDialog(requireActivity(), "lvl1_elevator")
                    return@setOnClickListener
                }

                FragmentNavigation.changeBG(this, lvlActions[index])
                UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
                soundManager.playSound(SoundEffect.ELEVATOR_DOOR)
                soundManager.playSound(SoundEffect.STEPS)
            }
        }


        binding.ivAccessCardDraggable.setOnTouchListener { v, event ->
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

                MotionEvent.ACTION_UP -> {
                    v.animate()
                        .translationY(0f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                    false
                }

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

        binding.ivAccessCardDraggable.setOnLongClickListener { view ->

            if (loadManager.getCurrentAccessCardNumber() == 0) {
                return@setOnLongClickListener false
            }

            val clipData = ClipData.newPlainText(
                "ACCESS_CARD",
                loadManager.getCurrentAccessCardNumber().toString()
            )

            val shadow = DragShadowBuilder(view)

            view.startDragAndDrop(clipData, shadow, view, 0)

            view.visibility = View.INVISIBLE
            true
        }
        binding.ivCardReader.setOnDragListener(this@ElevatorFragment)
    }

    private fun unlockLvls() {
        val unlockingLvls = (0..currAccessLevel)
        unlockingLvls.forEach { i ->
            openedLvlButtons.add(lvlButtons[i])
            lvlButtons[i].setBackgroundResource(android.R.color.transparent)
            lvlButtons[i].isClickable = true
        }
    }



    override fun onDrag(targetView: View, event: DragEvent): Boolean {
        val draggedView = event.localState as? View ?: return false

        when (event.action) {

            DragEvent.ACTION_DRAG_STARTED -> {
                event.clipDescription?.label == "ACCESS_CARD"
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

                val cardNumber = event.clipData
                    .getItemAt(0)
                    .text
                    .toString()
                    .toInt()

                if (targetView == binding.ivCardReader) {
                    onCardInserted(cardNumber, draggedView)
                } else {
                    returnToOriginalPosition(draggedView)
                }
                true
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                if (!event.result) {
                    returnToOriginalPosition(draggedView)
                }
                true
            }
            else -> true
        }
        return true
    }

    private fun onCardInserted(cardNumber: Int, cardView: View) {
        currAccessLevel = loadManager.updateAccessLvl(cardNumber)
        soundManager.playSound(SoundEffect.ACCESS_CARD_INSERT)
        cardView.visibility = View.VISIBLE
    }

    private fun returnToOriginalPosition(view: View) {
        view.x = originalPosition.first
        view.y = originalPosition.second
        view.visibility = View.VISIBLE
    }

}