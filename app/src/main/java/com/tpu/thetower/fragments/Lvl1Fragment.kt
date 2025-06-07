package com.tpu.thetower.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.tpu.thetower.DialogManager
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.LoadManager
import com.tpu.thetower.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.databinding.FragmentLvl1Binding


class Lvl1Fragment : Fragment(R.layout.fragment_lvl1) {

    private lateinit var binding: FragmentLvl1Binding

    private lateinit var musicManager: MusicManager
    private lateinit var saveManager: SaveManager

    private lateinit var btnNpcReceptionist: Button
    private lateinit var btnChandelier: Button
    private lateinit var btnAccessCard: Button

    private lateinit var ivBg: ImageView
    private lateinit var ivAccessCard: ImageView

    private var clickCount = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLvl1Binding.bind(view)
        bindView()
        setListeners()
        handleSounds()

        if (LoadManager.getPuzzleStatus(requireActivity(), 3, "chandelier") == "completed") {
            ivBg.setImageResource(R.drawable.lvl1_after_clicks)
            btnChandelier.visibility = View.GONE
            btnNpcReceptionist.visibility = View.GONE
            btnAccessCard.visibility = View.VISIBLE
        }
    }

    private fun bindView() {
        btnNpcReceptionist = binding.btnNpcReceptionist
        btnChandelier = binding.btnChandelier
        btnAccessCard = binding.btnAccessCard
        ivBg = binding.ivBg
        ivAccessCard = binding.ivAccessCard
    }

    private fun setListeners() {
        btnNpcReceptionist.setOnClickListener {
            when (LoadManager.getCurrentDialog(requireActivity(), 1, 0)) {
                0 -> {
                    DialogManager.startDialog(requireActivity(), "lvl1_npc_receptionist")
                    saveManager.saveLevelStatus(requireContext(), 1)
                }
                1 -> DialogManager.startDialog(requireActivity(), "lvl1_npc_receptionist_2")
            }
        }

        btnChandelier.setOnClickListener {
            clickCount++
            if (clickCount == 1) {
                // Настраиваем время
                val timer = object : CountDownTimer(5000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}

                    override fun onFinish() {
                        clickCount = 0
                    }
                }
                timer.start()
            } else if (clickCount == 5) {
                ivBg.animate()
                    .alpha(0f)
                    .setDuration(1500)
                    .withEndAction {
                        ivBg.setImageResource(R.drawable.lvl1_after_clicks)
                        ivBg.alpha = 1f
                        btnChandelier.visibility = View.GONE
                        btnNpcReceptionist.visibility = View.GONE
                        btnAccessCard.visibility = View.VISIBLE
                        saveManager.savePuzzleData(requireContext(), 1, "chandelier", status = "completed")
                    }
                    .start()
            }
        }

        btnAccessCard.setOnClickListener {
            ivAccessCard.visibility = View.VISIBLE
        }

        ivAccessCard.setOnClickListener {
            ivAccessCard.visibility = View.GONE
            btnAccessCard.visibility = View.GONE
        }
    }

    private fun handleSounds() {
        musicManager = MusicManager.getInstance()
    }


    override fun onResume() {
        super.onResume()

        saveManager = SaveManager.getInstance()
        saveManager.saveCurrentLevel(requireContext(), 1)
    }

}