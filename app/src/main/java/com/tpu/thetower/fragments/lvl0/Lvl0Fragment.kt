package com.tpu.thetower.fragments.lvl0

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.databinding.FragmentLvl0Binding
import com.tpu.thetower.devicemanagers.FlashlightManager
import com.tpu.thetower.managers.UiVisibilityController


class Lvl0Fragment : Fragment(R.layout.fragment_lvl0), Hintable {

    private lateinit var binding: FragmentLvl0Binding

    private lateinit var flashlightManager: FlashlightManager
    private lateinit var musicManager: MusicManager
    private lateinit var soundManager: SoundManager
    private val saveRepo: SaveRepository = SaveRepository.getInstance()
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
        handleSounds()
    }

    private fun initManagers() {
        UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)

        hintManager = HintManager(
            listOf("lvl0_puzzle0_hint1", "lvl0_puzzle0_hint2"),
            LoadManager.getPuzzleUsedHintsCount(requireActivity(), 0, "flashlight"),
            0,
            "flashlight"
        )
    }

    private fun setupInitialState() {
        val flashlightStatus = LoadManager.getPuzzleStatus(requireActivity(), 0, "flashlight")
        val lockStatus = LoadManager.getPuzzleStatus(requireActivity(), 0, "lock")

        if (flashlightStatus == "locked") {
            startAwakeningAnim()
        }

        if (flashlightStatus == "completed") {
            ivDarkness.visibility = View.GONE
            ivDarknessFlashlight.visibility = View.GONE
            btnLightOn.visibility = View.GONE
            ivBlack.visibility = View.GONE
            enableButtons()
        }

        if (lockStatus == "completed") {
            ivMain.setImageResource(R.drawable.lvl0_bd_solved)
            btnToPuzzle1Lock.visibility = View.GONE
            if (!LoadManager.getLevelStatus(requireActivity(), 0)) {
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
            soundManager.playSound(R.raw.sound_of_an_elevator_door_opening)
        }

        btnToPuzzle1Lock.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl0Fragment_to_lvl0PuzzleLockFragment)
            UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
        }

        btnToPuzzle1.setOnClickListener {
            ivPuzzle1.visibility = View.VISIBLE
            ivClick.visibility = View.VISIBLE
            DialogManager.startDialog(requireActivity(), "lvl0_puzzle1")
            soundManager.playSound(R.raw.sound_of_drawer_opening)
        }

        btnLvlCompleted.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl0Fragment_to_lvl0CompletedFragment)
        }

        ivClick.setOnClickListener {
            ivPuzzle1.visibility = View.GONE
            ivClick.visibility = View.GONE
            soundManager.playSound(R.raw.sound_of_drawer_closing)
        }

        ivDarkness.setOnClickListener {
            DialogManager.startDialog(requireActivity(), "lvl0_dark")
            //            // Тестирование !!!
                    flashlightManager.toggleFlashlight(true)
        }

        btnLightOn.setOnClickListener {
            ivDarknessFlashlight.visibility = View.GONE
            btnLightOn.visibility = View.GONE
            DialogManager.startDialog(requireActivity(), "lvl0_light_on")
            flashlightManager.toggleFlashlight(false)
            flashlightManager.stopMonitoring()
            saveRepo.savePuzzleData(requireActivity(), 0, "flashlight", status = "completed")
            enableButtons()
            soundManager.playSound(R.raw.sound_of_light_switch)
        }

        flashlightManager = FlashlightManager(requireContext()) { isFlashlightOn ->
            requireActivity().runOnUiThread {
                handleFlashlightStateChanged(isFlashlightOn)
            }
        }
    }

    private fun handleFlashlightStateChanged(isFlashlightOn: Boolean) {
        val currentStatus = LoadManager.getPuzzleStatus(requireActivity(), 0, "flashlight")

        if (isFlashlightOn && currentStatus == "locked") {
            soundManager.playSound(R.raw.sound_of_a_flashlight)
            DialogManager.startDialog(requireActivity(), "lvl0_flashlight_on")
            saveRepo.savePuzzleData(
                requireActivity(),
                0,
                "flashlight",
                status = "in_progress"
            )
            ivDarkness.visibility = View.GONE
        } else if (!isFlashlightOn && currentStatus == "in_progress") {
            soundManager.playSound(R.raw.sound_of_a_flashlight)
            ivDarkness.visibility = View.VISIBLE
            saveRepo.savePuzzleData(
                requireActivity(),
                0,
                "flashlight",
                status = "locked"
            )
        }
    }

    private fun startAwakeningAnim() {
        ivBlack.animate()
            .alpha(0f)
            .setDuration(3000)
            .withEndAction {
                ivBlack.visibility = View.GONE
                flashlightManager.startMonitoring()
                DialogManager.startDialog(requireActivity(), "lvl0_start")
            }
            .start()
    }

    private fun handleSounds() {
        musicManager = MusicManager.getInstance()
        soundManager = SoundManager.getInstance()
        soundManager.init()
        soundManager.loadSound(
            requireContext(),
            listOf(
                R.raw.sound_of_a_flashlight,
                R.raw.sound_of_an_elevator_door_opening,
                R.raw.sound_of_drawer_opening,
                R.raw.sound_of_drawer_closing,
                R.raw.sound_of_light_switch
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        flashlightManager.toggleFlashlight(false)
        flashlightManager.stopMonitoring()
        saveRepo.savePuzzleData(requireActivity(), 0, "flashlight", status = "locked")
    }

    override fun onResume() {
        super.onResume()

        musicManager.playMusic(requireContext(), R.raw.soundtrack_2)
        saveRepo.saveCurrentLevel(requireActivity(), 0)
    }


    override fun useHint() {
        val flashlightStatus = LoadManager.getPuzzleStatus(requireActivity(), 0, "flashlight")

        if (flashlightStatus == "in_progress") {
            hintManager.useHint(requireActivity())
        } else {
            if (LoadManager.isLevelCompleted(requireActivity(), 0)) {
                DialogManager.startDialog(requireActivity(), "no_hints")
            } else {
                DialogManager.startDialog(requireActivity(), "lvl0_to_puzzle1_hint")
            }
        }
    }
}
