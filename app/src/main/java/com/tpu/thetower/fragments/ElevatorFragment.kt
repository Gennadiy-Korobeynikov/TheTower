package com.tpu.thetower.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.LevelAccessManager
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.SoundManager
import com.tpu.thetower.databinding.FragmentElevatorBinding
import com.tpu.thetower.databinding.FragmentLvl0Binding

class ElevatorFragment : Fragment(R.layout.fragment_elevator) {

    private lateinit var binding : FragmentElevatorBinding
    private lateinit var soundManager: SoundManager
    private lateinit var saveManager: SaveManager

    private lateinit var btnToLvl0: Button
    private lateinit var btnToLvl1: Button
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


        val gameData = saveManager.readData(requireContext()) //СМОТРИ СЮДА
        val loadedLastUnlockedModule = gameData?.playerInfo?.lastUnlockedModule ?: 0
        unlockLvls(loadedLastUnlockedModule)


    }

    private fun bindView() {
        btnToLvl0 = binding.btnElevatorToLvl0
        btnToLvl1 = binding.btnElevatorToLvl1
        btnToLvlTest = binding.btnElevatorToLvlTest

        lvlButtons = listOf(
            btnToLvl0,
            btnToLvl1,
            btnToLvlTest
        )
    }

    private fun setListeners() {

        btnToLvl0.setOnClickListener {
            soundManager.release()
            FragmentManager.changeBG(this, R.id.action_elevatorFragment_to_lvl0Fragment)
        }

        btnToLvl1.setOnClickListener {
            soundManager.release()
            FragmentManager.changeBG(this, R.id.action_elevatorFragment_to_lvl1Fragment)
        }

        btnToLvlTest.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_elevatorFragment_to_lvlTestFragment)
        }



        requireActivity().supportFragmentManager
            .setFragmentResultListener("moduleUnlocking", viewLifecycleOwner) { _, bundle ->
                val lastUnlockedModule = bundle.getInt("lastUnlockedModule")
                unlockLvls(lastUnlockedModule)
            }
    }

    private fun unlockLvls(lastUnlockedModule : Int) {
        val unlockingLvls = LevelAccessManager.modules[lastUnlockedModule]
        unlockingLvls?.forEach { lvlButtons[it].visibility = View.VISIBLE }
        //TODO Обновить дизайн панели уроавления
    }
}