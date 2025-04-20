package com.tpu.thetower.fragments

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.DragShadowBuilder
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.SoundManager
import com.tpu.thetower.databinding.FragmentLvl3Binding

class Lvl3Fragment : Fragment(R.layout.fragment_lvl3) {
    private lateinit var binding: FragmentLvl3Binding

    private lateinit var musicManager: MusicManager
    private lateinit var soundManager: SoundManager
    private lateinit var saveManager: SaveManager

    private lateinit var btnToPuzzle0: Button
    private lateinit var btnToPuzzle1: Button

    private lateinit var ivTarget: ImageView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLvl3Binding.bind(view)
        bindView()
        setListeners()
        handleSounds()

        FragmentManager.showGoBackArrow(requireActivity())

    }

    private fun bindView() {
        btnToPuzzle0 = binding.btnToPuzzle0
        btnToPuzzle1 = binding.btnToPuzzle1
        ivTarget = binding.ivTarget
    }

    private fun setListeners() {
        btnToPuzzle0.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_lvl3Fragment_to_lvl3Puzzle0Fragment)
        }

        btnToPuzzle1.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_lvl3Fragment_to_lvl3Puzzle1Fragment)
        }

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


    override fun onResume() {
        super.onResume()

        saveManager = SaveManager.getInstance()
        saveManager.saveCurrentLevel(requireContext(), 3)
    }

    fun getTargetImageView(): View {
        return ivTarget
    }


}