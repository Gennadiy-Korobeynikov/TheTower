package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.LevelAccessManager
import com.tpu.thetower.LoadManager
import com.tpu.thetower.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.SoundManager
import com.tpu.thetower.databinding.FragmentLvl2Binding


class Lvl2Fragment : Fragment(R.layout.fragment_lvl2) {

    private lateinit var binding: FragmentLvl2Binding

    private lateinit var musicManager: MusicManager
    private lateinit var soundManager: SoundManager
    private lateinit var saveManager: SaveManager

    private lateinit var btnToPuzzle0: Button
    private lateinit var btnToPuzzle0Lock: Button
    private lateinit var btnToPuzzle0Completed: Button
    private lateinit var btnToPuzzle1: Button
    private lateinit var btnToPuzzle1Lock: Button

    private lateinit var ivPuzzle0: ImageView
    private lateinit var ivClick: ImageView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLvl2Binding.bind(view)
        bindView()
        setListeners()
        handleSounds()

        FragmentManager.showGoBackArrow(requireActivity())

        when (LoadManager.getLevelProgress(requireActivity(), 2)) {
            1 -> {
                btnToPuzzle0Lock.visibility = View.GONE
                btnToPuzzle0Completed.visibility = View.VISIBLE
            }
            2 -> {
                btnToPuzzle0Lock.visibility = View.GONE
                btnToPuzzle0Completed.visibility = View.VISIBLE
            }
            3 -> {
                // TODO Что происходит после завершения уровня и открытия последнего замка
                LevelAccessManager.upgradeAccessLvl(this)
            }
        }
    }

    private fun bindView() {
        btnToPuzzle0 = binding.btnToPuzzle0
        btnToPuzzle0Lock = binding.btnToPuzzle0Lock
        btnToPuzzle0Completed = binding.btnToPuzzle0Completed
        btnToPuzzle1 = binding.btnToPuzzle1
        btnToPuzzle1Lock = binding.btnToPuzzle1Lock
        ivPuzzle0 = binding.ivPuzzle0
        ivClick = binding.ivClick
    }

    private fun setListeners() {
        btnToPuzzle0.setOnClickListener {
            ivPuzzle0.setImageResource(R.drawable.lvl2_puzzle0)
            ivPuzzle0.visibility = View.VISIBLE
            ivClick.visibility = View.VISIBLE
        }

        ivClick.setOnClickListener {
            ivPuzzle0.visibility = View.GONE
            ivClick.visibility = View.GONE
        }

        btnToPuzzle0Lock.setOnClickListener {
            FragmentManager.choosePuzzle(this, 0)
            FragmentManager.changeBG(this, R.id.action_lvl2Fragment_to_lvl2Puzzle0Fragment)
        }

        btnToPuzzle0Completed.setOnClickListener {
            ivPuzzle0.setImageResource(R.drawable.lvl2_puzzle0_completed)
            ivPuzzle0.visibility = View.VISIBLE
            ivClick.visibility = View.VISIBLE
        }

        btnToPuzzle1.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_lvl2Fragment_to_lvl2Puzzle1Fragment)
        }

        btnToPuzzle1Lock.setOnClickListener {
            FragmentManager.choosePuzzle(this, 1)
            FragmentManager.changeBG(this, R.id.action_lvl2Fragment_to_lvl2Puzzle0Fragment)
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
        saveManager.saveCurrentLevel(requireContext(), 2)
    }

}