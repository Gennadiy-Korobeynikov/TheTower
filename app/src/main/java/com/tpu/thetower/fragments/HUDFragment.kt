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

    private lateinit var ivHint: ImageView
    private lateinit var ivAccessCard: ImageView

    private lateinit var btnMenu: Button
    private lateinit var btnHint: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHudBinding.bind(view)

        bindView()
        setListeners()
//        val tvTestHint: TextView = binding.tvTestHintRecovery
//        val ivDraggable: ImageView = binding.ivDraggable
//
//        val btnTestUpgrAccessLvl = binding.btnTestUpgrAccessLvl
//        btnTestUpgrAccessLvl.setOnClickListener {
//            LevelAccessManager.upgradeAccessLvl(this)
//        }

    }

    private fun setListeners() {
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

//        requireActivity().supportFragmentManager
//            .setFragmentResultListener("drag&drop", viewLifecycleOwner) { _, bundle ->
//                val dragAndDropImg = bundle.getInt("dragAndDropImg")
//                when (dragAndDropImg) {
//                    0 -> ivDraggable.visibility = View.GONE
//                    R.drawable.lvl3_puzzle_sleeping_pills -> {
//                        ivDraggable.setImageResource(dragAndDropImg)
//                        ivDraggable.visibility = View.VISIBLE
//                    }
//                }
//            }


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
                val step = bundle.getInt("step")
                when (step) {
                    5 -> ivHint.setImageResource(R.drawable.hint0)
                    4 -> ivHint.setImageResource(R.drawable.hint1)
                    3 -> ivHint.setImageResource(R.drawable.hint2)
                    2 -> ivHint.setImageResource(R.drawable.hint3)
                    1, 0 -> ivHint.setImageResource(R.drawable.hint4_full)
                }
            }

    }

    private fun bindView() {
        ivHint = binding.ivHint
        ivAccessCard = binding.ivAccessCard
        btnHint = binding.btnHint
        btnMenu = binding.btnMenu
    }

}