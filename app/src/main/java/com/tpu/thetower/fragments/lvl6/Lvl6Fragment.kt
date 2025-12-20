package com.tpu.thetower.fragments.lvl6

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.managers.SaveManager
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.databinding.FragmentLvl6Binding

class Lvl6Fragment : Fragment(R.layout.fragment_lvl6) {
    private lateinit var binding: FragmentLvl6Binding

    private lateinit var musicManager: MusicManager
    private lateinit var soundManager: SoundManager
    private lateinit var saveManager: SaveManager

    private lateinit var btnToMainPanel: AppCompatButton
    private lateinit var btnToPanels: AppCompatButton
    private lateinit var btnToLock: AppCompatButton


    private fun bindView() {
        btnToMainPanel = binding.btnToMainPanel
        btnToPanels = binding.btnToPanels
        btnToLock = binding.btnToLock
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLvl6Binding.bind(view)
        bindView()
        setListeners()
    }
    private fun setListeners() {
        btnToMainPanel.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl6Fragment_to_lvl6MainPanelFragment)
        }
        btnToPanels.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl6Fragment_to_lvl6PanelsFragment)
        }
        btnToLock.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl6Fragment_to_lvl6LockFragment)
        }
    }
}