package com.tpu.thetower.fragments.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.tpu.thetower.R
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.databinding.FragmentGoBackArrowBinding
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.utils.SoundEffect
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GoBackArrowFragment : Fragment(R.layout.fragment_go_back_arrow) {

    @Inject lateinit var soundManager: SoundManager
    @Inject lateinit var loadManager: LoadManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentGoBackArrowBinding.bind(view)
        val btnGoBack: ImageButton = binding.btnGoBack

        btnGoBack.setOnClickListener {
            val navController = findNavController()
            val isInElevator = navController.currentDestination?.id == R.id.elevatorFragment

            if (isInElevator) {
                FragmentNavigation.changeBG(this,loadManager.getCurrentLevelFragmentId())
                soundManager.playSound(SoundEffect.ELEVATOR_DOOR)
                soundManager.playSound(SoundEffect.STEPS)
            } else {
                FragmentNavigation.goBack(this, soundManager)
            }
        }
    }

}