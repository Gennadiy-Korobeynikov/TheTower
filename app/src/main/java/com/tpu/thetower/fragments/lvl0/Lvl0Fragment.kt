package com.tpu.thetower.fragments.lvl0

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl0Binding
import com.tpu.thetower.devicemanagers.FlashlightManager
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
class Lvl0Fragment : Fragment(R.layout.fragment_lvl0), Hintable {

    private lateinit var binding: FragmentLvl0Binding

    private var flashlightManager: FlashlightManager? = null

    @Inject lateinit var musicManager: MusicManager
    @Inject lateinit var soundManager: SoundManager
    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var dialogManager: DialogManager
    @Inject lateinit var hintManagerFactory: HintManager.Factory

    private lateinit var hintManager: HintManager

    private lateinit var btnToElevator: Button
    private lateinit var btnToPuzzle1: Button
    private lateinit var btnToPuzzle1Lock: Button
    private lateinit var btnLightOn: ImageButton
    private lateinit var btnLvlCompleted: Button

    private lateinit var ivDarkness: ImageView
    private lateinit var ivDarknessFlashlight: ImageView
    private lateinit var ivBlack: ImageView
    private lateinit var ivPuzzle1: ImageView
    private lateinit var ivClick: ImageView
    private lateinit var ivMain: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl0Binding.bind(view)
        bindView()
        initManagers()
        setupInitialState()
        setListeners()
    }

    private fun initManagers() {
        UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)

        hintManager = hintManagerFactory.create(
            listOf("lvl0_puzzle0_hint1", "lvl0_puzzle0_hint2"),
            0,
            "flashlight"
        )
    }

    private fun setupInitialState() {
        val flashlightPuzzleStatus = loadManager.getPuzzleStatus(0, "flashlight")
        val lockPuzzleStatus = loadManager.getPuzzleStatus(0, "lock")

        if (flashlightPuzzleStatus == PuzzleStatus.LOCKED.value) {
            startAwakeningAnim()
        }

        if (flashlightPuzzleStatus == PuzzleStatus.COMPLETED.value) {
            ivDarkness.visibility = View.GONE
            ivDarknessFlashlight.visibility = View.GONE
            btnLightOn.visibility = View.GONE
            ivBlack.visibility = View.GONE
            enableButtons()
        }

        if (lockPuzzleStatus == PuzzleStatus.COMPLETED.value) {
            ivMain.setImageResource(R.drawable.lvl0_bd_solved)
            btnToPuzzle1Lock.visibility = View.GONE
            if (!loadManager.isLevelCompleted(0)) {
                btnLvlCompleted.visibility = View.VISIBLE
            }
        }
    }

    private fun enableButtons() {
        btnToElevator.visibility = View.VISIBLE
        btnToPuzzle1.visibility = View.VISIBLE
        btnToPuzzle1Lock.visibility = View.VISIBLE
    }

    private fun bindView() {
        btnToElevator = binding.btnToElevator
        btnToPuzzle1 = binding.btnToPuzzle1
        btnToPuzzle1Lock = binding.btnToPuzzle1Lock
        btnLvlCompleted = binding.btnLvlCompleted
        ivDarkness = binding.ivDarkness
        ivDarknessFlashlight = binding.ivDarknessFlashlight
        ivBlack = binding.ivBlack
        ivPuzzle1 = binding.ivPuzzle1
        btnLightOn = binding.btnLightOn
        ivClick = binding.ivClick
        ivMain = binding.ivMain
    }

    private fun setListeners() {
        btnToElevator.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_global_elevatorFragment)
            UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
            soundManager.playSound(SoundEffect.ELEVATOR_DOOR)
        }

        btnToPuzzle1Lock.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl0Fragment_to_lvl0PuzzleLockFragment)
            UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
        }

        btnToPuzzle1.setOnClickListener {
            ivPuzzle1.visibility = View.VISIBLE
            ivClick.visibility = View.VISIBLE
            if (loadManager.getCurrentDialog(0, "shapes_paper") == 0)
                dialogManager.startDialog(requireActivity(), "lvl0_puzzle1")
            soundManager.playSound(SoundEffect.DRAWER_OPENING)
        }

        btnLvlCompleted.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl0Fragment_to_lvl0CompletedFragment)
        }

        ivClick.setOnClickListener {
            ivPuzzle1.visibility = View.GONE
            ivClick.visibility = View.GONE
            soundManager.playSound(SoundEffect.DRAWER_CLOSING)
        }

        ivDarkness.setOnClickListener {
            dialogManager.startDialog(requireActivity(), "lvl0_dark")
        }

        btnLightOn.setOnClickListener {
            ivDarknessFlashlight.visibility = View.GONE
            btnLightOn.visibility = View.GONE
            dialogManager.startDialog(requireActivity(), "lvl0_light_on")
            flashlightManager?.toggleFlashlight(false)
            flashlightManager?.stopMonitoring()
            saveRepo.savePuzzleData(0, "flashlight", status = PuzzleStatus.COMPLETED.value)
            enableButtons()
            soundManager.playSound(SoundEffect.LIGHT_SWITCH)
        }

        flashlightManager = FlashlightManager(requireContext()) { isFlashlightOn ->
            requireActivity().runOnUiThread {
                handleFlashlightStateChanged(isFlashlightOn)
            }
        }
    }

    private fun handleFlashlightStateChanged(isFlashlightOn: Boolean) {
        val currentStatus = loadManager.getPuzzleStatus(0, "flashlight")

        if (isFlashlightOn && currentStatus == PuzzleStatus.LOCKED.value) {
            soundManager.playSound(SoundEffect.FLASHLIGHT)
            dialogManager.startDialog(requireActivity(), "lvl0_flashlight_on")
            saveRepo.savePuzzleData(0, "flashlight", status = PuzzleStatus.IN_PROGRESS.value)
            ivDarkness.visibility = View.GONE

        } else if (!isFlashlightOn && currentStatus == PuzzleStatus.IN_PROGRESS.value) {
            soundManager.playSound(SoundEffect.FLASHLIGHT)
            ivDarkness.visibility = View.VISIBLE
            saveRepo.savePuzzleData(0, "flashlight", status = PuzzleStatus.LOCKED.value)
        }
    }

    private fun startAwakeningAnim() {
        ivBlack.animate()
            .alpha(0f)
            .setDuration(3000)
            .withEndAction {
                ivBlack.visibility = View.GONE
                flashlightManager?.startMonitoring()
                dialogManager.startDialog(requireActivity(), "lvl0_start")
            }
            .start()
    }

    override fun useHint() {
        val flashlightStatus = loadManager.getPuzzleStatus(0, "flashlight")

        if (flashlightStatus == PuzzleStatus.LOCKED.value) {
            hintManager.useHint(requireActivity())
            return
        }
        if (flashlightStatus == PuzzleStatus.IN_PROGRESS.value) {
            dialogManager.startDialog(requireActivity(), "lvl0_click_switch")
            return
        }
        val dialogKey = if (loadManager.isLevelCompleted(0)) "no_hints" else "lvl0_to_puzzle1_hint"
        dialogManager.startDialog(requireActivity(), dialogKey)
    }

    override fun skipPuzzle() {
        handleFlashlightStateChanged(true)
    }

    override fun onDestroyView() {
        flashlightManager?.toggleFlashlight(false)
        flashlightManager?.stopMonitoring()
        flashlightManager = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()

        musicManager.playMusic(R.raw.soundtrack_2)
        saveRepo.saveCurrentLevel(0)
    }
}
