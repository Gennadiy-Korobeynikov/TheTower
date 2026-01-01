package com.tpu.thetower.fragments.lvl3

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl3PuzzleEditorBinding
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.models.PuzzleStatus
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl3PuzzleEditorFragment : Fragment(R.layout.fragment_lvl3_puzzle_editor), Hintable {

    private var _binding: FragmentLvl3PuzzleEditorBinding? = null
    private val binding get() = _binding!!

    private lateinit var hintManager: HintManager

    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var hintManagerFactory: HintManager.Factory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLvl3PuzzleEditorBinding.bind(view)

        setListeners()

        hintManager = hintManagerFactory.create(
            hints = listOf(
                "lvl3_puzzle3_hint1",
                "lvl3_puzzle3_hint2",
                "lvl3_puzzle3_hint3",
                "lvl3_puzzle3_hint4",
                "lvl3_puzzle3_hint5",
            ),
            level = 3,
            puzzle = "lock model"
        )

        if (loadManager.getPuzzleStatus(3, "lock model") == PuzzleStatus.COMPLETED.value) {
            paste()
        }
        UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
    }

    private fun setListeners() {
        binding.btnPaste.setOnClickListener {
            if (loadManager.getPuzzleStatus(3, "lock model") == PuzzleStatus.IN_PROGRESS.value) {
                paste()
                saveRepo.savePuzzleData(3, "lock model", status = PuzzleStatus.COMPLETED.value)
            } else {
                Snackbar.make(binding.ivBg, getString(R.string.lvl3_paste), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnFirstLayer.setOnClickListener { binding.ivBg.setImageResource(R.drawable.lvl3_model_front) }
        binding.btnSecondLayer.setOnClickListener { binding.ivBg.setImageResource(R.drawable.lvl3_model_back) }
    }

    private fun paste() {
        binding.ivBg.setImageResource(R.drawable.lvl3_model_front)
        binding.btnPaste.visibility = View.GONE
        binding.btnFirstLayer.visibility = View.VISIBLE
        binding.btnSecondLayer.visibility = View.VISIBLE
        hintManager = hintManagerFactory.create(
            hints = listOf("lvl3_puzzle3_hint6"),
            level = 3,
            puzzle = "lock model after pasted"
        )
    }

    override fun useHint() {
        hintManager.useHint(requireActivity())
    }

    override fun skipPuzzle() {
        paste()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}