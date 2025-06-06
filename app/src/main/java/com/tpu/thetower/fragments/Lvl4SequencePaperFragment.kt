package com.tpu.thetower.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl4SequencePaperBinding

class Lvl4SequencePaperFragment : Fragment(R.layout.fragment_lvl4_sequence_paper) {

    private lateinit var binding: FragmentLvl4SequencePaperBinding

    private lateinit var ivBg: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl4SequencePaperBinding.bind(view)

        bindView()
        setListeners()

        FragmentManager.showGoBackArrow(requireActivity())
    }

    private fun bindView() {
        ivBg = binding.ivBg
    }

    private fun setListeners() {
        ivBg.setOnClickListener {
            FragmentManager.goBack(this)
        }
    }

}