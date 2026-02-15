package com.tpu.thetower.fragments.lvl5

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl5Binding
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.models.PuzzleStatus
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl5Fragment : Fragment(R.layout.fragment_lvl5) {

    private var _binding: FragmentLvl5Binding? = null
    private val binding get() = _binding!!

    @Inject lateinit var musicManager: MusicManager
    @Inject lateinit var soundManager: SoundManager
    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var dialogManager: DialogManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLvl5Binding.bind(view)

        setListeners()

        if (loadManager.getPuzzleStatus(5, "moose") == PuzzleStatus.COMPLETED.value) {
            binding.btnMoose.visibility = View.GONE
            binding.ivBg.setImageResource(R.drawable.lvl5_bg_after_moose)
            binding.btnMoosePaper.visibility = View.VISIBLE
            binding.btnFish.visibility = View.GONE
        }

        if (loadManager.getPuzzleStatus(5, "fish rack") == PuzzleStatus.COMPLETED.value) {
            binding.btnFishRack.visibility = View.GONE
            if (loadManager.getCurrentDialogIndex(5, "lvl5_fisher_rack_completed") < 1) {
                dialogManager.startDialog(requireActivity(), "lvl5_fisher_rack_completed")
            }
        }

        if (loadManager.getPuzzleStatus(5, "chest") == PuzzleStatus.COMPLETED.value) {
            binding.btnChest.visibility = View.GONE
        }
    }

    private fun setListeners() {

        binding.btnFishRack.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl5Fragment_to_lvl5FishRackFragment)
        }

        binding.btnMoose.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl5Fragment_to_lvl5PuzzleMooseFragment)
        }

        binding.btnFish.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl5Fragment_to_lvl5PuzzleBluetoothFragment)
        }

        binding.btnMoosePaper.setOnClickListener {
            dialogManager.startDialog(requireActivity(), "lvl5_moose_paper")
        }

        binding.btnMap.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl5Fragment_to_lvl5MapFragment)
        }

        binding.btnChest.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl5Fragment_to_lvl5PuzzleChestFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        saveRepo.saveCurrentLevel(5)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}