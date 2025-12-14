package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.LoadManager
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl4BookcaseBinding

class Lvl4BookcaseFragment : Fragment(R.layout.fragment_lvl4_bookcase) {

    private lateinit var binding: FragmentLvl4BookcaseBinding

    private lateinit var btnBookHelp: Button
    private lateinit var btnBookBlur: Button
    private lateinit var btnBookAskii: Button
    private lateinit var btnBookQr: Button
    private lateinit var btnBookHistory: Button
    private lateinit var btnBookBabel: Button


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl4BookcaseBinding.bind(view)

        bindView()
        setListeners()

        FragmentManager.showGoBackArrow(requireActivity())
    }

    private fun bindView() {
        btnBookHelp = binding.btnBookHelp
        btnBookBlur = binding.btnBookBlur
        btnBookAskii = binding.btnBookAskii
        btnBookQr = binding.btnBookQr
        btnBookHistory = binding.btnBookHistory
        btnBookBabel = binding.btnBookBabel
    }

    private fun setListeners() {

        btnBookHelp.setOnClickListener {
            openBook("help")
        }

        btnBookBlur.setOnClickListener {
            openBook("blur")
        }

        btnBookAskii.setOnClickListener {
            val book: String = if (LoadManager.getPuzzleStatus(
                    requireActivity(),
                    4,
                    "askiibtn"
                ) == "locked"
            ) "askii_a" else "askii_b"
            openBook(book)
        }

        btnBookQr.setOnClickListener {
            openBook("qr")
        }

        btnBookHistory.setOnClickListener {
            openBook("history")
        }

        btnBookBabel.setOnClickListener {
            openBook("babel")
        }
    }

    private fun openBook(book: String) {
        FragmentManager.changeBG(this, R.id.action_lvl4BookcaseFragment_to_booksFragment)
        requireActivity().supportFragmentManager
            .setFragmentResult("bookOpening", bundleOf("book" to book))
    }

}