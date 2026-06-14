package com.tpu.thetower.fragments.lvl5

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.tpu.thetower.Hintable
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl5PuzzleMooseBinding
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.models.PuzzleStatus
import com.tpu.thetower.puzzles.Lvl5PuzzleMoose
import com.tpu.thetower.utils.CommonAnimationHelper
import com.tpu.thetower.utils.SoundEffect
import com.tpu.thetower.viewmodels.BlurViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl5PuzzleMooseFragment : Fragment(R.layout.fragment_lvl5_puzzle_moose), Hintable {

    private var _binding: FragmentLvl5PuzzleMooseBinding? = null
    private val binding get() = _binding!!

    private var solution = ""
    private val puzzle: Puzzle = Lvl5PuzzleMoose(5, "moose")

    private lateinit var hintManager: HintManager

    @Inject lateinit var saveRepo : SaveRepository
    @Inject lateinit var soundManager : SoundManager
    @Inject lateinit var hintManagerFactory: HintManager.Factory
    @Inject lateinit var loadManager: LoadManager

    private val blurVM: BlurViewModel by navGraphViewModels(R.id.nav_lvl5)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLvl5PuzzleMooseBinding.bind(view)

        setListeners()

        hintManager = if (loadManager.getPuzzleStatus(5, "bluetooth") == PuzzleStatus.COMPLETED.value) {
            hintManagerFactory.create(
                hints = listOf("lvl5_horns_hint1", "lvl5_horns_hint2", "lvl5_horns_hint3"),
                level = 5,
                puzzle = "moose"
            )
        } else
            hintManagerFactory.create(
                hints = listOf("lvl5_horns_hint1"),
                level = 5,
                puzzle = "moose"
            )
    }

    private fun setListeners() {
        val moose = binding.ivMoose

        fun playAndAppend(bit: Char, drawableOnClick: Int) {
            moose.setImageResource(drawableOnClick)
            soundManager.playSound(SoundEffect.HORNS)

            moose.animate()
                .alpha(1f)
                .setDuration(500)
                .withEndAction {
                    if (isAdded && view != null) {
                        moose.setImageResource(R.drawable.lvl5_moose_base)
                    }
                }

            solution += bit
            check()
        }

        binding.btnLeft.setOnClickListener {
            playAndAppend(bit = '0', drawableOnClick = R.drawable.lvl5_moose_left)
        }

        binding.btnRight.setOnClickListener {
            playAndAppend(bit = '1', drawableOnClick = R.drawable.lvl5_moose_right)
        }
    }


    private fun check() {
        if (solution.length >= 5) {
            if (puzzle.checkSolution(requireActivity(), saveRepo, solution.takeLast(5))) {
                passed()
            }
        }
    }

    private fun passed() {
        //soundManager.playSound()
        CommonAnimationHelper.animatePuzzleCompletion(
            fragment = this,
            mainScreen = binding.mainScreen,
            fragmentRoot = binding.root
        )
        blurVM.clearBlur(Lvl5Fragment.KEY_LVL5_SNAPSHOT)
        blurVM.clearBlur(Lvl5Fragment.KEY_LVL5_BLUR)
    }

    override fun useHint() {
        hintManager.useHint(requireActivity())
    }

    override fun skipPuzzle() {
        passed()
        puzzle.complete(saveRepo)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}