package com.tpu.thetower.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.tpu.thetower.DialogManager
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.LevelAccessManager
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.databinding.FragmentLvl0CompletedBinding

class Lvl0CompletedFragment : Fragment(R.layout.fragment_lvl0_completed) {

    private lateinit var binding: FragmentLvl0CompletedBinding

    private lateinit var btnAccessCard: Button

    private lateinit var saveManager: SaveManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl0CompletedBinding.bind(view)

        saveManager = SaveManager.getInstance()

        bindView()
        setListeners()

        FragmentManager.showGoBackArrow(requireActivity())
    }

    private fun bindView() {
        btnAccessCard = binding.btnAccessCard
    }

    private fun setListeners() {
        btnAccessCard.setOnClickListener {
            if (LevelAccessManager.currentAccessLvl == 0) {
                DialogManager.startDialog(requireActivity(), "lvl0_access_card")
                LevelAccessManager.upgradeAccessLvl(this)
                saveManager.saveLevelStatus(requireContext(), 0)
            }
            DialogManager.startDialog(requireActivity(), "lvl0_access_card_got")
        }
    }
}