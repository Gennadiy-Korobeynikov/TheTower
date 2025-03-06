package com.tpu.thetower.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentGoBackArrowBinding
import com.tpu.thetower.databinding.FragmentHudBinding
import com.tpu.thetower.databinding.FragmentTitleScreenBinding

class GoBackArrowFragment : Fragment(R.layout.fragment_go_back_arrow) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentGoBackArrowBinding.bind(view)
        val btnToElevator: ImageButton = binding.btnGoBack

        btnToElevator.setOnClickListener {
            FragmentManager.goBack(this)
        }
    }

}