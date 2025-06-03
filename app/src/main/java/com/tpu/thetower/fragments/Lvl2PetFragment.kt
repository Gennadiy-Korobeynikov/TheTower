package com.tpu.thetower.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tpu.thetower.DialogManager
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.SoundManager
import com.tpu.thetower.databinding.FragmentLvl2PetBinding

class Lvl2PetFragment : Fragment(R.layout.fragment_lvl2_pet), Hintable {

    private lateinit var soundManager: SoundManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FragmentManager.showGoBackArrow(requireActivity())

        soundManager = SoundManager.getInstance()
        soundManager.init()
        soundManager.loadSound(requireContext(), listOf(
            R.raw.sound_of_drawer_closing
        ))
    }

    override fun useHint() {
        DialogManager.startDialog(requireActivity(), "hint_is_not_here")
    }

    override fun onPause() {
        super.onPause()

        soundManager.playSound(R.raw.sound_of_drawer_closing)
    }

}