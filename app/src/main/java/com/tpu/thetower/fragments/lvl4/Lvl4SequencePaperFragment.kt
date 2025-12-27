package com.tpu.thetower.fragments.lvl4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl4SequencePaperBinding

class Lvl4SequencePaperFragment : Fragment(R.layout.fragment_lvl4_sequence_paper) {

    private var _binding: FragmentLvl4SequencePaperBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLvl4SequencePaperBinding.bind(view)

        setListeners()

        UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
    }

    private fun setListeners() {
        binding.ivBg.setOnClickListener {
            FragmentNavigation.goBack(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}