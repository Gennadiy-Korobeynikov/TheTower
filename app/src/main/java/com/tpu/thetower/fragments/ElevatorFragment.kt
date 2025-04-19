package com.tpu.thetower.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.LevelAccessManager
import com.tpu.thetower.LoadManager
import com.tpu.thetower.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.SoundManager
import com.tpu.thetower.databinding.FragmentElevatorBinding
import com.tpu.thetower.databinding.FragmentLvl0Binding

class ElevatorFragment : Fragment(R.layout.fragment_elevator) {

    private lateinit var binding : FragmentElevatorBinding
    private lateinit var soundManager: SoundManager
    private lateinit var saveManager: SaveManager
    private lateinit var musicManager: MusicManager

    private lateinit var btnToLvl0: Button
    private lateinit var btnToLvl1: Button
    private lateinit var btnToLvl2: Button
    private lateinit var btnToLvl3: Button
    private lateinit var btnToLvl4: Button
    private lateinit var btnToLvlTest: Button

    private lateinit var lvlButtons : List<Button>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding = FragmentElevatorBinding.bind(view)
        bindView()
        setListeners()

        val receivedData = arguments?.getString("saved_level")
        if (receivedData != null) {
            FragmentManager.changeBG(this, receivedData.toInt())
            arguments = Bundle()
        }
        soundManager = SoundManager.getInstance()
        saveManager = SaveManager.getInstance()
        musicManager = MusicManager.getInstance()

        musicManager.stopMusic()


        val currAccessLevel = LoadManager.getAccessLevel(requireActivity())
        unlockLvls(currAccessLevel)
    }

    private fun bindView() {
        btnToLvl0 = binding.btnElevatorToLvl0
        btnToLvl1 = binding.btnElevatorToLvl1
        btnToLvl2 = binding.btnElevatorToLvl2
        btnToLvl3 = binding.btnElevatorToLvl3
        btnToLvl4 = binding.btnElevatorToLvl4
        btnToLvlTest = binding.btnElevatorToLvlTest

        lvlButtons = listOf(
            btnToLvl0,
            btnToLvl1,
            btnToLvl2,
            btnToLvl3,
            btnToLvl4,
            btnToLvlTest
        )
    }

    private fun setListeners() {
        btnToLvlTest.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_elevatorFragment_to_lvlTestFragment)
        }

        btnToLvl0.setOnClickListener {
            soundManager.release()
            FragmentManager.changeBG(this, R.id.action_elevatorFragment_to_lvl0Fragment)
        }

        btnToLvl1.setOnClickListener {
            soundManager.release()
            FragmentManager.changeBG(this, R.id.action_elevatorFragment_to_lvl1Fragment)
        }


        btnToLvl2.setOnClickListener {
            soundManager.release()
            FragmentManager.changeBG(this, R.id.action_elevatorFragment_to_lvl2Fragment)
        }

        btnToLvl3.setOnClickListener {
            //soundManager.release()
            FragmentManager.changeBG(this, R.id.action_elevatorFragment_to_lvl2PuzzleHooverFragment)
        }

        btnToLvl4.setOnClickListener {
            soundManager.release()
            FragmentManager.changeBG(this, R.id.action_elevatorFragment_to_lvl4Fragment)
        }


        requireActivity().supportFragmentManager
            .setFragmentResultListener("moduleUnlocking", viewLifecycleOwner) { _, bundle ->
                val currAccessLevel = bundle.getInt("currAccessLevel")
                unlockLvls(currAccessLevel)
            }
    }

    private fun unlockLvls(currAccessLevel : Int) {
        val topUnlockingLvl = LevelAccessManager.topUnlockedLvlsForModules[currAccessLevel]
        val unlockingLvls = (0..topUnlockingLvl)
        unlockingLvls.forEach { lvlButtons[it].visibility = View.VISIBLE }
        //TODO Обновить дизайн панели управления
    }
}