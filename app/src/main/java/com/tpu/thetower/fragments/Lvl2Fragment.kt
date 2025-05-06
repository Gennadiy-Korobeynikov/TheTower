package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
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
import com.tpu.thetower.databinding.FragmentLvl2Binding


class Lvl2Fragment : Fragment(R.layout.fragment_lvl2) , Hintable {

    private lateinit var binding: FragmentLvl2Binding

    private lateinit var musicManager: MusicManager
    private lateinit var soundManager: SoundManager
    private lateinit var saveManager: SaveManager

    private lateinit var btnToPuzzle0: Button
    private lateinit var btnToPuzzle0Lock: Button
    private lateinit var btnToPuzzle0Completed: Button
    private lateinit var btnToPuzzle1: Button
    private lateinit var btnToPuzzle2Lock: Button
    private lateinit var btnToPuzzle2Completed: Button
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
                btnToPuzzle0Lock.visibility = View.GONE
                btnToPuzzle0Completed.visibility = View.VISIBLE
                btnToPuzzle2Lock.visibility = View.GONE
                btnToPuzzle2Completed.visibility = View.GONE
                // TODO Что происходит после завершения уровня и открытия последнего замка
            }
        }
    }

    private fun bindView() {
        btnToPuzzle0 = binding.btnToPuzzle0
        btnToPuzzle0Lock = binding.btnToPuzzle0Lock
        btnToPuzzle0Completed = binding.btnToPuzzle0Completed
        btnToPuzzle1 = binding.btnToPuzzle1
        btnToPuzzle2Lock = binding.btnToPuzzle2Lock
        btnToPuzzle2Completed = binding.btnToPuzzle2Completed
    }

    private fun setListeners() {
        btnToPuzzle0.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_lvl2Fragment_to_lvl2CaesarFragment)
        }

        btnToPuzzle0Lock.setOnClickListener {
            FragmentManager.choosePuzzle(this, 0)
            FragmentManager.changeBG(this, R.id.action_lvl2Fragment_to_lvl2Puzzle0Fragment)
        }

        btnToPuzzle0Completed.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_lvl2Fragment_to_lvl2PetFragment)
        }

        btnToPuzzle1.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_lvl2Fragment_to_lvl2Puzzle1Fragment)
        }

        btnToPuzzle2Lock.setOnClickListener {
            FragmentManager.choosePuzzle(this, 1)
            FragmentManager.changeBG(this, R.id.action_lvl2Fragment_to_lvl2Puzzle0Fragment)
        }

        btnToPuzzle2Completed.setOnClickListener {
            // TODO Как будто сюда можно что-то запихнуть, если нет, то просто удалить
            LevelAccessManager.upgradeAccessLvl(this)
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

    override fun useHint() {
        if (LoadManager.isLevelCompleted(requireActivity(),2)) {
            DialogManager.startDialog(requireActivity(), "no_hints")
        }
        else
            DialogManager.startDialog(requireActivity(), "hint_is_not_here")
    }

}