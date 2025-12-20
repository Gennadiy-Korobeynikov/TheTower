package com.tpu.thetower.fragments.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageButton
import com.tpu.thetower.R
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.databinding.FragmentGoBackArrowBinding

class GoBackArrowFragment : Fragment(R.layout.fragment_go_back_arrow) {

    private lateinit var soundManager: SoundManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        soundManager = SoundManager.getInstance()

        val binding = FragmentGoBackArrowBinding.bind(view)
        val btnToElevator: ImageButton = binding.btnGoBack

        btnToElevator.setOnClickListener {
            soundManager.release()
            FragmentNavigation.goBack(this)
        }
    }

}