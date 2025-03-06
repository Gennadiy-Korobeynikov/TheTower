package com.tpu.thetower.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentHudBinding

class HUDFragment : Fragment(R.layout.fragment_hud) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentHudBinding.bind(view)
        val btnToTitle: Button = binding.btnToTitlescreen

        btnToTitle.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_global_titleScreenFragment)
            FragmentManager.hideHUD(requireActivity())
            FragmentManager.hideGoBackArrow(requireActivity())
        }
    }

}