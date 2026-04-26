package com.tpu.thetower.fragments.lvl4

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl4SequencePaperBinding
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.utils.getOrCreateBlur
import com.tpu.thetower.viewmodels.BlurViewModel

class Lvl4SequencePaperFragment : Fragment(R.layout.fragment_lvl4_sequence_paper) {

    private var _binding: FragmentLvl4SequencePaperBinding? = null
    private val binding get() = _binding!!

    private val blurVM: BlurViewModel by navGraphViewModels(R.id.nav_lvl4)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLvl4SequencePaperBinding.bind(view)

        setListeners()

        UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)

        val levelSnapshot = blurVM.getBlur(Lvl4Fragment.KEY_LVL4_SNAPSHOT)
            ?: error("Snapshot must be set before opening puzzle")

        val blur = getOrCreateBlur(
            blurVM = blurVM,
            blurKey = Lvl4Fragment.KEY_LVL4_BLUR,
            sourceBitmap = levelSnapshot,
            radius = 220f,
            context = requireContext()
        )

        binding.ivBg.setImageBitmap(blur)
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