package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.os.bundleOf

import androidx.fragment.app.Fragment
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.LoadManager

import com.tpu.thetower.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.SoundManager
import com.tpu.thetower.databinding.FragmentLvl4Binding


class Lvl4Fragment : Fragment(R.layout.fragment_lvl4) {

    private lateinit var binding: FragmentLvl4Binding

    private lateinit var musicManager: MusicManager
    private lateinit var soundManager: SoundManager
    private lateinit var saveManager: SaveManager

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
        saveManager = SaveManager.getInstance()

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
//        btnToBookBabel.setOnClickListener { openBook("babel") }
//        btnToBookQr.setOnClickListener { openBook("qr") }
//        btnToBookBlur.setOnClickListener { openBook("blur") }
//        btnToBookHistory.setOnClickListener { openBook("history") }
//
//        btnToBookAskii.setOnClickListener {
//
//            //val book : String =  if (LoadManager.isPuzzleCompleted(requireActivity(),4,2)) "askii_a" else "askii_b"
//        val book : String =  if (LoadManager.isASKII) "askii_b" else "askii_a" // TODO Исправить!!!!! длолжно быть через сохранения
//            openBook(book)
//        }

        btnChess.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_lvl4Fragment_to_lvl4PuzzleChessboardFragment)
        }

        btnChessboard.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_lvl4Fragment_to_lvl4ChessFragment)
        }

        btnTimeline.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_lvl4Fragment_to_lvl4TimelineFragment)
        }

        btnRaven.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_lvl4Fragment_to_lvl4RavenFragment)
        }

        btnBookcase.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_lvl4Fragment_to_lvl4BookcaseFragment)
        }

        btnSequencePaper.setOnClickListener {
            FragmentManager.changeBG(this, R.id.action_lvl4Fragment_to_lvl4SequencePaperFragment)
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

        saveManager = SaveManager.getInstance()
        saveManager.saveCurrentLevel(requireContext(), 4)
    }

}