package com.tpu.thetower.fragments.lvl2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.tpu.thetower.managers.DialogManager

import com.tpu.thetower.Hintable
import com.tpu.thetower.managers.LevelAccessManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.databinding.FragmentLvl2Binding
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.UiVisibilityController


class Lvl2Fragment : Fragment(R.layout.fragment_lvl2), Hintable {

    private lateinit var binding: FragmentLvl2Binding

    private lateinit var musicManager: MusicManager
    private lateinit var soundManager: SoundManager

    private lateinit var btnToPuzzle0: Button
    private lateinit var btnToPuzzle0Lock: Button
    private lateinit var btnToPuzzle0Completed: Button
    private lateinit var btnToPuzzle1: Button
    private lateinit var btnToPuzzle2Lock: Button
    private lateinit var btnToPuzzle2Completed: Button

    private lateinit var ivAccessCard: ImageView

    private val saveRepo: SaveRepository = SaveRepository.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLvl2Binding.bind(view)
        bindView()
        setListeners()
        handleSounds()

        UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)

        if (LoadManager.getPuzzleStatus(requireActivity(), 2, "lock") == "locked") {
            DialogManager.startDialog(requireActivity(), "lvl2_start")
            saveRepo.savePuzzleData(requireActivity(), 2, "lock", status = "in_progress")
        }

        if (LoadManager.getPuzzleStatus(requireActivity(), 2, "lock") == "completed") {
            btnToPuzzle0Lock.visibility = View.GONE
            btnToPuzzle0Completed.visibility = View.VISIBLE
        }

        if (LoadManager.getPuzzleStatus(requireActivity(), 2, "chat") == "completed") {
            btnToPuzzle2Lock.visibility = View.GONE
            if (!LoadManager.getLevelStatus(requireActivity(), 2))
                btnToPuzzle2Completed.visibility = View.VISIBLE
        }
    }

    private fun bindView() {
        btnToPuzzle0 = binding.btnToPuzzle0
        btnToPuzzle0Lock = binding.btnToPuzzle0Lock
        btnToPuzzle0Completed = binding.btnToPuzzle0Completed
        btnToPuzzle1 = binding.btnToPuzzle1
        btnToPuzzle2Lock = binding.btnToPuzzle2Lock
        btnToPuzzle2Completed = binding.btnToPuzzle2Completed
        ivAccessCard = binding.ivAccessCard
    }

    private fun setListeners() {
        btnToPuzzle0.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl2Fragment_to_lvl2CaesarFragment)
        }

        btnToPuzzle0Lock.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl2Fragment_to_lvl2PuzzleLockFragment)
        }

        btnToPuzzle0Completed.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl2Fragment_to_lvl2PetFragment)
            soundManager.playSound(R.raw.sound_of_drawer_opening)
        }

        btnToPuzzle1.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl2Fragment_to_lvl2PuzzlePasswordFragment)
        }

        btnToPuzzle2Lock.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl2Fragment_to_lvl2PuzzleChatFragment)
        }

        btnToPuzzle2Completed.setOnClickListener {
            UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
            ivAccessCard.visibility = View.VISIBLE
            soundManager.playSound(R.raw.sound_of_drawer_opening)
        }

        ivAccessCard.setOnClickListener {
            ivAccessCard.visibility = View.GONE
            saveRepo.saveLevelStatus(requireActivity(), 2)
            btnToPuzzle2Completed.visibility = View.GONE
            UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
            LevelAccessManager.upgradeAccessLvl(this)
            soundManager.playSound(R.raw.sound_of_drawer_closing)
        }

    }

    private fun handleSounds() {
        musicManager = MusicManager.getInstance()
        soundManager = SoundManager.getInstance()
        soundManager.init()
        soundManager.loadSound(
            requireContext(), listOf(
                R.raw.sound_of_drawer_opening,
                R.raw.sound_of_drawer_closing
            )
        )
    }

    override fun onResume() {
        super.onResume()
        saveRepo.saveCurrentLevel(requireActivity(), 2)
    }

    override fun useHint() {
        if (LoadManager.isLevelCompleted(requireActivity(), 2)) {
            DialogManager.startDialog(requireActivity(), "no_hints")
        } else
            DialogManager.startDialog(requireActivity(), "hint_is_not_here")
    }

}