package com.tpu.thetower.fragments.lvl0

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import com.tpu.thetower.managers.DialogManager

import com.tpu.thetower.managers.LevelAccessManager
import com.tpu.thetower.R
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.databinding.FragmentLvl0CompletedBinding
import com.tpu.thetower.managers.UiVisibilityController

class Lvl0CompletedFragment : Fragment(R.layout.fragment_lvl0_completed) {

    private lateinit var binding: FragmentLvl0CompletedBinding

    private lateinit var btnAccessCard: Button

    private val saveRepo: SaveRepository = SaveRepository.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl0CompletedBinding.bind(view)

        bindView()
        setListeners()

        UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
    }

    private fun bindView() {
        btnAccessCard = binding.btnAccessCard
    }

    private fun setListeners() {
        btnAccessCard.setOnClickListener {
            if (LevelAccessManager.currentAccessLvl == 0) {
                DialogManager.startDialog(requireActivity(), "lvl0_access_card")
                LevelAccessManager.upgradeAccessLvl(this)
                saveRepo.saveLevelStatus(requireActivity(), 0)
            }
            DialogManager.startDialog(requireActivity(), "lvl0_access_card_got")
        }
    }
}