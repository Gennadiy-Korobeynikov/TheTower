package com.tpu.thetower.fragments.lvl4

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl4BookcaseBinding
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.models.PuzzleStatus
import com.tpu.thetower.utils.getOrCreateBlur
import com.tpu.thetower.viewmodels.BlurViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl4BookcaseFragment : Fragment(R.layout.fragment_lvl4_bookcase), Hintable {

    private var _binding: FragmentLvl4BookcaseBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var hintManagerFactory: HintManager.Factory
    @Inject lateinit var dialogManager : DialogManager


    private val blurVM: BlurViewModel by navGraphViewModels(R.id.nav_lvl4)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLvl4BookcaseBinding.bind(view)

        setListeners()

        UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)

        val levelSnapshot = blurVM.getBlur(Lvl4Fragment.KEY_LVL4_SNAPSHOT)
            ?: error("Snapshot must be set before opening puzzle")

        val blur = getOrCreateBlur(
            blurVM = blurVM,
            blurKey = Lvl4Fragment.KEY_LVL4_BLUR,
            sourceBitmap = levelSnapshot,
            radius = 220f,
            context = requireContext()
        )

        binding.ivBg.setImageBitmap(blur)
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

        binding.ivBg.setOnClickListener {
                dialogManager.startDialog(requireActivity(), "lvl4_wrong_book")
        }
    }

    private fun openBook(book: String) {
        val args = Bundle().apply {
            putString(Lvl4BookFragment.ARG_BOOK_KEY, book)
        }

        FragmentNavigation.changeBG(
            this,
            R.id.action_lvl4BookcaseFragment_to_lvl4BookFragment,
            args
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun useHint() {
        dialogManager.startDialog(requireActivity(), "hint_is_not_here")
    }

    override fun skipPuzzle() {
    }
}