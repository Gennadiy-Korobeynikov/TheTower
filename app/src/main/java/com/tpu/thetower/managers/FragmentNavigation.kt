package com.tpu.thetower.managers

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

object FragmentNavigation {

    fun changeBG(from: Fragment, to: Int, bundle: Bundle = Bundle()) {
        from.findNavController().navigate(to, bundle)
    }

    fun goBack(from: Fragment) {
        from.findNavController().popBackStack()
    }
}
