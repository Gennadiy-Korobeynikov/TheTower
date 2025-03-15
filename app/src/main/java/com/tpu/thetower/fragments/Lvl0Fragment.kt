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
import com.tpu.thetower.DialogManager
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.SoundManager
import com.tpu.thetower.databinding.FragmentLvl0Binding
import com.tpu.thetower.devicemanagers.FlashlightManager


class Lvl0Fragment : Fragment(R.layout.fragment_lvl0) {

    private lateinit var binding : FragmentLvl0Binding

    private lateinit var flashlightManager: FlashlightManager
    private lateinit var musicManager: MusicManager
    private lateinit var soundManager: SoundManager
    private lateinit var saveManager: SaveManager

    private lateinit var btnToElevator : Button
    private lateinit var btnToPuzzle1 : Button
    private lateinit var btnLightOn : ImageButton

    private lateinit var ivDarkness : ImageView
    private lateinit var ivDarknessFlashlight : ImageView
    private lateinit var ivBlack : ImageView



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl0Binding.bind(view)
        bindView()
        setListeners()
        handleSounds()
        FragmentManager.hideGoBackArrow(requireActivity())

        if (FragmentManager.light) {
            ivDarkness.visibility = View.GONE
            ivDarknessFlashlight.visibility = View.GONE
            btnLightOn.visibility = View.GONE
            ivBlack.visibility = View.GONE
        }
        else
            startAwakeningAnim()
    }

    private fun bindView() {

        btnToElevator= binding.btnToElevator
        btnToPuzzle1 = binding.btnToPuzzle1
        ivDarkness  = binding.ivDarkness
        ivDarknessFlashlight = binding.ivDarknessFlashlight
        ivBlack = binding.ivBlack
        btnLightOn = binding.btnLightOn
    }

    private fun setListeners() {


        btnToElevator.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_global_elevatorFragment)
            FragmentManager.showGoBackArrow(requireActivity())
            soundManager.playSound(R.raw.sound_of_an_elevator_door_opening)
        }

        btnToPuzzle1.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_lvl0Fragment_to_lvl0Puzzle1Fragment)
            FragmentManager.showGoBackArrow(requireActivity())
        }



        ivDarkness.setOnClickListener {
            DialogManager.startDialog(requireActivity(),"lvl0_dark")
        }

        btnLightOn.setOnClickListener {
            FragmentManager.light = true
            ivDarknessFlashlight.visibility = View.GONE
            btnLightOn.visibility = View.GONE
            DialogManager.startDialog(requireActivity(),"lvl0_light_on")
            flashlightManager.unregister()
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

    private fun getPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) //Временная заглушка для демо
            FragmentManager.showPermissionRequestFragment(requireActivity())
    }

    private fun startAwakeningAnim() {
        ivBlack.animate()
            .alpha(0f)
            .setDuration(3000)
            .withEndAction {
                ivBlack.visibility = View.GONE
                DialogManager.startDialog(requireActivity(),"lvl0_start")
            }
            .start()
        }


    private fun handleSounds() {
        musicManager = MusicManager.getInstance()
        soundManager = SoundManager.getInstance()
        soundManager.init()
        soundManager.loadSound(requireContext(), R.raw.sound_of_a_flashlight)
        soundManager.loadSound(requireContext(), R.raw.sound_of_an_elevator_door_opening)
    }


    override fun onDestroy() {
        super.onDestroy()
        flashlightManager.unregister()
    }

    override fun onResume() {
        super.onResume()

        musicManager.playMusic(requireContext(), R.raw.soundtrack_2)
        saveManager = SaveManager.getInstance()
        saveManager.saveCurrentLevel(requireContext(), 0)
    }

    override fun onPause() {
        super.onPause()

        musicManager.pauseMusic()
    }
}





