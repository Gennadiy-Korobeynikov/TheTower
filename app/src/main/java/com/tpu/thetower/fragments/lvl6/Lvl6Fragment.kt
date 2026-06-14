package com.tpu.thetower.fragments.lvl6

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl6Binding
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl6Fragment : Fragment(R.layout.fragment_lvl6), Hintable {

    private var _binding: FragmentLvl6Binding? = null
    private val binding get() = _binding!!

    @Inject lateinit var musicManager: MusicManager
    @Inject lateinit var soundManager: SoundManager
    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var dialogManager: DialogManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLvl6Binding.bind(view)
        setListeners()

        if (loadManager.getCurrentDialogIndex(6, "start") == 0) {
            dialogManager.startDialog(requireActivity(), "lvl6_start")
        }
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

    override fun useHint() {
        dialogManager.startDialog(requireActivity(), "hint_is_not_here")
    }

    override fun skipPuzzle() {
    }

    override fun onResume() {
        super.onResume()

        musicManager.playMusic(R.raw.soundtrack_empty_museum_after_hours)
        saveRepo.saveCurrentLevel(6)
    }
}