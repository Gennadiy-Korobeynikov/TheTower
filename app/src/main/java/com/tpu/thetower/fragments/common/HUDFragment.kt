package com.tpu.thetower.fragments.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentHudBinding
import com.tpu.thetower.managers.AppPreferences
import com.tpu.thetower.managers.ImageUpdateDispatcher
import com.tpu.thetower.managers.UiVisibilityController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HUDFragment : Fragment(R.layout.fragment_hud) {

    private lateinit var binding: FragmentHudBinding

    @Inject lateinit var dialogManager: DialogManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHudBinding.bind(view)

        setListeners()

    }

    private fun setListeners() {
        binding.btnMenu.setOnClickListener {
            UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.MENU)
            ImageUpdateDispatcher.updateProgressBar(this)
        }

        requireActivity().supportFragmentManager
            .setFragmentResultListener("accessCardUpgrading", viewLifecycleOwner) { _, bundle ->
                val accessCardImgId = bundle.getInt("accessCardImgId")
                if (accessCardImgId == 0) binding.ivAccessCard.visibility = View.GONE
                else {
                    binding.ivAccessCard.setImageResource(accessCardImgId)
                    binding.ivAccessCard.visibility = View.VISIBLE
                }
            }

        binding.btnHint.setOnClickListener {
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.fcv_bg)
            val currMainFragment =
                (navHostFragment as? NavHostFragment)?.childFragmentManager?.fragments?.lastOrNull() as? Hintable

            currMainFragment?.useHint()
            if (currMainFragment == null) {
                dialogManager.startDialog(requireActivity(), "no_hints")
            }
        }

        requireActivity().supportFragmentManager
            .setFragmentResultListener("hintImgUpdating", viewLifecycleOwner) { _, bundle ->
                val step = bundle.getInt("step")
                when (step) {
                    1 -> binding.ivHint.setImageResource(R.drawable.hint0)
                    2 -> binding.ivHint.setImageResource(R.drawable.hint1)
                    3 -> binding.ivHint.setImageResource(R.drawable.hint2)
                    4 -> binding.ivHint.setImageResource(R.drawable.hint3)
                    5, 0 -> binding.ivHint.setImageResource(R.drawable.hint4_full)
                }
            }

        if (AppPreferences(requireContext()).isDevMode) {
            binding.testBtnSkipPuzzle.visibility = View.VISIBLE
            binding.testBtnSkipPuzzle.setOnClickListener {
                val navHostFragment =
                    requireActivity().supportFragmentManager.findFragmentById(R.id.fcv_bg)
                val currMainFragment =
                    (navHostFragment as? NavHostFragment)?.childFragmentManager?.fragments?.lastOrNull() as? Hintable
                currMainFragment?.skipPuzzle()
            }
        }
    }

}