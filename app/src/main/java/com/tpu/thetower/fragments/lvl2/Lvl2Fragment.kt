package com.tpu.thetower.fragments.lvl2

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl2Binding
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.LevelAccessManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.managers.UiVisibilityController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl2Fragment : Fragment(R.layout.fragment_lvl2), Hintable {

    private lateinit var binding: FragmentLvl2Binding

    @Inject lateinit var musicManager: MusicManager
    @Inject lateinit var soundManager: SoundManager
    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var dialogManager: DialogManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLvl2Binding.bind(view)

        setListeners()
        handleSounds()

        UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)

        if (loadManager.getPuzzleStatus(2, "lock") == "locked") {
            dialogManager.startDialog(requireActivity(), "lvl2_start")
            saveRepo.savePuzzleData(2, "lock", status = "in_progress")
        }

        if (loadManager.getPuzzleStatus(2, "lock") == "completed") {
            binding.btnToPuzzle0Lock.visibility = View.GONE
            binding.btnToPuzzle0Completed.visibility = View.VISIBLE
        }

        if (loadManager.getPuzzleStatus(2, "chat") == "completed") {
            binding.btnToPuzzle2Lock.visibility = View.GONE
            if (!loadManager.isLevelCompleted(2)) {
                binding.btnToPuzzle2Completed.visibility = View.VISIBLE
            }
        }
    }

    private fun setListeners() {
        binding.btnToPuzzle0.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl2Fragment_to_lvl2CaesarFragment)
        }

        binding.btnToPuzzle0Lock.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl2Fragment_to_lvl2PuzzleLockFragment)
        }

        binding.btnToPuzzle0Completed.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl2Fragment_to_lvl2PetFragment)
            soundManager.playSound(R.raw.sound_of_drawer_opening)
        }

        binding.btnToPuzzle1.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl2Fragment_to_lvl2PuzzlePasswordFragment)
        }

        binding.btnToPuzzle2Lock.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl2Fragment_to_lvl2PuzzleChatFragment)
        }

        binding.btnToPuzzle2Completed.setOnClickListener {
            UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
            binding.ivAccessCard.visibility = View.VISIBLE
            soundManager.playSound(R.raw.sound_of_drawer_opening)
        }

        binding.ivAccessCard.setOnClickListener {
            binding.ivAccessCard.visibility = View.GONE
            saveRepo.saveLevelStatus(2)
            binding.btnToPuzzle2Completed.visibility = View.GONE
            UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
            LevelAccessManager.upgradeAccessLvl(this, saveRepo)
            soundManager.playSound(R.raw.sound_of_drawer_closing)
        }
    }

    private fun handleSounds() {
        // musicManager/soundManager уже внедрены через DI
        soundManager.init()
        soundManager.loadSound(
            listOf(
                R.raw.sound_of_drawer_opening,
                R.raw.sound_of_drawer_closing
            )
        )
    }

    override fun onResume() {
        super.onResume()
        saveRepo.saveCurrentLevel(2)
    }

    override fun useHint() {
        if (loadManager.isLevelCompleted(2)) {
            dialogManager.startDialog(requireActivity(), "no_hints")
        } else {
            dialogManager.startDialog(requireActivity(), "hint_is_not_here")
        }
    }

    override fun skipPuzzle() {
        Snackbar.make(requireView(), "Левый замок; Комп; Правый замок; ", Snackbar.LENGTH_SHORT).show()
    }

}