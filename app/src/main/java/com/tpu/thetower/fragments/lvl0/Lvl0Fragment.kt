package com.tpu.thetower.fragments.lvl0

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tpu.thetower.AppPreferences
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl0Binding
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.managers.devicemanagers.FlashlightManager
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl0Binding.bind(view)
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


        if (lockPuzzleStatus == PuzzleStatus.COMPLETED.value) {
            binding.ivMain.setImageResource(R.drawable.lvl0_bd_solved)
            binding.btnToPuzzle1Lock.visibility = View.GONE
            if (!loadManager.isLevelCompleted(0)) {
                binding.btnLvlCompleted.visibility = View.VISIBLE
            }
        }

        if (flashlightPuzzleStatus == PuzzleStatus.COMPLETED.value
            || AppPreferences(requireContext()).isMaxAccessLvl) {
            binding.ivDarkness.visibility = View.GONE
            binding.ivDarknessFlashlight.visibility = View.GONE
            binding.btnLightOn.visibility = View.GONE
            binding.ivBlack.visibility = View.GONE
            enableButtons()
            return
        }

        if (flashlightPuzzleStatus == PuzzleStatus.LOCKED.value) {
            startAwakeningAnim()
        }

    }

    private fun enableButtons() {
        binding.btnToElevator.visibility = View.VISIBLE
        binding.btnToPuzzle1.visibility = View.VISIBLE
        if (loadManager.getPuzzleStatus(0, "lock") != PuzzleStatus.COMPLETED.value)
            binding.btnToPuzzle1Lock.visibility = View.VISIBLE
    }

    private fun setListeners() {
        binding.btnToElevator.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_global_elevatorFragment)
            UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
            soundManager.playSound(SoundEffect.ELEVATOR_DOOR)
        }

        binding.btnToPuzzle1Lock.setOnClickListener {
            if (loadManager.getCurrentDialogIndex(0, "safe") == 0)
                dialogManager.startDialog(requireActivity(), "lvl0_safe")
            FragmentNavigation.changeBG(this, R.id.action_lvl0Fragment_to_lvl0PuzzleLockFragment)
            UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
        }

        binding.btnToPuzzle1.setOnClickListener {
            binding.ivPuzzle1.visibility = View.VISIBLE
            binding.ivClick.visibility = View.VISIBLE
            if (loadManager.getCurrentDialogIndex(0, "shapes_paper") == 0)
                dialogManager.startDialog(requireActivity(), "lvl0_box")
            soundManager.playSound(SoundEffect.DRAWER_OPENING)
        }

        binding.btnLvlCompleted.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl0Fragment_to_lvl0CompletedFragment)
        }

        binding.ivClick.setOnClickListener {
            binding.ivPuzzle1.visibility = View.GONE
            binding.ivClick.visibility = View.GONE
            soundManager.playSound(SoundEffect.DRAWER_CLOSING)
        }

        binding.ivDarkness.setOnClickListener {
            dialogManager.startDialog(requireActivity(), "lvl0_dark")
        }

        binding.btnLightOn.setOnClickListener {
            binding.ivDarknessFlashlight.visibility = View.GONE
            binding.btnLightOn.visibility = View.GONE
            dialogManager.startDialog(requireActivity(), "lvl0_after_light")
            flashlightManager?.toggleFlashlight(false)
            flashlightManager?.stopMonitoring()
            saveRepo.savePuzzleStatus(0, "flashlight", status = PuzzleStatus.COMPLETED.value)
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
            saveRepo.savePuzzleStatus(0, "flashlight", status = PuzzleStatus.IN_PROGRESS.value)
            binding.ivDarkness.visibility = View.GONE

        } else if (!isFlashlightOn && currentStatus == PuzzleStatus.IN_PROGRESS.value) {
            soundManager.playSound(SoundEffect.FLASHLIGHT)
            binding.ivDarkness.visibility = View.VISIBLE
            saveRepo.savePuzzleStatus(0, "flashlight", status = PuzzleStatus.LOCKED.value)
        }
    }

    private fun startAwakeningAnim() {
        binding.ivBlack.animate()
            .alpha(0f)
            .setDuration(3000)
            .withEndAction {
                binding.ivBlack.visibility = View.GONE
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
