package com.tpu.thetower.fragments

import android.annotation.SuppressLint
import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import android.view.View
import android.view.View.DragShadowBuilder
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

    private lateinit var binding: FragmentHudBinding
    private lateinit var ivDraggable: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHudBinding.bind(view)
        val btnMenu: Button = binding.btnMenu
        val btnHint : Button = binding.btnHint
        val ivAccessCard : ImageView = binding.ivAccessCard
        val tvTestHint : TextView  = binding.tvTestHintRecovery
        ivDraggable = binding.ivDraggable

        val btnTestUpgrAccessLvl = binding.btnTestUpgrAccessLvl
        btnTestUpgrAccessLvl.setOnClickListener {
            LevelAccessManager.upgradeAccessLvl(this)
        }

        btnMenu.setOnClickListener {
            FragmentManager.showMenu(requireActivity())
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

        fun getDraggableImageView(): View {
            return ivDraggable
        }


    }


}