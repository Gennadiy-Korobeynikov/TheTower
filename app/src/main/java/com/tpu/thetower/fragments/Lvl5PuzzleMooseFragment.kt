package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl5PuzzleMooseBinding

class Lvl5PuzzleMooseFragment : Fragment(R.layout.fragment_lvl5_puzzle_moose) {

    private lateinit var binding: FragmentLvl5PuzzleMooseBinding

    private lateinit var ivMoose: ImageView

    private lateinit var btnLeft: Button
    private lateinit var btnRight: Button

    private var solution = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl5PuzzleMooseBinding.bind(view)

        bindView()
        setListeners()
    }

    private fun bindView() {
        ivMoose = binding.ivMoose
        btnLeft = binding.btnLeft
        btnRight = binding.btnRight
    }

    private fun setListeners() {

        btnLeft.setOnClickListener {
            ivMoose.setImageResource(R.drawable.lvl5_puzzle1_left)
            ivMoose.animate().setDuration(1000).withEndAction {
                ivMoose.setImageResource(R.drawable.lvl5_puzzle1_base)
            }
            solution += "0"
        }

        btnRight.setOnClickListener {
            ivMoose.setImageResource(R.drawable.lvl5_puzzle1_right)
            ivMoose.animate().setDuration(1000).withEndAction {
                ivMoose.setImageResource(R.drawable.lvl5_puzzle1_base)
            }
            solution += "1"
        }



    }

}