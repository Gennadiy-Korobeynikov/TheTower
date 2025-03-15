package com.tpu.thetower.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.R
import com.tpu.thetower.SoundManager
import com.tpu.thetower.databinding.FragmentElevatorBinding
import com.tpu.thetower.databinding.FragmentLvl0Binding

class ElevatorFragment : Fragment(R.layout.fragment_elevator) {

    private lateinit var soundManager: SoundManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val receivedData = arguments?.getString("saved_level")
        if (receivedData != null) {
            FragmentManager.changeBG(this, receivedData.toInt())
            arguments = Bundle()
        }

        soundManager = SoundManager.getInstance()

        val binding = FragmentElevatorBinding.bind(view)
        val btnToLvl0: Button = binding.btnElevatorToLvl0
        val btnToLvl1: Button = binding.btnElevatorToLvl1
        val btnToLvlTest: Button = binding.btnElevatorToLvlTest

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
    }
}