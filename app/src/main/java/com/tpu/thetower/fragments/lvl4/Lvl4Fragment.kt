package com.tpu.thetower.fragments.lvl4

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.databinding.FragmentLvl4Binding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl4Fragment : Fragment(R.layout.fragment_lvl4) {

    private var _binding: FragmentLvl4Binding? = null
    private val binding get() = _binding!!

    @Inject lateinit var musicManager: MusicManager
    @Inject lateinit var soundManager: SoundManager
    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var loadManager: LoadManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLvl4Binding.bind(view)

        setListeners()
        handleSounds()

        if (loadManager.getPuzzleStatus(4, "chess") == "completed") {
            binding.btnChess.visibility = View.GONE
            binding.btnChessboard.visibility = View.GONE
            binding.ivBg.setImageResource(R.drawable.lvl4_bg_chess_completed)
            binding.btnTimeline.visibility = View.VISIBLE
        }

        if (loadManager.getPuzzleStatus(4, "timeline") == "completed") {
            binding.btnTimeline.visibility = View.GONE
            binding.ivBg.setImageResource(R.drawable.lvl4_bg_timeline_completed)
            binding.btnRaven.visibility = View.VISIBLE
            binding.btnSequencePaper.visibility = View.VISIBLE
            binding.btnBookcase.visibility = View.VISIBLE
        }
    }

    private fun setListeners() {

        binding.btnChess.setOnClickListener {
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

    private fun handleSounds() {
        soundManager.init()
//        soundManager.loadSound(
//            requireContext(), listOf(
//                R.raw.sound_of_a_flashlight,
//                R.raw.sound_of_an_elevator_door_opening
//            )
//        )
    }

    override fun onResume() {
        super.onResume()
        saveRepo.saveCurrentLevel(4)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}