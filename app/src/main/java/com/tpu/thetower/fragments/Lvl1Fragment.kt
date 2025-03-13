package com.tpu.thetower.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.devicemanagers.FlashlightManager


class Lvl1Fragment : Fragment(R.layout.fragment_lvl1) {

    private lateinit var saveManager: SaveManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onResume() {
        super.onResume()

        saveManager = SaveManager.getInstance()
        saveManager.saveCurrentLevel(requireContext(), 1)
    }
}