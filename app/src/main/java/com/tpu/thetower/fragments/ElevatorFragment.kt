package com.tpu.thetower.fragments

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.DragShadowBuilder
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.LevelAccessManager
import com.tpu.thetower.LoadManager
import com.tpu.thetower.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.SoundManager
import com.tpu.thetower.databinding.FragmentElevatorBinding

class ElevatorFragment : Fragment(R.layout.fragment_elevator), View.OnTouchListener, View.OnDragListener {

    private lateinit var binding : FragmentElevatorBinding
    private lateinit var soundManager: SoundManager
    private lateinit var saveManager: SaveManager
    private lateinit var musicManager: MusicManager

    private lateinit var ivBg: ImageView
    private lateinit var ivPanel: ImageView
    private lateinit var ivBgBlurred: ImageView
    private lateinit var ivOpenedPanel: ImageView
    private lateinit var ivCardReader: ImageView
    private lateinit var clPanel: ConstraintLayout
    private lateinit var ivDraggable: ImageView

    private lateinit var btnToLvl0: Button
    private lateinit var btnToLvl1: Button
    private lateinit var btnToLvl2: Button
    private lateinit var btnToLvl3: Button
    private lateinit var btnToLvl4: Button
    private lateinit var btnToLvl5: Button
    private lateinit var btnToLvl6: Button

    private lateinit var lvlButtons : List<Button>
    private lateinit var lvlActions: List<Int>

    private lateinit var originalPosition: Pair<Float, Float>
    private var currAccessLevel: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentElevatorBinding.bind(view)
        bindView()
        setListeners()

        val receivedData = arguments?.getString("saved_level")
        if (receivedData != null) {
            FragmentManager.changeBG(this, receivedData.toInt())
            arguments = Bundle()
        }
        soundManager = SoundManager.getInstance()
        saveManager = SaveManager.getInstance()
        musicManager = MusicManager.getInstance()

        musicManager.stopMusic()

        ivDraggable.post {
            originalPosition = Pair(ivDraggable.x, ivDraggable.y)
        }

        // TODO WARNING!!! ВНИМАНИЕ!!! ДАЛЬШЕ КОСТЫЛЬ
        if (LoadManager.getAccessLevel(requireActivity()) != 0)
            FragmentManager.changeAccessCardImg(this, LevelAccessManager.getCardImage())

        // TODO Также тут можно "достать" карту доступа из пустоты, если попробовать перетащить. Вроде баг, надо фиксить
    }

    private fun bindView() {
        ivBg = binding.ivBg
        ivPanel = binding.ivPanel
        ivBgBlurred = binding.ivBgBlurred
        ivOpenedPanel = binding.ivOpenedPanel
        ivCardReader = binding.ivCardReader
        clPanel = binding.clPanel
        ivDraggable = binding.ivDraggable

        btnToLvl0 = binding.btnElevatorToLvl0
        btnToLvl1 = binding.btnElevatorToLvl1
        btnToLvl2 = binding.btnElevatorToLvl2
        btnToLvl3 = binding.btnElevatorToLvl3
        btnToLvl4 = binding.btnElevatorToLvl4
        btnToLvl5 = binding.btnElevatorToLvl5
        btnToLvl6 = binding.btnElevatorToLvl6

        lvlButtons = listOf(btnToLvl0, btnToLvl1, btnToLvl2, btnToLvl3, btnToLvl4, btnToLvl5, btnToLvl6)
        lvlActions = listOf(
            R.id.action_elevatorFragment_to_lvl0Fragment,
            R.id.action_elevatorFragment_to_lvl1Fragment,
            R.id.action_elevatorFragment_to_lvl2Fragment,
            R.id.action_elevatorFragment_to_lvl3Fragment,
            R.id.action_elevatorFragment_to_lvl4Fragment,
            R.id.action_elevatorFragment_to_lvl5Fragment,
            R.id.action_elevatorFragment_to_lvl6Fragment

        )
    }

    private fun setListeners() {

        ivPanel.setOnClickListener {
            ivBgBlurred.visibility = View.VISIBLE
            ivOpenedPanel.visibility = View.VISIBLE
            clPanel.visibility = View.VISIBLE
            ivPanel.visibility = View.GONE
            ivCardReader.visibility = View.GONE
            FragmentManager.hideGoBackArrow(requireActivity())

            unlockLvls(currAccessLevel)
        }

        ivBgBlurred.setOnClickListener {
            ivBgBlurred.visibility = View.GONE
            ivOpenedPanel.visibility = View.GONE
            clPanel.visibility = View.GONE
            ivPanel.visibility = View.VISIBLE
            ivCardReader.visibility = View.VISIBLE
            FragmentManager.showGoBackArrow(requireActivity())
        }

        lvlButtons.forEach {
            it.setOnClickListener {
                if (it.isVisible) {
                    soundManager.release()
                    FragmentManager.changeBG(this, lvlActions[lvlButtons.indexOf(it)])
                    FragmentManager.showGoBackArrow(requireActivity())
                }
            }
        }

        ivCardReader.setOnDragListener(this@ElevatorFragment)
        ivDraggable.setOnTouchListener(this@ElevatorFragment)


        //TODO Разобраться в необходимости кода

//        requireActivity().supportFragmentManager
//            .setFragmentResultListener("moduleUnlocking", viewLifecycleOwner) { _, bundle ->
//                val currAccessLevel = bundle.getInt("currAccessLevel")
//                unlockLvls(currAccessLevel)
//            }
    }

    private fun unlockLvls(currAccessLevel : Int) {
        val topUnlockingLvl = LevelAccessManager.topUnlockedLvlsForModules[currAccessLevel]
        val unlockingLvls = (0..topUnlockingLvl)
        unlockingLvls.forEach { lvlButtons[it].visibility = View.VISIBLE }
        //TODO Обновить дизайн панели уроавления
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        return if (event?.action == MotionEvent.ACTION_DOWN) {
            view?.visibility = View.INVISIBLE
            FragmentManager.changeAccessCardImg(this, 0)


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
                if (targetView == ivCardReader) {
                    placeViewInZone(draggedView, targetView)
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

    private fun placeViewInZone(view: View, zone: View) {
        view.apply {
            x = zone.x + (zone.width - width) / 2f
            y = zone.y + (zone.height - height) / 2f
//            visibility = View.VISIBLE
        }
        FragmentManager.changeAccessCardImg(this, LevelAccessManager.getCardImage())
        currAccessLevel = LoadManager.getAccessLevel(requireActivity())
    }

    private fun returnToOriginalPosition(view: View) {
        FragmentManager.changeAccessCardImg(this, LevelAccessManager.getCardImage())
        view.x = originalPosition.first
        view.y = originalPosition.second
        view.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()

        FragmentManager.changeAccessCardImg(this, 0)
    }
}