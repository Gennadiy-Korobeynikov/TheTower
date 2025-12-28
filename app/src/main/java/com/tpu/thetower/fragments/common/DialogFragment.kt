package com.tpu.thetower.fragments.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.text.HtmlCompat
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentDialogBinding
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.models.Dialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DialogFragment : Fragment(R.layout.fragment_dialog) {

    private lateinit var binding: FragmentDialogBinding

    @Inject lateinit var dialogManager: DialogManager

    private lateinit var dialog: Dialog

    private lateinit var dialogKey: String

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = requireArguments()

        dialogKey = args.getString(ARG_DIALOG_KEY)
            ?: error("DialogFragment requires dialogKey argument. Use DialogFragment.newInstance(dialogKey).")

        // строим Dialog каждый раз заново из ключа.
        // Так мы гарантируем, что после смены конфигурации восстановится и сам диалог, и callback.
        dialog = dialogManager.buildDialog(requireActivity(), dialogKey)

        currentIndex = savedInstanceState?.getInt(STATE_INDEX)
            ?: args.getInt(ARG_START_INDEX, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_INDEX, currentIndex)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDialogBinding.bind(view)

        val tvName = binding.tvName
        val tvDialogText = binding.tvDialogText
        val ivAvatar = binding.ivAvatar
        val layoutDialog = binding.layoutDialog

        UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.DIALOG)

        fun updateDialog() {
            if (currentIndex < dialog.messages.size) { // Ещё есть реплики
                tvDialogText.text = HtmlCompat.fromHtml(
                    dialog.messages[currentIndex].replace("\n", "<br/>"),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )

                tvName.text = dialog.speakers[currentIndex].name
                ivAvatar.setImageResource(dialog.speakers[currentIndex].avatarId)
                currentIndex++
            } else { // Последняя реплика
                UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.DIALOG)
                dialog.onDialogEnd?.invoke()
            }
        }

        layoutDialog.setOnClickListener { updateDialog() }

        updateDialog()
    }

    companion object {
        private const val ARG_DIALOG_KEY = "dialog_key"
        private const val ARG_START_INDEX = "start_index"

        private const val STATE_INDEX = "state_index"

        fun newInstance(dialogKey: String, startIndex: Int = 0): DialogFragment {
            val f = DialogFragment()
            f.arguments = Bundle().apply {
                putString(ARG_DIALOG_KEY, dialogKey)
                putInt(ARG_START_INDEX, startIndex)
            }
            return f
        }
    }

}