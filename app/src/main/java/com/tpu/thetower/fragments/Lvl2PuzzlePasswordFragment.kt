package com.tpu.thetower.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tpu.thetower.DialogManager
import com.tpu.thetower.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.LoadManager
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl2PuzzlePasswordBinding
import com.tpu.thetower.puzzles.Lvl2PuzzleLock1

class Lvl2PuzzlePasswordFragment : Fragment(R.layout.fragment_lvl2_puzzle_password) , Hintable{

    private lateinit var binding: FragmentLvl2PuzzlePasswordBinding
    private val pinCells = mutableListOf<TextView>()
    private val puzzle: Puzzle = Lvl2PuzzleLock1(2, "password")


    private lateinit var tvPin1: TextView
    private lateinit var tvPin2: TextView
    private lateinit var tvPin3: TextView
    private lateinit var tvPin4: TextView
    private lateinit var tvPin5: TextView
    private lateinit var tvPin6: TextView
    private lateinit var tvPin7: TextView
    private lateinit var tvPassword: TextView

    private lateinit var pinContainer: LinearLayout
    private lateinit var hiddenInput: EditText
    private lateinit var ivDialog: ImageView
    private lateinit var hintManager: HintManager



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl2PuzzlePasswordBinding.bind(view)

        bindView()
        setListeners()

        pinCells.addAll(listOf(tvPin1, tvPin2, tvPin3, tvPin4, tvPin5, tvPin6, tvPin7))

        if (LoadManager.getPuzzleStatus(requireActivity(), 2, "password") == "locked") {
            showKeyboard()
            hintManager = HintManager(listOf("lvl2_puzzle2_hint1", "lvl2_puzzle2_hint2", "lvl2_puzzle2_hint3",),
                LoadManager.getPuzzleUsedHintsCount(requireActivity(),2,"password"),
                2,"password")
        }

        if (LoadManager.getPuzzleStatus(requireActivity(), 2, "password") == "completed") {
            hintManager = HintManager(listOf("lvl2_puzzle3_hint1", "lvl2_puzzle3_hint2",
                "lvl2_puzzle3_hint3","lvl2_puzzle3_hint4","lvl2_puzzle3_hint5"),
                LoadManager.getPuzzleUsedHintsCount(requireActivity(),2,"chat"),
                2,"chat")
            completed()
        }
    }

    private fun bindView() {
        tvPin1 = binding.tvPin1
        tvPin2 = binding.tvPin2
        tvPin3 = binding.tvPin3
        tvPin4 = binding.tvPin4
        tvPin5 = binding.tvPin5
        tvPin6 = binding.tvPin6
        tvPin7 = binding.tvPin7
        tvPassword = binding.tvPassword

        hiddenInput = binding.hiddenInput
        pinContainer = binding.pinContainer
        ivDialog = binding.ivDialog

    }

    private fun setListeners() {
        pinContainer.setOnClickListener {
            showKeyboard()
        }
        hiddenInput.requestFocus()
        hiddenInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s?.toString() ?: ""
                // Обновление отображения ячеек
                pinCells.forEachIndexed { index, textView ->
                    textView.text = if (index < input.length) input[index].toString() else ""
                }

                if (puzzle.checkSolution(requireContext(), input)) {
                    completed()
                    hideKeyboard()
                }


            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun showKeyboard() {
        hiddenInput.apply {
            requestFocus()
            post {
                context?.let {
                    val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                }
            }
        }
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun completed() {
        hintManager = HintManager(listOf("lvl2_puzzle3_hint1", "lvl2_puzzle3_hint2",
            "lvl2_puzzle3_hint3","lvl2_puzzle3_hint4","lvl2_puzzle3_hint5"),
            LoadManager.getPuzzleUsedHintsCount(requireActivity(),2,"chat"),
            2,"chat")
        hiddenInput.visibility = View.GONE
        pinContainer.visibility = View.GONE
        tvPassword.visibility = View.GONE
        ivDialog.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()

        hideKeyboard()
    }

    override fun useHint() {
        if (LoadManager.getPuzzleStatus(requireActivity(), 2, "caesar") == "completed")
            hintManager.useHint(requireActivity())
        else
            DialogManager.startDialog(requireActivity(), "hint_is_not_here")
    }
}