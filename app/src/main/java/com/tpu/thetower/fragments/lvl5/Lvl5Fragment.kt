package com.tpu.thetower.fragments.lvl5

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView

import androidx.fragment.app.Fragment
import com.tpu.thetower.managers.DialogManager

import com.tpu.thetower.managers.LoadManager

import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.managers.SaveManager
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.databinding.FragmentLvl5Binding
import com.tpu.thetower.managers.FragmentNavigation


class Lvl5Fragment : Fragment(R.layout.fragment_lvl5) {

    private lateinit var binding: FragmentLvl5Binding

    private lateinit var musicManager: MusicManager
    private lateinit var soundManager: SoundManager
    private lateinit var saveManager: SaveManager

    private lateinit var btnFishRack: Button
    private lateinit var btnMoose: Button
    private lateinit var btnFish: Button
    private lateinit var btnMap: Button
    private lateinit var btnMoosePaper: Button

    private lateinit var ivBg: ImageView

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

        if (LoadManager.getPuzzleStatus(requireActivity(), 5, "moose") == "completed") {
            btnFishRack.visibility = View.VISIBLE
            btnMoose.visibility = View.GONE
            ivBg.setImageResource(R.drawable.lvl5_bg_after_moose)
            btnMoosePaper.visibility = View.VISIBLE
        }
    }

    private fun bindView() {
        btnFishRack = binding.btnFishRack
        btnMoose = binding.btnMoose
        btnFish = binding.btnFish
        btnMap = binding.btnMap
        ivBg = binding.ivBg
    }

    private fun setListeners() {

        btnFishRack.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl5Fragment_to_lvl5FishRackFragment)
        }

        btnMoose.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl5Fragment_to_lvl5PuzzleMooseFragment)
        }

        btnFish.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl5Fragment_to_lvl5PuzzleBluetoothFragment)
        }

        btnMoosePaper.setOnClickListener {
            DialogManager.startDialog(requireActivity(), "lvl5_moose_paper")
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