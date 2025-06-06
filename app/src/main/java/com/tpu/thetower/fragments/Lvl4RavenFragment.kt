package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.tpu.thetower.DialogManager
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.LoadManager
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.databinding.FragmentLvl4RavenBinding

class Lvl4RavenFragment : Fragment(R.layout.fragment_lvl4_raven), Hintable {

    private lateinit var binding: FragmentLvl4RavenBinding
    private lateinit var saveManager: SaveManager
    private lateinit var hintManager: HintManager

    private lateinit var btnRaven: Button

    private lateinit var ivBg: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl4RavenBinding.bind(view)

        bindView()
        setListeners()

        FragmentManager.showGoBackArrow(requireActivity())

        hintManager = HintManager(
            listOf(
                "lvl4_askiiBtn_hint1", "lvl4_askiiBtn_hint2"
            ),
            LoadManager.getPuzzleUsedHintsCount(requireActivity(), 4, "askiibtn"),
            4, "askiibtn"
        )

        saveManager = SaveManager.getInstance()
    }

    private fun bindView() {
        btnRaven = binding.btnRaven
        ivBg = binding.ivBg
    }

    private fun setListeners() {
        btnRaven.setOnClickListener {
            var dialog: String

            if (LoadManager.getPuzzleStatus(requireActivity(), 4, "askiibtn") == "locked") {
                ivBg.setImageResource(R.drawable.lvl4_raven_switch_2)
                saveManager.savePuzzleData(requireContext(), 4, "askiibtn", status = "in_progress")
                dialog = "lvl4_puzzle1_askii"
            } else {
                ivBg.setImageResource(R.drawable.lvl4_raven_switch_1)
                saveManager.savePuzzleData(requireContext(), 4, "askiibtn", status = "locked")
                dialog = "lvl4_puzzle1_normal"
            }
            DialogManager.startDialog(requireActivity(), dialog)
        }
    }

    override fun useHint() {
        hintManager.useHint(requireActivity())
    }

}