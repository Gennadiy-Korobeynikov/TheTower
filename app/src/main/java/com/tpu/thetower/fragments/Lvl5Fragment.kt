package com.tpu.thetower.fragments

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible

import androidx.fragment.app.Fragment
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.LoadManager

import com.tpu.thetower.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.SoundManager
import com.tpu.thetower.databinding.FragmentLvl4Binding
import com.tpu.thetower.databinding.FragmentLvl5Binding


class Lvl5Fragment : Fragment(R.layout.fragment_lvl5) {

    private lateinit var binding: FragmentLvl5Binding

    private lateinit var musicManager: MusicManager
    private lateinit var soundManager: SoundManager
    private lateinit var saveManager: SaveManager

    private lateinit var btnFishRack: Button
    private lateinit var btnMoose: Button
    private lateinit var btnFish: Button
    private lateinit var btnMap: Button



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLvl5Binding.bind(view)
        bindView()
        setListeners()
        handleSounds()
        saveManager = SaveManager.getInstance()

        if (LoadManager.getPuzzleStatus(requireActivity(), 5, "bluetooth") == "completed") {
            btnMoose.visibility = View.VISIBLE
        }
    }

    private fun bindView() {
        btnFishRack = binding.btnFishRack
        btnMoose = binding.btnMoose
        btnFish = binding.btnFish
        btnMap = binding.btnMap

    }

    private fun setListeners() {

        btnFishRack.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_lvl5Fragment_to_lvl5FishRackFragment)
        }

        btnMoose.setOnClickListener {
//            FragmentManager.changeBG(this, R.id.)
        }
    }





    private fun handleSounds() {
        musicManager = MusicManager.getInstance()
        soundManager = SoundManager.getInstance()
        soundManager.init()
//        soundManager.loadSound(
//            requireContext(), listOf(
//                R.raw.sound_of_a_flashlight,
//                R.raw.sound_of_an_elevator_door_opening
//            )
//        )
    }


    override fun onResume() {
        super.onResume()

        saveManager = SaveManager.getInstance()
        saveManager.saveCurrentLevel(requireContext(), 5)
    }

}