package com.tpu.thetower.fragments.lvl4

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl4RavenBinding
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.models.PuzzleStatus
import com.tpu.thetower.utils.getOrCreateBlur
import com.tpu.thetower.viewmodels.BlurViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl4RavenFragment : Fragment(R.layout.fragment_lvl4_raven), Hintable {

    private var _binding: FragmentLvl4RavenBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var dialogManager: DialogManager
    @Inject lateinit var hintManagerFactory: HintManager.Factory

    private val blurVM: BlurViewModel by navGraphViewModels(R.id.nav_graph)

    private lateinit var hintManager: HintManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLvl4RavenBinding.bind(view)

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

        setListeners()

        UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)

        hintManager = hintManagerFactory.create(
            hints = listOf(
                "lvl4_askiiBtn_hint1", "lvl4_askiiBtn_hint2"
            ),
            level = 4,
            puzzle = "askiibtn"
        )

        if (loadManager.getPuzzleStatus(4, "askiibtn") == PuzzleStatus.IN_PROGRESS.value) {
            binding.ivRaven.setImageResource(R.drawable.lvl4_raven_off)
        }
    }

    private fun setListeners() {
        binding.btnRaven.setOnClickListener {
            val dialog: String

            if (loadManager.getPuzzleStatus(4, "askiibtn") == PuzzleStatus.LOCKED.value) {
                binding.ivRaven.setImageResource(R.drawable.lvl4_raven_off)
                saveRepo.savePuzzleStatus(4, "askiibtn", status = PuzzleStatus.IN_PROGRESS.value)
                dialog = "lvl4_puzzle1_askii"
            } else {
                binding.ivRaven.setImageResource(R.drawable.lvl4_raven_on)
                saveRepo.savePuzzleStatus(4, "askiibtn", status = PuzzleStatus.LOCKED.value)
                dialog = "lvl4_puzzle1_normal"
            }
            dialogManager.startDialog(requireActivity(), dialog)
        }
    }

    override fun useHint() {
        hintManager.useHint(requireActivity())
    }

    override fun skipPuzzle() {
        Snackbar.make(requireView(), "Не нужно для прохождения", Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}