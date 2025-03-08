package com.tpu.thetower.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl0Binding
import com.tpu.thetower.databinding.FragmentLvl0Puzzle1Binding
import com.tpu.thetower.databinding.FragmentTitleScreenBinding


class Lvl0Puzzle1Fragment : Fragment(R.layout.fragment_lvl0_puzzle1) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentLvl0Puzzle1Binding.bind(view)

    }

}