package com.tpu.thetower.fragments.lvl4

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.databinding.FragmentLvl4Binding

class Lvl4Fragment : Fragment(R.layout.fragment_lvl4) {

    private lateinit var binding: FragmentLvl4Binding

    private lateinit var musicManager: MusicManager
    private lateinit var soundManager: SoundManager
    private val saveRepo: SaveRepository = SaveRepository.getInstance()

    private lateinit var btnToBookBabel: Button
    private lateinit var btnToBookAskii: Button
    private lateinit var btnToBookQr: Button
    private lateinit var btnToBookBlur: Button
    private lateinit var btnToBookHistory: Button
    private lateinit var btnToBookHelp: Button

    private lateinit var btnToChessboardPuzzle: Button
    private lateinit var btnToTimeLinePuzzle1: Button

    private lateinit var btnChess: Button
    private lateinit var btnChessboard: Button
    private lateinit var btnTimeline: Button
    private lateinit var btnRaven: Button
    private lateinit var btnBookcase: Button
    private lateinit var btnSequencePaper: Button

    private lateinit var ivBg: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLvl4Binding.bind(view)
        bindView()
        setListeners()
        handleSounds()

        if (LoadManager.getPuzzleStatus(requireActivity(), 4, "chess") == "completed") {
            btnChess.visibility = View.GONE
            btnChessboard.visibility = View.GONE
            ivBg.setImageResource(R.drawable.lvl4_bg_chess_completed)
            btnTimeline.visibility = View.VISIBLE
        }

        if (LoadManager.getPuzzleStatus(requireActivity(), 4, "timeline") == "completed") {
            btnTimeline.visibility = View.GONE
            ivBg.setImageResource(R.drawable.lvl4_bg_timeline_completed)
            btnRaven.visibility = View.VISIBLE
            btnSequencePaper.visibility = View.VISIBLE
            btnBookcase.visibility = View.VISIBLE
        }
    }

    private fun bindView() {
        btnChess = binding.btnChess
        btnChessboard = binding.btnChessboard
        btnTimeline = binding.btnTimeline
        btnRaven = binding.btnRaven
        btnBookcase = binding.btnBookcase
        btnSequencePaper = binding.btnSequencePaper
        ivBg = binding.ivBg
    }

    private fun setListeners() {

        btnChess.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl4Fragment_to_lvl4PuzzleChessboardFragment)
        }

        btnChessboard.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl4Fragment_to_lvl4ChessFragment)
        }

        btnTimeline.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl4Fragment_to_lvl4TimelineFragment)
        }

        btnRaven.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl4Fragment_to_lvl4RavenFragment)
        }

        btnBookcase.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl4Fragment_to_lvl4BookcaseFragment)
        }

        btnSequencePaper.setOnClickListener {
            FragmentNavigation.changeBG(this, R.id.action_lvl4Fragment_to_lvl4SequencePaperFragment)
        }
    }

    private fun handleSounds() {
        musicManager = MusicManager.getInstance()
        soundManager = SoundManager.getInstance()
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
        saveRepo.saveCurrentLevel(requireActivity(), 4)
    }

}