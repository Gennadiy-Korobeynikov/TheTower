package com.tpu.thetower.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import com.tpu.thetower.FragmentManager

import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentDialogBinding


class DialogFragment : Fragment(R.layout.fragment_dialog) {

    private lateinit var binding : FragmentDialogBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDialogBinding.bind(view)


        val tvName = binding.tvName
        val tvDialogText = binding.tvDialogText
        val imgv_avatar = binding.imgvAvatar
        val layoutDialog = binding.layoutDialog


        requireActivity().supportFragmentManager
            .setFragmentResultListener("testt", viewLifecycleOwner) { _, bundle ->
            val text = bundle.getString("test")
            tvName.text = "Джон"
            tvDialogText.text = text
        }

        layoutDialog.setOnClickListener {
            FragmentManager.hideDialog(requireActivity())
        }


    }

}