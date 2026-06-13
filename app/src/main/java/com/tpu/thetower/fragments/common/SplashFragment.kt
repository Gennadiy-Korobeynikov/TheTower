package com.tpu.thetower.fragments.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tpu.thetower.R
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.UiVisibilityController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            delay(SPLASH_DELAY_MS)
            if (!isAdded) return@launch

            FragmentNavigation.changeBG(this@SplashFragment, R.id.action_global_titleScreenFragment)
        }

        UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
        UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.TOPBAR_UI)

    }

    private companion object {
        private const val SPLASH_DELAY_MS = 1800L
    }
}
