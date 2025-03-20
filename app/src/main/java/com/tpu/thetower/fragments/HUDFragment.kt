package com.tpu.thetower.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import com.tpu.thetower.DialogManager
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.LevelAccessManager
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentHudBinding

class HUDFragment : Fragment(R.layout.fragment_hud) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val binding = FragmentHudBinding.bind(view)
        val btnToTitle: Button = binding.btnToTitlescreen
        val btnHint : Button = binding.btnHint
        val ivAccessCard : ImageView = binding.ivAccessCard
        val tvTestHint : TextView  = binding.tvTestHintRecovery

        val btnTestUpgrAccessLvl = binding.btnTestUpgrAccessLvl
        btnTestUpgrAccessLvl.setOnClickListener {
            LevelAccessManager.upgradeAccessLvl(this)
        }

        btnToTitle.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_global_titleScreenFragment)
            FragmentManager.hideHUD(requireActivity())
            FragmentManager.hideGoBackArrow(requireActivity())
        }

        requireActivity().supportFragmentManager
            .setFragmentResultListener("accessCardUpgrading", viewLifecycleOwner) { _, bundle ->
            val accessCardImgId = bundle.getInt("accessCardImgId")
            ivAccessCard.setImageResource(accessCardImgId)
        }


        btnHint.setOnClickListener {
            val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fcv_bg)
            val currMainFragment = (navHostFragment as? NavHostFragment)?.childFragmentManager?.fragments?.lastOrNull() as? Hintable
            currMainFragment?.useHint()
            if (currMainFragment == null)
                DialogManager.startDialog(requireActivity(), "no_hints")
        }


        requireActivity().supportFragmentManager
            .setFragmentResultListener("hintImgUpdating", viewLifecycleOwner) { _, bundle ->
                val step = bundle.getString("step")
                tvTestHint.text = step
            }

    }

}