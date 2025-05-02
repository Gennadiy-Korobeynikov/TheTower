package com.tpu.thetower.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl2PetBinding

class Lvl2PetFragment : Fragment(R.layout.fragment_lvl2_pet) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FragmentManager.showGoBackArrow(requireActivity())

    }

}