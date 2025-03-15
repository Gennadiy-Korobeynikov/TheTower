package com.tpu.thetower.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.tpu.thetower.DialogManager
import com.tpu.thetower.FragmentManager

import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentDialogBinding
import com.tpu.thetower.models.Dialog


class DialogFragment(
    private val dialog: Dialog
) : Fragment(R.layout.fragment_dialog) {

    private lateinit var binding : FragmentDialogBinding
    private var currentIndex = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDialogBinding.bind(view)

        val tvName = binding.tvName
        val tvDialogText = binding.tvDialogText
        val ivAvatar = binding.ivAvatar
        val layoutDialog = binding.layoutDialog

        FragmentManager.showDialog(requireActivity())

//        requireActivity().supportFragmentManager
//            .setFragmentResultListener("dialog", viewLifecycleOwner) { _, bundle ->
//            val text = bundle.getString("test")
//            tvName.text = "Джон"
//            tvDialogText.text = text
//        }

        fun updateDialog() {
            // Ещё есть реплики
            if (currentIndex < dialog.messages.size) {
                tvDialogText.text = dialog.messages[currentIndex]
                tvName.text = dialog.speakers[currentIndex].name
                ivAvatar.setImageResource(dialog.speakers[currentIndex].avatarId)
                currentIndex++
            // Полседняя реплика
            } else {
                FragmentManager.hideDialog(requireActivity())  // Закрываем диалог
                dialog.onDialogEnd()  // Вызываем функцию завершения
            }
        }


        layoutDialog.setOnClickListener {
            //FragmentManager.showDialog(requireActivity())
            updateDialog()
        }



        updateDialog() // Показать первую реплику


    }

}