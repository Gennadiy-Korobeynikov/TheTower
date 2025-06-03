package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.tpu.thetower.DialogManager
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.LevelAccessManager
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentHudBinding

class HUDFragment : Fragment(R.layout.fragment_hud) {

    private lateinit var binding: FragmentHudBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHudBinding.bind(view)
        val btnMenu: Button = binding.btnMenu
        val btnHint: Button = binding.btnHint
        val ivAccessCard: ImageView = binding.ivAccessCard
        val tvTestHint: TextView = binding.tvTestHintRecovery
        val ivDraggable: ImageView = binding.ivDraggable

        val btnTestUpgrAccessLvl = binding.btnTestUpgrAccessLvl
        btnTestUpgrAccessLvl.setOnClickListener {
            LevelAccessManager.upgradeAccessLvl(this)
        }

        btnMenu.setOnClickListener {
            FragmentManager.showMenu(requireActivity())
            FragmentManager.updateProgressBar(this)
        }

        requireActivity().supportFragmentManager
            .setFragmentResultListener("accessCardUpgrading", viewLifecycleOwner) { _, bundle ->
                val accessCardImgId = bundle.getInt("accessCardImgId")
                if (accessCardImgId == 0) ivAccessCard.visibility = View.GONE
                else {
                    ivAccessCard.setImageResource(accessCardImgId)
                    ivAccessCard.visibility = View.VISIBLE
                }
            }

        requireActivity().supportFragmentManager
            .setFragmentResultListener("drag&drop", viewLifecycleOwner) { _, bundle ->
                val dragAndDropImg = bundle.getInt("dragAndDropImg")
                when (dragAndDropImg) {
                    0 -> ivDraggable.visibility = View.GONE
                    R.drawable.lvl3_puzzle_sleeping_pills -> {
                        ivDraggable.setImageResource(dragAndDropImg)
                        ivDraggable.visibility = View.VISIBLE
                    }
                }
            }


        btnHint.setOnClickListener {
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.fcv_bg)
            val currMainFragment =
                (navHostFragment as? NavHostFragment)?.childFragmentManager?.fragments?.lastOrNull() as? Hintable
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