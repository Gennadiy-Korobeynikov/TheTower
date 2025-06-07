package com.tpu.thetower.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl5PuzzleMooseBinding

class Lvl5PuzzleMooseFragment : Fragment(R.layout.fragment_lvl5_puzzle_moose) {

    private lateinit var binding: FragmentLvl5PuzzleMooseBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl5PuzzleMooseBinding.bind(view)
    }

}