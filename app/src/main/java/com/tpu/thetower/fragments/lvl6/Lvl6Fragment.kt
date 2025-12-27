package com.tpu.thetower.fragments.lvl6

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.databinding.FragmentLvl6Binding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl6Fragment : Fragment(R.layout.fragment_lvl6) {

    private var _binding: FragmentLvl6Binding? = null
    private val binding get() = _binding!!

    @Inject lateinit var musicManager: MusicManager
    @Inject lateinit var soundManager: SoundManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLvl6Binding.bind(view)
        setListeners()
    }

    private fun setListeners() {
        binding.btnToMainPanel.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl6Fragment_to_lvl6MainPanelFragment)
        }
        binding.btnToPanels.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl6Fragment_to_lvl6PanelsFragment)
        }
        binding.btnToLock.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl6Fragment_to_lvl6LockFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}