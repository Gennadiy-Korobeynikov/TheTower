package com.tpu.thetower.fragments.lvl5

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl5Binding
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.models.PuzzleStatus
import com.tpu.thetower.utils.BlurUtils
import com.tpu.thetower.utils.SoundEffect
import com.tpu.thetower.utils.getOrCreateBlur
import com.tpu.thetower.viewmodels.BlurViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl5Fragment : Fragment(R.layout.fragment_lvl5), Hintable {

    private var _binding: FragmentLvl5Binding? = null
    private val binding get() = _binding!!

    @Inject lateinit var musicManager: MusicManager
    @Inject lateinit var soundManager: SoundManager
    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var dialogManager: DialogManager
    @Inject lateinit var hintManagerFactory: HintManager.Factory

    private lateinit var hintManager: HintManager

    private val blurVM: BlurViewModel by navGraphViewModels(R.id.nav_lvl5)

    companion object {
        const val KEY_LVL5_SNAPSHOT = "lvl5_snapshot"
        const val KEY_LVL5_BLUR = "lvl5_blur"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLvl5Binding.bind(view)

        setListeners()

        if (loadManager.getCurrentDialogIndex(5, "start") == 0) {
            dialogManager.startDialog(requireActivity(), "lvl5_start")
        }

        if (loadManager.getPuzzleStatus(5, "moose") == PuzzleStatus.COMPLETED.value) {
            binding.btnMoose.visibility = View.GONE
            binding.ivMooseWithPaper.visibility = View.VISIBLE
            binding.btnMoosePaper.visibility = View.VISIBLE
            binding.btnFish.visibility = View.GONE
        }

        if (loadManager.getPuzzleStatus(5, "fish rack") == PuzzleStatus.COMPLETED.value) {
            binding.btnFishRack.visibility = View.GONE
            binding.ivFishRack.setImageResource(R.drawable.lvl5_fishes_solved)
            if (loadManager.getCurrentDialogIndex(5, "npc_fisher_reward") == 0) {
                dialogManager.startDialog(requireActivity(), "lvl5_npc_fisher_reward")
            }
        }

        if (loadManager.getPuzzleStatus(5, "chest") == PuzzleStatus.COMPLETED.value) {
            binding.btnChest.visibility = View.GONE
        }


        binding.root.post {
            if (!isAdded) return@post

            if (blurVM.getBlur(KEY_LVL5_SNAPSHOT) == null) {
                val snapshot = BlurUtils.captureSnapshot(binding.root)
                blurVM.setBlur(KEY_LVL5_SNAPSHOT, snapshot)
            }

            val snapshot = blurVM.getBlur(KEY_LVL5_SNAPSHOT) ?: return@post
            getOrCreateBlur(blurVM, blurKey = KEY_LVL5_BLUR, sourceBitmap = snapshot, radius = 220f, context = requireContext())
        }

        setGeneralHints()
    }

    private fun setListeners() {
        binding.btnFisher.setOnClickListener {
            when (loadManager.getPuzzleStatus(5, "fish rack")) {
                PuzzleStatus.COMPLETED.value ->
                        dialogManager.startDialog(requireActivity(), "lvl5_npc_fisher_reward_repeat")

                else -> {
                    if (loadManager.getCurrentDialogIndex(5, "npc_fisher") == 0) {
                        dialogManager.startDialog(requireActivity(), "lvl5_npc_fisher")
                    } else
                        dialogManager.startDialog(requireActivity(), "lvl5_npc_fisher_return")
                }
            }
        }


        binding.btnFishRack.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl5Fragment_to_lvl5FishRackFragment)
        }

        binding.btnMoose.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl5Fragment_to_lvl5PuzzleMooseFragment)
        }

        binding.btnFish.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl5Fragment_to_lvl5PuzzleBluetoothFragment)
        }

        binding.btnMoosePaper.setOnClickListener {
            binding.clMoosePaper.visibility = View.VISIBLE
            UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
            soundManager.playSound(SoundEffect.PAPER)
        }

        binding.clMoosePaper.setOnClickListener {
            binding.clMoosePaper.visibility = View.GONE
            UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
        }

        binding.btnMap.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl5Fragment_to_lvl5MapFragment)
        }

        binding.btnChest.setOnClickListener {
            if (loadManager.getCurrentDialogIndex(5, "chest") == 0) {
                dialogManager.startDialog(requireActivity(), "lvl5_chest")
            }
            FragmentNavigation.changeBG(this, R.id.action_lvl5Fragment_to_lvl5PuzzleChestFragment)
        }
    }

    private fun setGeneralHints() {
        when (PuzzleStatus.COMPLETED.value) {
            loadManager.getPuzzleStatus(5, "fish rack") -> {
                hintManager = hintManagerFactory.create(
                    hints = listOf("lvl5_general_hint4"),
                    level = 5,
                    puzzle = "general1"
                )
            }
            loadManager.getPuzzleStatus(5, "moose") -> {
                hintManager = hintManagerFactory.create(
                    hints = listOf("lvl5_general_hint3"),
                    level = 5,
                    puzzle = "general2"
                )
            }
            loadManager.getPuzzleStatus(5, "bluetooth") -> {
                hintManager = hintManagerFactory.create(
                    hints = listOf("lvl5_general_hint2"),
                    level = 5,
                    puzzle = "general3"
                )
            }
            else -> {
                hintManager = hintManagerFactory.create(
                    hints = listOf("lvl5_general_hint1"),
                    level = 5,
                    puzzle = "general4"
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()

        musicManager.playMusic(R.raw.soundtrack_portraits_in_the_hallway)
        saveRepo.saveCurrentLevel(5)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun useHint() {
        hintManager.useHint(requireActivity())
    }

    override fun skipPuzzle() {
    }
}