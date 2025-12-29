package com.tpu.thetower.fragments.lvl1

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.Fragment
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl1Binding
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.managers.SaveRepository
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

        if (loadManager.getPuzzleStatus(1, "chandelier") == "completed") {
            binding.ivBg.setImageResource(R.drawable.lvl1_after_clicks)
            binding.btnChandelier.visibility = View.GONE
            binding.btnNpcReceptionist.visibility = View.GONE
            binding.btnAccessCard.visibility = View.VISIBLE
        }
    }

    private fun setListeners() {
        binding.btnNpcReceptionist.setOnClickListener {
            when (loadManager.getCurrentDialog(1, 0)) {
                0 -> {
                    dialogManager.startDialog(requireActivity(), "lvl1_npc_receptionist")
                    saveRepo.saveLevelCompletedStatus(1)
                }
                1 -> dialogManager.startDialog(requireActivity(), "lvl1_npc_receptionist_2")
            }
        }

        binding.btnChandelier.setOnClickListener {
            clickCount++
            if (clickCount == 1) {
                val timer = object : CountDownTimer(5000, 1000) {
                    override fun onTick(millisUntilFinished: Long) = Unit
                    override fun onFinish() { clickCount = 0 }
                }
                timer.start()
            } else if (clickCount == 5) {
                binding.ivBg.animate()
                    .alpha(0f)
                    .setDuration(1500)
                    .withEndAction {
                        binding.ivBg.setImageResource(R.drawable.lvl1_after_clicks)
                        binding.ivBg.alpha = 1f
                        binding.btnChandelier.visibility = View.GONE
                        binding.btnNpcReceptionist.visibility = View.GONE
                        binding.btnAccessCard.visibility = View.VISIBLE
                        saveRepo.savePuzzleData(1, "chandelier", status = "completed")
                    }
                    .start()
            }
        }

        binding.btnAccessCard.setOnClickListener {
            binding.ivAccessCard.visibility = View.VISIBLE
        }

        binding.ivAccessCard.setOnClickListener {
            binding.ivAccessCard.visibility = View.GONE
            binding.btnAccessCard.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        saveRepo.saveCurrentLevel(1)
    }

}