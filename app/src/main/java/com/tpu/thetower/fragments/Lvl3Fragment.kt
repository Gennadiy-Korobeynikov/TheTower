package com.tpu.thetower.fragments

import android.annotation.SuppressLint
import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.DragShadowBuilder
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.tpu.thetower.DialogManager
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.LoadManager
import com.tpu.thetower.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.SoundManager
import com.tpu.thetower.databinding.FragmentLvl3Binding

class Lvl3Fragment : Fragment(R.layout.fragment_lvl3), View.OnTouchListener, View.OnDragListener,
    Hintable {
    private lateinit var binding: FragmentLvl3Binding

    private lateinit var musicManager: MusicManager
    private lateinit var soundManager: SoundManager
    private lateinit var saveManager: SaveManager
    private lateinit var hintManager: HintManager

    private lateinit var btnToPuzzle0: Button
    private lateinit var btnToPuzzle1: Button
    private lateinit var btnToPuzzle3: Button
    private lateinit var btnToPuzzle4: Button
    private lateinit var btnToPuzzle4Lock: Button

    private lateinit var ivTarget: ImageView
    private lateinit var ivDraggable: ImageView
    private lateinit var ivCopy: ImageView

    private lateinit var originalPosition: Pair<Float, Float>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLvl3Binding.bind(view)
        bindView()
        setListeners()
        handleSounds()

        hintManager = HintManager(
            listOf("lvl3_to_puzzle0_hint1", "lvl3_to_puzzle0_hint2"),
            LoadManager.getPuzzleUsedHintsCount(requireActivity(), 3, "sleeping pills"),
            3, "sleeping pills"
        ) // 11 - подсыпка снотвороного
        FragmentManager.showGoBackArrow(requireActivity())

        ivDraggable.post {
            originalPosition = Pair(ivDraggable.x, ivDraggable.y)
        }

        if (LoadManager.getPuzzleStatus(requireActivity(), 3, "buttons") == "completed") {
            btnToPuzzle0.visibility = View.GONE
            btnToPuzzle1.visibility = View.GONE
            ivDraggable.visibility = View.VISIBLE
            FragmentManager.changeDragAndDropImg(this, R.drawable.ic_triangle_drag1)
        }

        if (LoadManager.getPuzzleStatus(requireActivity(), 3, "sleeping pills") == "completed") {
            hintManager = HintManager(
                listOf("lvl3_to_coffee_hint1"),
                LoadManager.getPuzzleUsedHintsCount(requireActivity(), 3, "sleeping pills"),
                3, "sleeping pills"
            )
            FragmentManager.showGoBackArrow(requireActivity())
        }

    }

    private fun bindView() {
        btnToPuzzle0 = binding.btnToPuzzle0
        btnToPuzzle1 = binding.btnToPuzzle1
        btnToPuzzle3 = binding.btnToPuzzle3
        btnToPuzzle4 = binding.btnToPuzzle4
        btnToPuzzle4Lock = binding.btnToPuzzle4Lock
        ivTarget = binding.ivTarget
        ivDraggable = binding.ivDraggable
        ivCopy = binding.ivCopy
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {

        btnToPuzzle0.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_lvl3Fragment_to_lvl3PuzzleDonutsFragment)
        }

        btnToPuzzle1.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_lvl3Fragment_to_lvl3PuzzleButtonsFragment)
        }

        btnToPuzzle3.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_lvl3Fragment_to_lvl3PuzzleHooverFragment)
        }

        btnToPuzzle4.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_lvl3Fragment_to_lvl3PuzzleEditorFragment)
        }

        btnToPuzzle4Lock.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_lvl3Fragment_to_lvl3PuzzleKeyFragment)
        }

        var longPressRunnable: Runnable? = null
        var isLongPressHandled = false

        btnToPuzzle4Lock.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    isLongPressHandled = false
                    longPressRunnable = Runnable {
                        ivCopy.visibility = View.VISIBLE
                        isLongPressHandled = true
                    }
                    view.postDelayed(longPressRunnable, 1000)
                    true
                }

                MotionEvent.ACTION_UP -> {
                    if (!isLongPressHandled) {
//                        FragmentManager.changeBG(this, R.id.action_lvl3Fragment_to_keyFragment)
                    }
                    longPressRunnable?.let { view.removeCallbacks(it) }
                    true
                }

                MotionEvent.ACTION_CANCEL -> {
                    longPressRunnable?.let { view.removeCallbacks(it) }
                    true
                }

                else -> false
            }

        }

        ivCopy.setOnClickListener {
            saveManager.savePuzzleData(requireContext(), 3, "lock model", status = "in_progress")
            ivCopy.visibility = View.GONE
        }

        ivTarget.setOnDragListener(this@Lvl3Fragment)
        ivDraggable.setOnTouchListener(this@Lvl3Fragment)

    }

    private fun handleSounds() {
        musicManager = MusicManager.getInstance()
        soundManager = SoundManager.getInstance()
        soundManager.init()
        soundManager.loadSound(
            requireContext(), listOf(
                R.raw.sound_of_a_flashlight,
                R.raw.sound_of_an_elevator_door_opening
            )
        )
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        return if (event?.action == MotionEvent.ACTION_DOWN) {
            view?.visibility = View.INVISIBLE
            FragmentManager.changeDragAndDropImg(this, 0)

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
                if (targetView == ivTarget) {
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
        saveManager.savePuzzleData(requireContext(), 3, "sleeping pills")
    }

    private fun returnToOriginalPosition(view: View) {
        FragmentManager.changeDragAndDropImg(this, R.drawable.ic_triangle_drag1)
        view.x = originalPosition.first
        view.y = originalPosition.second
        view.visibility = View.VISIBLE
    }


    override fun onResume() {
        super.onResume()

        saveManager = SaveManager.getInstance()
        saveManager.saveCurrentLevel(requireContext(), 3)
    }

    override fun useHint() {
        if (LoadManager.getPuzzleStatus(requireActivity(), 3, "sleeping pills") == "completed") // Охранник спит
            DialogManager.startDialog(requireActivity(), "hint_is_not_here")
        else
            hintManager.useHint(requireActivity())
    }


}