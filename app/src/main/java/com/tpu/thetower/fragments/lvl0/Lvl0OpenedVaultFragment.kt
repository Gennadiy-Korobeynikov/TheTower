package com.tpu.thetower.fragments.lvl0

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl0CompletedBinding
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.UiVisibilityController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl0OpenedVaultFragment : Fragment(R.layout.fragment_lvl0_completed) {

    private var _binding: FragmentLvl0CompletedBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var dialogManager: DialogManager
    @Inject lateinit var loadManager: LoadManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLvl0CompletedBinding.bind(view)

        setListeners()

        UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
    }

    private fun setListeners() {
        binding.btnAccessCard.setOnClickListener {
            dialogManager.startDialog(requireActivity(), "lvl0_access_card_got")
            loadManager.changeAccessCardNumber(1)
            saveRepo.saveLevelCompletedStatus(0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}