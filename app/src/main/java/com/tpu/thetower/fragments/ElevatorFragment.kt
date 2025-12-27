package com.tpu.thetower.fragments

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.DragShadowBuilder
import androidx.fragment.app.Fragment
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.LevelAccessManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.databinding.FragmentElevatorBinding
import com.tpu.thetower.managers.UiVisibilityController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ElevatorFragment : Fragment(R.layout.fragment_elevator), View.OnTouchListener,
    View.OnDragListener {

    private lateinit var binding: FragmentElevatorBinding

    @Inject lateinit var soundManager: SoundManager
    @Inject lateinit var musicManager: MusicManager
    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var dialogManager: DialogManager

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

    private lateinit var originalPosition: Pair<Float, Float>
    private var currAccessLevel: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentElevatorBinding.bind(view)
        setListeners()

        val receivedData = arguments?.getString("saved_level")
        if (receivedData != null) {
            FragmentNavigation.changeBG(this, receivedData.toInt())
            arguments = Bundle()
        }

        musicManager.stopMusic()

        binding.ivDraggable.post {
            originalPosition = Pair(binding.ivDraggable.x, binding.ivDraggable.y)
        }

        // TODO WARNING!!! ВНИМАНИЕ!!! ДАЛЬШЕ КОСТЫЛЬ
        if (loadManager.getAccessLevel() != 0) {
            binding.ivDraggable.visibility = View.VISIBLE
            binding.ivDraggable.setImageResource(LevelAccessManager.getCardImage())
        }
        // TODO Также тут можно "достать" карту доступа из пустоты, если попробовать перетащить. Вроде баг, надо фиксить
    }

    private fun setListeners() {

        binding.ivPanel.setOnClickListener {
            binding.ivBgBlurred.visibility = View.VISIBLE
            binding.ivOpenedPanel.visibility = View.VISIBLE
            binding.clPanel.visibility = View.VISIBLE
            binding.ivPanel.visibility = View.GONE
            binding.ivCardReader.visibility = View.GONE
            UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)

            unlockLvls(currAccessLevel)
        }

        binding.ivBgBlurred.setOnClickListener {
            binding.ivBgBlurred.visibility = View.GONE
            binding.ivOpenedPanel.visibility = View.GONE
            binding.clPanel.visibility = View.GONE
            binding.ivPanel.visibility = View.VISIBLE
            binding.ivCardReader.visibility = View.VISIBLE
            UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
        }

        val lvlButtons = listOf(
            binding.btnElevatorToLvl0,
            binding.btnElevatorToLvl1,
            binding.btnElevatorToLvl2,
            binding.btnElevatorToLvl3,
            binding.btnElevatorToLvl4,
            binding.btnElevatorToLvl5,
            binding.btnElevatorToLvl6
        )

        lvlButtons.forEach { btn ->
            btn.setOnClickListener {
                if (btn == binding.btnElevatorToLvl2 && btn in openedLvlButtons) {
                    if (!loadManager.isLevelCompleted(1)) {
                        dialogManager.startDialog(requireActivity(), "lvl1_elevator")
                    } else {
                        soundManager.release()
                        FragmentNavigation.changeBG(this, lvlActions[lvlButtons.indexOf(btn)])
                        UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
                    }
                } else if (btn in openedLvlButtons) {
                    soundManager.release()
                    FragmentNavigation.changeBG(this, lvlActions[lvlButtons.indexOf(btn)])
                    UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
                }
            }
        }

        binding.ivCardReader.setOnDragListener(this@ElevatorFragment)
        binding.ivDraggable.setOnTouchListener(this@ElevatorFragment)


        //TODO Разобраться в необходимости кода

//        requireActivity().supportFragmentManager
//            .setFragmentResultListener("moduleUnlocking", viewLifecycleOwner) { _, bundle ->
//                val currAccessLevel = bundle.getInt("currAccessLevel")
//                unlockLvls(currAccessLevel)
//            }
    }

    private fun unlockLvls(currAccessLevel: Int) {
        val lvlButtons = listOf(
            binding.btnElevatorToLvl0,
            binding.btnElevatorToLvl1,
            binding.btnElevatorToLvl2,
            binding.btnElevatorToLvl3,
            binding.btnElevatorToLvl4,
            binding.btnElevatorToLvl5,
            binding.btnElevatorToLvl6
        )

        val topUnlockingLvl = LevelAccessManager.topUnlockedLvlsForModules[currAccessLevel]
        val unlockingLvls = (0..topUnlockingLvl)
        unlockingLvls.forEach { i ->
            openedLvlButtons.add(lvlButtons[i])
            lvlButtons[i].setBackgroundResource(android.R.color.transparent)
            lvlButtons[i].isClickable = true
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

    override fun onDrag(targetView: View, event: DragEvent?): Boolean {
        val draggedView = event?.localState as? View ?: return false

        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED,
            DragEvent.ACTION_DRAG_ENTERED,
            DragEvent.ACTION_DRAG_LOCATION,
            DragEvent.ACTION_DRAG_EXITED -> return true

            DragEvent.ACTION_DROP -> {
                if (targetView == binding.ivCardReader) {
                    returnToOriginalPosition(draggedView)
                    currAccessLevel = loadManager.getAccessLevel()
                } else {
                    returnToOriginalPosition(draggedView)
                }
                return true
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                if (!event.result) {
                    returnToOriginalPosition(draggedView)
                }
                return true
            }
        }
        return false
    }

    private fun returnToOriginalPosition(view: View) {
        view.x = originalPosition.first
        view.y = originalPosition.second
        view.visibility = View.VISIBLE
    }

}