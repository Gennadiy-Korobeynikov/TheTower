package com.tpu.thetower.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentTitleScreenBinding


class TitleScreenFragment : Fragment(R.layout.fragment_title_screen) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val binding = FragmentTitleScreenBinding.bind(view)
        val btn_test : Button = binding.btnToLvl0
        val btn_settings : Button = binding.btnToSettings

        FragmentManager.hideHUD(requireActivity())
        FragmentManager.hideGoBackArrow(requireActivity())

        btn_test.setOnClickListener {
            FragmentManager.changeBG(this,R.id.action_titleScreenFragment_to_lvl0Fragment)
            FragmentManager.showHUD(requireActivity())
            FragmentManager.showGoBackArrow(requireActivity())

        }

        btn_settings.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_titleScreenFragment_to_settingsFragment)
        }
    }


}