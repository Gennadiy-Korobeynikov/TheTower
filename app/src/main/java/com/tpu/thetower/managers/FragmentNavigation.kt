package com.tpu.thetower.managers

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tpu.thetower.R
import com.tpu.thetower.utils.SoundEffect

object FragmentNavigation {

    fun changeBG(from: Fragment, to: Int, bundle: Bundle = Bundle()) {
        from.findNavController().navigate(to, bundle)
    }

    fun goBack(from: Fragment, soundManager: SoundManager? = null) {
        val navController = from.findNavController()
        val prevDestId = navController.previousBackStackEntry?.destination?.id

        if (prevDestId == R.id.elevatorFragment) {
            soundManager?.playSound(SoundEffect.ELEVATOR_DOOR)
            soundManager?.playSound(SoundEffect.STEPS)
        }

        navController.popBackStack()
    }
}
