package com.tpu.thetower.fragments.lvl3

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
import com.tpu.thetower.managers.LevelAccessManager
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
class Lvl3Fragment : Fragment(R.layout.fragment_lvl3), View.OnTouchListener, View.OnDragListener, Hintable {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLvl3Binding.bind(view)

        setListeners()

        hintManager = hintManagerFactory.create(
            hints = listOf("lvl3_to_puzzle0_hint1", "lvl3_to_puzzle0_hint2"),
            level = 3,
            puzzle = "sleeping pills"
        )

        UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)

        binding.ivDraggable.post {
            originalPosition = Pair(binding.ivDraggable.x, binding.ivDraggable.y)
        }

        if (loadManager.getPuzzleStatus(3, "donuts") == PuzzleStatus.LOCKED.value) {
            dialogManager.startDialog(requireActivity(), "lvl3_npc_security")
            saveRepo.savePuzzleData(3, "donuts", status = PuzzleStatus.IN_PROGRESS.value)
        }

        if (loadManager.getPuzzleStatus(3, "buttons") == PuzzleStatus.COMPLETED.value) {
            binding.btnToPuzzle0.visibility = View.GONE
            binding.btnToPuzzle1.visibility = View.GONE
            if (loadManager.getPuzzleStatus(3, "sleeping pills") != PuzzleStatus.COMPLETED.value) {
                binding.ivDraggable.visibility = View.VISIBLE
                binding.ivBg.setImageResource(R.drawable.lvl3_bg_no_sleeping_pills)
            }
        }

        if (loadManager.getPuzzleStatus(3, "sleeping pills") == PuzzleStatus.COMPLETED.value) {
            hintManager = hintManagerFactory.create(
                hints = listOf("lvl3_to_coffee_hint1"),
                level = 3,
                puzzle = "sleeping pills"
            )
            binding.ivBg.setImageResource(R.drawable.lvl3_bg_guard_sleeping)
            UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
            binding.ivTarget.visibility = View.GONE
            binding.btnToMap.visibility = View.VISIBLE
        }

        if (loadManager.getPuzzleStatus(3, "vacuum cleaner") == PuzzleStatus.COMPLETED.value) {
            binding.ivBg.setImageResource(R.drawable.lvl3_bg_hoover_with_key)
            binding.btnKey.visibility = View.VISIBLE
            binding.btnToPuzzle3.visibility = View.GONE
            binding.btnToMap.visibility = View.GONE
        }

        if (loadManager.getPuzzleStatus(3, "key") == PuzzleStatus.COMPLETED.value) {
            binding.ivBg.setImageResource(R.drawable.lvl3_bg_last)
            binding.btnToPuzzle4Lock.visibility = View.GONE
            binding.btnToPuzzle4.visibility = View.GONE
            binding.btnToAccessCard.visibility = View.VISIBLE
        }
    }

    private fun setListeners() {
        binding.btnToPuzzle0.setOnClickListener {
            if (loadManager.getPuzzleStatus(3, "donuts after shaking") == PuzzleStatus.LOCKED.value) {
                dialogManager.startDialog(requireActivity(), "lvl3_donuts")
                saveRepo.savePuzzleData(3, "donuts after shaking", status = PuzzleStatus.IN_PROGRESS.value)
            }
            FragmentNavigation.changeBG(this, R.id.action_lvl3Fragment_to_lvl3PuzzleDonutsFragment)
        }

        binding.btnToPuzzle1.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl3Fragment_to_lvl3PuzzleButtonsFragment)
        }

        binding.btnToPuzzle3.setOnClickListener {
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

        binding.btnKey.setOnClickListener {
            binding.ivBg.setImageResource(R.drawable.lvl3_bg_hoover_no_key)
            binding.btnKey.visibility = View.GONE
            binding.btnToPuzzle4Lock.visibility = View.VISIBLE
        }

        binding.btnToPuzzle4.setOnClickListener {
            if (loadManager.getPuzzleStatus(3, "sleeping pills") != PuzzleStatus.COMPLETED.value) {
                dialogManager.startDialog(requireActivity(), "lvl3_computer")
            } else {
                FragmentNavigation.changeBG(this, R.id.action_lvl3Fragment_to_lvl3PuzzleEditorFragment)
            }
        }

        binding.btnToPuzzle4Lock.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl3Fragment_to_lvl3PuzzleKeyFragment)
        }

        binding.btnToAccessCard.setOnClickListener {
            binding.ivAccessCard.visibility = View.VISIBLE
            LevelAccessManager.changeAccessCardNumber(saveRepo, 4)
        }

        binding.ivAccessCard.setOnClickListener {
            binding.ivAccessCard.visibility = View.GONE
            binding.btnToAccessCard.visibility = View.GONE
        }

        binding.ivTarget.setOnDragListener(this@Lvl3Fragment)
        binding.ivDraggable.setOnTouchListener(this@Lvl3Fragment)
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        return if (event?.action == MotionEvent.ACTION_DOWN) {
            view?.visibility = View.INVISIBLE

            val data = ClipData.newPlainText("", "")
            val shadowBuilder = DragShadowBuilder(view)
            view?.startDragAndDrop(data, shadowBuilder, view, 0)
            true
        } else false
    }

    override fun onDrag(targetView: View, event: DragEvent?): Boolean {
        val draggedView = event?.localState as? View ?: return false

        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED,
            DragEvent.ACTION_DRAG_ENTERED,
            DragEvent.ACTION_DRAG_LOCATION,
            DragEvent.ACTION_DRAG_EXITED -> return true

            DragEvent.ACTION_DROP -> {
                if (targetView == _binding?.ivTarget) {
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
        binding.ivBg.setImageResource(R.drawable.lvl3_bg_guard_sleeping)
        binding.ivTarget.visibility = View.GONE
        binding.btnToMap.visibility = View.VISIBLE
        saveRepo.savePuzzleData(3, "sleeping pills")
        soundManager.playSound(SoundEffect.GUARD_SNORING, repeat = -1)
    }

    private fun returnToOriginalPosition(view: View) {
        view.x = originalPosition.first
        view.y = originalPosition.second
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
    }
}