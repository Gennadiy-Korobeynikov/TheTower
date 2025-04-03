package com.tpu.thetower.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.tpu.thetower.DialogManager
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.LoadManager
import com.tpu.thetower.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.SoundManager
import com.tpu.thetower.databinding.FragmentLvl0Binding
import com.tpu.thetower.databinding.FragmentLvl1Binding
import com.tpu.thetower.devicemanagers.FlashlightManager


class Lvl1Fragment : Fragment(R.layout.fragment_lvl1){

    private lateinit var binding : FragmentLvl1Binding

    private lateinit var musicManager: MusicManager
    private lateinit var soundManager: SoundManager
    private lateinit var saveManager: SaveManager

    private lateinit var btnNpcReceptionist : Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLvl1Binding.bind(view)
        bindView()
        setListeners()
        handleSounds()

    }

    private fun bindView() {
        btnNpcReceptionist = binding.btnNpcReceptionist
    }

    private fun setListeners() {
        btnNpcReceptionist.setOnClickListener {
            // if (Первый раз диалог)
            DialogManager.startDialog(requireActivity(),"lvl1_npc_receptionist")
            //else
            // DialogManager.startDialog(requireActivity(),"lvl1_npc_receptionist_2)")
        }
    }

    private fun handleSounds() {
        musicManager = MusicManager.getInstance()
        soundManager = SoundManager.getInstance()
        soundManager.init()
        soundManager.loadSound(requireContext(), R.raw.sound_of_a_flashlight)
        soundManager.loadSound(requireContext(), R.raw.sound_of_an_elevator_door_opening)
    }


    override fun onResume() {
        super.onResume()

        saveManager = SaveManager.getInstance()
        saveManager.saveCurrentLevel(requireContext(), 1)
    }

}