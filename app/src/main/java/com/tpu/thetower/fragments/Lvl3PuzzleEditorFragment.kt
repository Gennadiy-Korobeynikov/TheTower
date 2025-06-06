package com.tpu.thetower.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.LoadManager
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.databinding.FragmentLvl3Binding
import com.tpu.thetower.databinding.FragmentLvl3PuzzleEditorBinding


class Lvl3PuzzleEditorFragment : Fragment(R.layout.fragment_lvl3_puzzle_editor), Hintable {

    private lateinit var binding: FragmentLvl3PuzzleEditorBinding

    private lateinit var btnPaste: Button
    private lateinit var btnFirstLayer: Button
    private lateinit var btnSecondLayer: Button

    private lateinit var ivBg: ImageView

    private lateinit var saveManager: SaveManager
    private lateinit var hintManager: HintManager

    private var keyLayers = mutableListOf<Int>()
    private var currentIndex = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl3PuzzleEditorBinding.bind(view)

        bindView()
        setListeners()
        saveManager = SaveManager.getInstance()

        hintManager = HintManager(
            listOf(
                "lvl3_puzzle3_hint1",
                "lvl3_puzzle3_hint2",
                "lvl3_puzzle3_hint3",
                "lvl3_puzzle3_hint4",
                "lvl3_puzzle3_hint5",
            ),
            LoadManager.getPuzzleUsedHintsCount(requireActivity(), 3, "lock model"),
            3, "lock model")

        if (LoadManager.getPuzzleStatus(requireActivity(), 3, "lock model") == "completed") {
           Paste()
        }
        FragmentManager.showGoBackArrow(requireActivity())
    }

    private fun bindView() {
        btnPaste = binding.btnPaste
        btnFirstLayer = binding.btnFirstLayer
        btnSecondLayer = binding.btnSecondLayer
        ivBg = binding.ivBg
    }

    private fun setListeners() {

        btnPaste.setOnClickListener {
            if (LoadManager.getPuzzleStatus(requireActivity(), 3, "lock model") == "in_progress") {
                Paste()
                saveManager.savePuzzleData(requireContext(), 3, "lock model", status = "completed")
                hintManager = HintManager(
                    listOf("lvl3_puzzle3_hint6",),
                    LoadManager.getPuzzleUsedHintsCount(requireActivity(), 3, "lock model after pasted"),
                    3, "lock model after pasted")
            } else {
                val snackBar = Snackbar.make(
                    ivBg,
                    getString(R.string.lvl3_paste),
                    Toast.LENGTH_SHORT
                )
                snackBar.show()
            }
        }

        btnFirstLayer.setOnClickListener {
            ivBg.setImageResource(R.drawable.lvl3_model_front)
        }

        btnSecondLayer.setOnClickListener {
            ivBg.setImageResource(R.drawable.lvl3_model_back)
        }
    }

    private fun Paste() {
        ivBg.setImageResource(R.drawable.lvl3_model_front)
        btnPaste.visibility = View.GONE
        btnFirstLayer.visibility = View.VISIBLE
        btnSecondLayer.visibility = View.VISIBLE
    }


    override fun useHint() {
        hintManager.useHint(requireActivity())
    }

}