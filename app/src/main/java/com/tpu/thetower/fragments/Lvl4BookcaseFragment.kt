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
    private lateinit var btnBook1: Button
    private lateinit var btnBook2: Button
    private lateinit var btnBook3: Button
    private lateinit var btnBook4: Button
    private lateinit var btnBook5: Button


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl4BookcaseBinding.bind(view)

        bindView()
        setListeners()

        FragmentManager.showGoBackArrow(requireActivity())
    }

    private fun bindView() {
        btnBookHelp = binding.btnBookHelp
        btnBook1 = binding.btnBook1
        btnBook2 = binding.btnBook2
        btnBook3 = binding.btnBook3
        btnBook4 = binding.btnBook4
        btnBook5 = binding.btnBook5
    }

    private fun setListeners() {

        btnBookHelp.setOnClickListener {
            openBook("help")
        }

        // Честно, не знаю, что за что отвечает, так что не рискну добавлять


        val book: String = if (LoadManager.getPuzzleStatus(
                requireActivity(),
                4,
                "askiibtn"
            ) == "locked"
        ) "askii_a" else "askii_b"

    }

    private fun openBook(book: String) {
        FragmentManager.changeBG(this, R.id.action_lvl4BookcaseFragment_to_booksFragment)
        requireActivity().supportFragmentManager
            .setFragmentResult("bookOpening", bundleOf("book" to book))
    }

}