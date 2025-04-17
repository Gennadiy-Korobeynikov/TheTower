package com.tpu.thetower.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
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
import kotlin.concurrent.thread
import kotlin.reflect.KProperty


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
        setListeners()
        handleSounds()
        FragmentManager.hideGoBackArrow(requireActivity())
        hintManager = HintManager(listOf("lvl0_puzzle0_hint1" , "lvl0_puzzle0_hint2"),
            LoadManager.getPuzzleUsedHintsCount(requireActivity(),0,0),
            0,0)

        when (LoadManager.getLevelProgress(requireActivity(), 0)) {
            0 -> {
                startAwakeningAnim()
            }
            1 -> {
                FragmentManager.light = true
                ivDarkness.visibility = View.GONE
                ivDarknessFlashlight.visibility = View.GONE
                btnLightOn.visibility = View.GONE
                ivBlack.visibility = View.GONE
            }

            2 -> {
                ivDarkness.visibility = View.GONE
                ivDarknessFlashlight.visibility = View.GONE
                btnLightOn.visibility = View.GONE
                ivBlack.visibility = View.GONE
                FragmentManager.light = true

                ivMain.setImageResource(R.drawable.lvl0_bd_solved)
                btnToPuzzle1Lock.visibility = View.GONE
                btnToPuzzle1Lock.visibility = View.GONE
                btnToElevator.visibility = View.VISIBLE

            }
        }
    }

    private fun bindView() {

        btnToElevator = binding.btnToElevator
        btnToPuzzle1 = binding.btnToPuzzle1
        btnToPuzzle1Lock = binding.btnToPuzzle1Lock
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
            FragmentManager.changeBG(this, R.id.action_global_elevatorFragment)
            FragmentManager.showGoBackArrow(requireActivity())
            soundManager.playSound(R.raw.sound_of_an_elevator_door_opening)
        }

        btnToPuzzle1Lock.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_lvl0Fragment_to_lvl0Puzzle1Fragment)
            FragmentManager.showGoBackArrow(requireActivity())
        }

        btnToPuzzle1.setOnClickListener {
            ivPuzzle1.visibility = View.VISIBLE
            DialogManager.startDialog(requireActivity(), "lvl0_puzzle1")
            ivClick.visibility = View.VISIBLE
        }

        ivClick.setOnClickListener {
            ivPuzzle1.visibility = View.GONE
            ivClick.visibility = View.GONE
        }

        ivDarkness.setOnClickListener {
            DialogManager.startDialog(requireActivity(), "lvl0_dark")
        }

        btnLightOn.setOnClickListener {
            FragmentManager.light = true
            ivDarknessFlashlight.visibility = View.GONE
            btnLightOn.visibility = View.GONE
            DialogManager.startDialog(requireActivity(),"lvl0_light_on")
            flashlightManager.toggleFlashlight(false) // Выкл фонарик
            flashlightManager.stopMonitoring()
            saveManager.savePuzzleData(requireContext(), 0, 0)
        }

        flashlightManager = FlashlightManager(requireContext()) { isFlashlightOn ->
            requireActivity().runOnUiThread {
                if (isFlashlightOn && !FragmentManager.light) {
                    soundManager.playSound(R.raw.sound_of_a_flashlight)
                    DialogManager.startDialog(requireActivity(),"lvl0_flashlight_on")
                    ivDarkness.visibility = View.GONE
                } else {
                    if (!FragmentManager.light) {
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
                R.raw.sound_of_an_elevator_door_opening
            )
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        flashlightManager.stopMonitoring()
        saveManager.saveLevelProgress(requireActivity(), 0)
    }

    override fun onResume() {
        super.onResume()

        musicManager.playMusic(requireContext(), R.raw.soundtrack_2)
        saveManager = SaveManager.getInstance()
        saveManager.saveCurrentLevel(requireContext(), 0)
    }


    override fun useHint() {
        if (!FragmentManager.light)
            hintManager.useHint(requireActivity())
        else
            DialogManager.startDialog(requireActivity(), "lvl0_to_puzzle1_hint")
    }
}





