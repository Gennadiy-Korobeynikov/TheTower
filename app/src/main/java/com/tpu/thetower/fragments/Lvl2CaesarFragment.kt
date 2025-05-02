package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.R


class Lvl2CaesarFragment : Fragment(R.layout.fragment_lvl2_caesar) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FragmentManager.showGoBackArrow(requireActivity())
    }

}