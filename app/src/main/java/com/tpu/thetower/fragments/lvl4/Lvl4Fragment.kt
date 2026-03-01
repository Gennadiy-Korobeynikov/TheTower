package com.tpu.thetower.fragments.lvl4

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl4Binding
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.models.PuzzleStatus
import com.tpu.thetower.utils.BlurUtils
import com.tpu.thetower.utils.getOrCreateBlur
import com.tpu.thetower.viewmodels.BlurViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl4Fragment : Fragment(R.layout.fragment_lvl4), Hintable {
    private var _binding: FragmentLvl4Binding? = null
    private val binding get() = _binding!!

    @Inject lateinit var musicManager: MusicManager
    @Inject lateinit var soundManager: SoundManager
    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var dialogManager : DialogManager

    private val blurVM: BlurViewModel by navGraphViewModels(R.id.nav_graph)

    private companion object {
        private const val KEY_LVL4_SNAPSHOT = "lvl4_snapshot"
        private const val KEY_LVL4_BLUR = "lvl4_blur"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLvl4Binding.bind(view)

        setListeners()

        if (loadManager.getPuzzleStatus(4, "chess") == PuzzleStatus.COMPLETED.value) {
            binding.btnWallMechanism.visibility = View.GONE
            binding.btnChessboard.visibility = View.GONE
            binding.btnSequencePaper.visibility = View.VISIBLE
            binding.ivBg.setImageResource(R.drawable.lvl4_bg_chess_completed)
        }

        if (loadManager.getPuzzleStatus(4, "timeline") == PuzzleStatus.COMPLETED.value) {
            binding.btnTimeline.visibility = View.GONE
            binding.ivRavenHead.setImageResource(
                if (loadManager.getPuzzleStatus(4, "askiibtn") == PuzzleStatus.LOCKED.value)
                    R.drawable.lvl4_raven_on
                else
                    R.drawable.lvl4_raven_off
            )

            binding.ivRavenHead.visibility = View.VISIBLE
            binding.btnRaven.visibility = View.VISIBLE
        }

        binding.root.post {
            if (!isAdded) return@post

            if (blurVM.getBlur(KEY_LVL4_SNAPSHOT) == null) {
                val snapshot = BlurUtils.captureSnapshot(binding.root)
                blurVM.setBlur(KEY_LVL4_SNAPSHOT, snapshot)
            }

            val snapshot = blurVM.getBlur(KEY_LVL4_SNAPSHOT) ?: return@post
            getOrCreateBlur(blurVM, blurKey = KEY_LVL4_BLUR, sourceBitmap = snapshot, radius = 220f, context = requireContext())
        }
    }

    private fun setListeners() {

        binding.btnWallMechanism.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl4Fragment_to_lvl4PuzzleChessboardFragment)
        }

        binding.btnChessboard.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl4Fragment_to_lvl4ChessFragment)
        }

        binding.btnTimeline.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl4Fragment_to_lvl4TimelineFragment)
        }

        binding.btnRaven.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl4Fragment_to_lvl4RavenFragment)
        }

        binding.btnBookcase.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl4Fragment_to_lvl4BookcaseFragment)
        }

        binding.btnSequencePaper.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl4Fragment_to_lvl4SequencePaperFragment)
        }
    }


    override fun onResume() {
        super.onResume()
        saveRepo.saveCurrentLevel(4)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        blurVM.clearBlur(KEY_LVL4_SNAPSHOT) //todo пока тут
        blurVM.clearBlur(KEY_LVL4_BLUR)
    }

    override fun useHint() {
        dialogManager.startDialog(requireActivity(), "hint_is_not_here")
    }

    override fun skipPuzzle() {
        Snackbar.make(requireView(), "Можно сразу на 1 таж - кликать по люстре", Snackbar.LENGTH_SHORT).show()
    }
}