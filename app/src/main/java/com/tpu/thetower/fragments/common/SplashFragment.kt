package com.tpu.thetower.fragments.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tpu.thetower.R
import com.tpu.thetower.managers.FragmentNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Небольшая задержка: даём системе стабилизировать измерения/лейаут,
        // и уводим пользователя с проблемного первого кадра TitleScreen.
        viewLifecycleOwner.lifecycleScope.launch {
            delay(SPLASH_DELAY_MS)
            if (!isAdded) return@launch

            FragmentNavigation.changeBG(this@SplashFragment, R.id.action_global_titleScreenFragment)
        }
    }

    private companion object {
        private const val SPLASH_DELAY_MS = 1800L
    }
}
