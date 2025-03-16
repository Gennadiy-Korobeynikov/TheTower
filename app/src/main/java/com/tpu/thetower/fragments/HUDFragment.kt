package com.tpu.thetower.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.tpu.thetower.DialogManager
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentHudBinding

class HUDFragment : Fragment(R.layout.fragment_hud) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentHudBinding.bind(view)
        val btnToTitle: Button = binding.btnToTitlescreen
        val btnHint : Button = binding.btnHint
        val ivAccessCard : ImageView = binding.ivAccessCard

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
            DialogManager.startDialog(requireActivity(),"test")
        }
    }

}