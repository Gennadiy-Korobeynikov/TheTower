package com.tpu.thetower.fragments.lvl4

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl4BookcaseBinding
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.ImageUpdateDispatcher
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.models.PuzzleStatus
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl4BookcaseFragment : Fragment(R.layout.fragment_lvl4_bookcase) {

    private var _binding: FragmentLvl4BookcaseBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var loadManager: LoadManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLvl4BookcaseBinding.bind(view)

        setListeners()

        UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
    }

    private fun setListeners() {

        binding.btnBookHelp.setOnClickListener {
            openBook("help")
        }

        binding.btnBookBlur.setOnClickListener {
            openBook("blur")
        }

        binding.btnBookAskii.setOnClickListener {
            val book: String = if (loadManager.getPuzzleStatus(4, "askiibtn") == PuzzleStatus.LOCKED.value)
                "askii_a" else "askii_b"
            openBook(book)
        }

        binding.btnBookQr.setOnClickListener {
            openBook("qr")
        }

        binding.btnBookHistory.setOnClickListener {
            openBook("history")
        }

        binding.btnBookBabel.setOnClickListener {
            openBook("babel")
        }
    }

    private fun openBook(book: String) {
        FragmentNavigation.changeBG(this, R.id.action_lvl4BookcaseFragment_to_booksFragment)
        ImageUpdateDispatcher.openBook(this, book)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}