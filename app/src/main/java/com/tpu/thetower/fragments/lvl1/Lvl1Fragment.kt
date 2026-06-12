package com.tpu.thetower.fragments.lvl1

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.animation.addListener
import androidx.fragment.app.Fragment
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl1Binding
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.models.PuzzleStatus
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl1Fragment : Fragment(R.layout.fragment_lvl1) {

    private lateinit var binding: FragmentLvl1Binding

    @Inject lateinit var musicManager: MusicManager
    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var dialogManager: DialogManager

    private var clickCount = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLvl1Binding.bind(view)

        setListeners()

        if (loadManager.getPuzzleStatus(1, "chandelier") == PuzzleStatus.COMPLETED.value) {
            binding.ivBg.setImageResource(R.drawable.lvl1_no_card)
            binding.btnChandelier.visibility = View.GONE
            binding.btnNpcReceptionist.visibility = View.GONE
            binding.btnAccessCard.visibility = View.GONE
        }
        else if (loadManager.getPuzzleStatus(1, "chandelier") == PuzzleStatus.IN_PROGRESS.value) {
            binding.ivBg.setImageResource(R.drawable.lvl1_after_clicks)
            binding.btnChandelier.visibility = View.GONE
            binding.btnNpcReceptionist.visibility = View.GONE
            binding.btnAccessCard.visibility = View.VISIBLE
        }

        if (loadManager.getCurrentDialogIndex(1, "start") == 0)
            dialogManager.startDialog(requireActivity(), "lvl1_start")

        // todo
        saveRepo.saveLevelCompletedStatus(3)
    }

    private fun setListeners() {
        binding.btnNpcReceptionist.setOnClickListener {
            when (loadManager.getCurrentDialogIndex(1, "receptionist")) {
                0 -> {
                    dialogManager.startDialog(requireActivity(), "lvl1_receptionist")
                    saveRepo.saveLevelCompletedStatus(1)
                }
                1 -> dialogManager.startDialog(requireActivity(), "lvl1_npc_receptionist_repeat")
            }
        }

        if (loadManager.isLevelCompleted(3)) {
            binding.btnChandelier.setOnClickListener {
                clickCount++
                if (clickCount == 1) {
                    val timer = object : CountDownTimer(5000, 1000) {
                        override fun onTick(millisUntilFinished: Long) = Unit
                        override fun onFinish() {
                            clickCount = 0
                        }
                    }
                    timer.start()
                } else if (clickCount == 5) {
                    val root = binding.root // корневой ViewGroup

                    // Тряска по X и Y одновременно
                    val shakeX = ObjectAnimator.ofFloat(root, "translationX",
                        0f, -20f, 20f, -15f, 15f, -8f, 8f, 0f).apply { duration = 380 }
                    val shakeY = ObjectAnimator.ofFloat(root, "translationY",
                        0f, -6f, 6f, -4f, 4f, 0f).apply { duration = 380 }

                    // Scale "удар" — экран чуть вздрагивает
                    val scaleUp = ObjectAnimator.ofPropertyValuesHolder(root,
                        PropertyValuesHolder.ofFloat("scaleX", 1f, 1.04f),
                        PropertyValuesHolder.ofFloat("scaleY", 1f, 1.04f)
                    ).apply { duration = 150; startDelay = 200 }
                    val scaleDown = ObjectAnimator.ofPropertyValuesHolder(root,
                        PropertyValuesHolder.ofFloat("scaleX", 1.04f, 1f),
                        PropertyValuesHolder.ofFloat("scaleY", 1.04f, 1f)
                    ).apply { duration = 100; startDelay = 350 }

                    // Вспышка
                    val flash = binding.viewFlash
                    val flashIn  = ObjectAnimator.ofFloat(flash, "alpha", 0f, 1f).apply { duration = 60; startDelay = 320 }
                    val flashOut = ObjectAnimator.ofFloat(flash, "alpha", 1f, 0f).apply {
                        duration = 300; startDelay = 380
                        addListener(onStart = {
                            binding.ivBg.setImageResource(R.drawable.lvl1_after_clicks)
                            binding.btnChandelier.visibility = View.GONE
                            binding.btnNpcReceptionist.visibility = View.GONE
                            binding.btnAccessCard.visibility = View.VISIBLE
                        })
                    }

                    AnimatorSet().apply {
                        playTogether(shakeX, shakeY, scaleUp, scaleDown, flashIn, flashOut)
                        start()
                    }
                    saveRepo.savePuzzleStatus(1, "chandelier", status = PuzzleStatus.IN_PROGRESS.value)
                }
            }
        }

        binding.btnAccessCard.setOnClickListener {
            binding.ivAccessCard.visibility = View.VISIBLE
            saveRepo.savePuzzleStatus(1, "chandelier", status = PuzzleStatus.COMPLETED.value)
            saveRepo.saveLevelCompletedStatus(4)
            dialogManager.startDialog(requireActivity(), "lvl1_after_clicks")
        }

        binding.ivAccessCard.setOnClickListener {
            binding.ivAccessCard.visibility = View.GONE
            binding.btnAccessCard.visibility = View.GONE
            binding.ivBg.setImageResource(R.drawable.lvl1_no_card)
            loadManager.changeAccessCardNumber(5)
        }
    }


    override fun onResume() {
        super.onResume()
        saveRepo.saveCurrentLevel(1)
    }

}