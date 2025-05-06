package com.tpu.thetower.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
    private lateinit var btnNextLayer: Button
    private lateinit var btnPreviousLayer: Button
    private lateinit var hintManager: HintManager

    private lateinit var tvText: TextView
    private lateinit var ivKey: ImageView

    private lateinit var saveManager: SaveManager

    private var isCopied = false
    private var isPasted = false
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

        isCopied = LoadManager.getPuzzleStatus(requireActivity(), 3, "lock model") == "in_progress"
        isPasted = LoadManager.getPuzzleStatus(requireActivity(), 3, "lock model") == "completed"
        if (isPasted) {
            ivKey.visibility = View.VISIBLE
        }
        keyLayers = mutableListOf(
            R.drawable.lvl3_puzzle4_key_0,
            R.drawable.lvl3_puzzle4_key_1,
            R.drawable.lvl3_puzzle4_key_2,
            R.drawable.lvl3_puzzle4_key_3,
            R.drawable.lvl3_puzzle4_key_4,
            R.drawable.lvl3_puzzle4_key_5
        )
    }

    private fun bindView() {
        btnPaste = binding.btnPaste
        btnNextLayer = binding.btnNextLayer
        btnPreviousLayer = binding.btnPreviousLayer
        tvText = binding.tvText
        ivKey = binding.ivKey
    }

    private fun setListeners() {

        btnPaste.setOnClickListener {
            if (isCopied) {
                isPasted = true
                saveManager.savePuzzleData(requireContext(), 3, "lock model", status = "completed")
                ivKey.visibility = View.VISIBLE
                hintManager = HintManager(
                    listOf("lvl3_puzzle3_hint6",),
                    LoadManager.getPuzzleUsedHintsCount(requireActivity(), 3, "lock model after pasted"),
                    3, "lock model after pasted")
            } else {
                tvText.text = "В буфере обмена ничего нет"
            }
        }

        btnNextLayer.setOnClickListener {
            if (isPasted) {
                if (currentIndex in 0..<keyLayers.size - 1) {
                    currentIndex++
                    ivKey.setImageResource(keyLayers[currentIndex])
                }
            }
        }

        btnPreviousLayer.setOnClickListener {
            if (isPasted) {
                if (currentIndex in 1..keyLayers.size) {
                    currentIndex--
                    ivKey.setImageResource(keyLayers[currentIndex])
            }
            }
        }
    }


    override fun useHint() {
        hintManager.useHint(requireActivity())
    }

}