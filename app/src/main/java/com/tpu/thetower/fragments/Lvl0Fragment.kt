package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.tpu.thetower.DialogManager
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.LevelAccessManager
import com.tpu.thetower.LoadManager
import com.tpu.thetower.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.SoundManager
import com.tpu.thetower.databinding.FragmentLvl0Binding
import com.tpu.thetower.devicemanagers.FlashlightManager


class Lvl0Fragment : Fragment(R.layout.fragment_lvl0), Hintable {

    private lateinit var binding: FragmentLvl0Binding

    private lateinit var flashlightManager: FlashlightManager
    private lateinit var musicManager: MusicManager
    private lateinit var soundManager: SoundManager
    private lateinit var saveManager: SaveManager
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
    private lateinit var ivCard: ImageView
    private lateinit var ivBgBlurred: ImageView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl0Binding.bind(view)
        bindView()
        setListeners()
        handleSounds()
        FragmentManager.hideGoBackArrow(requireActivity())
        hintManager = HintManager(listOf("lvl0_puzzle0_hint1" , "lvl0_puzzle0_hint2"),
            LoadManager.getPuzzleUsedHintsCount(requireActivity(),0,"flashlight"),
            0,"flashlight")

        saveManager = SaveManager.getInstance()

        if (LoadManager.getPuzzleStatus(requireActivity(), 0, "flashlight") == "in_progress") {
            startAwakeningAnim()
            saveManager.savePuzzleData(requireContext(), 0, "flashlight", status = "in_progress")
        }

        if (LoadManager.getPuzzleStatus(requireActivity(), 0, "flashlight") == "completed") {
            ivDarkness.visibility = View.GONE
            ivDarknessFlashlight.visibility = View.GONE
            btnLightOn.visibility = View.GONE
            ivBlack.visibility = View.GONE
            enableButtons()
        }

        if (LoadManager.getPuzzleStatus(requireActivity(), 0, "lock") == "completed") {
            ivMain.setImageResource(R.drawable.lvl0_bd_solved)
            btnToPuzzle1Lock.visibility = View.GONE
            btnToPuzzle1Lock.visibility = View.GONE
            if (!LoadManager.getLevelStatus(requireActivity(), 0))
                btnLvlCompleted.visibility = View.VISIBLE
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
        ivCard = binding.ivCard
        ivBgBlurred = binding.ivBgBlurred
    }

    private fun setListeners() {

        btnToElevator.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_global_elevatorFragment)
            FragmentManager.showGoBackArrow(requireActivity())
            soundManager.playSound(R.raw.sound_of_an_elevator_door_opening)
        }

        btnToPuzzle1Lock.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_lvl0Fragment_to_lvl0PuzzleLockFragment)
            FragmentManager.showGoBackArrow(requireActivity())
        }

        btnToPuzzle1.setOnClickListener {
            ivPuzzle1.visibility = View.VISIBLE
            DialogManager.startDialog(requireActivity(), "lvl0_puzzle1")
            ivClick.visibility = View.VISIBLE
            soundManager.playSound(R.raw.sound_of_drawer_opening)
        }

        btnLvlCompleted.setOnClickListener {
            ivBgBlurred.visibility = View.VISIBLE
            ivCard.visibility = View.VISIBLE
            saveManager.saveLevelStatus(requireContext(), 0)
        }

        ivBgBlurred.setOnClickListener {
            ivBgBlurred.visibility = View.GONE
            ivCard.visibility = View.GONE
            btnLvlCompleted.visibility = View.GONE
            LevelAccessManager.upgradeAccessLvl(this)
        }

        ivClick.setOnClickListener {
            ivPuzzle1.visibility = View.GONE
            ivClick.visibility = View.GONE
            soundManager.playSound(R.raw.sound_of_drawer_closing)
        }

        ivDarkness.setOnClickListener {
            DialogManager.startDialog(requireActivity(), "lvl0_dark")
            // Тестирование !!!
            flashlightManager.toggleFlashlight(true)
        }

        btnLightOn.setOnClickListener {
            ivDarknessFlashlight.visibility = View.GONE
            btnLightOn.visibility = View.GONE
            DialogManager.startDialog(requireActivity(),"lvl0_light_on")
            flashlightManager.toggleFlashlight(false) // Выкл фонарик
            flashlightManager.stopMonitoring()
            saveManager.savePuzzleData(requireContext(), 0, "flashlight", status = "completed")
            enableButtons()
            soundManager.playSound(R.raw.sound_of_light_switch)
        }

        flashlightManager = FlashlightManager(requireContext()) { isFlashlightOn ->
            requireActivity().runOnUiThread {
                if (isFlashlightOn && LoadManager.getPuzzleStatus(requireActivity(), 0, "flashlight") == "in_progress") {
                    soundManager.playSound(R.raw.sound_of_a_flashlight)
                    DialogManager.startDialog(requireActivity(),"lvl0_flashlight_on")
                    ivDarkness.visibility = View.GONE
                } else {
                    if (LoadManager.getPuzzleStatus(requireActivity(), 0, "flashlight") == "in_progress") {
                        soundManager.playSound(R.raw.sound_of_a_flashlight)
                        ivDarkness.visibility = View.VISIBLE
                    }
                }
            }
        }
    }


    private fun startAwakeningAnim() {
        ivBlack.animate()
            .alpha(0f)
            .setDuration(3000)
            .withEndAction {
                ivBlack.visibility = View.GONE
                flashlightManager.startMonitoring()
                DialogManager.startDialog(requireActivity(),"lvl0_start")
            }
            .start()
    }


    private fun handleSounds() {
        musicManager = MusicManager.getInstance()
        soundManager = SoundManager.getInstance()
        soundManager.init()
        soundManager.loadSound(
            requireContext(), listOf(
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
        flashlightManager.stopMonitoring()
    }

    override fun onResume() {
        super.onResume()

        musicManager.playMusic(requireContext(), R.raw.soundtrack_2)
        saveManager = SaveManager.getInstance()
        saveManager.saveCurrentLevel(requireContext(), 0)
    }


    override fun useHint() {
        if (LoadManager.getPuzzleStatus(requireActivity(), 0, "flashlight") == "in_progress")
            hintManager.useHint(requireActivity())
        else {
            if (LoadManager.isLevelCompleted(requireActivity(),0)) {
                DialogManager.startDialog(requireActivity(), "no_hints")
            }
            else
                DialogManager.startDialog(requireActivity(), "lvl0_to_puzzle1_hint")

        }
    }
}





