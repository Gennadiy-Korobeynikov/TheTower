package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf

import androidx.fragment.app.Fragment
import com.tpu.thetower.FragmentManager

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



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLvl4Binding.bind(view)
        bindView()
        setListeners()
        handleSounds()
    }

    private fun bindView() {
        btnToBookBabel = binding.btnToBookBabel
        btnToBookAskii = binding.btnToBookAskii
        btnToBookQr = binding.btnToBookQr
        btnToBookBlur = binding.btnToBookBlur
        btnToBookHistory = binding.btnToBookHistory
    }

    private fun setListeners() {
        btnToBookBabel.setOnClickListener {
            openBook("babel")
        }

        btnToBookAskii.setOnClickListener {
            openBook("askii")
        }

    }

    private fun openBook(book : String) {
        FragmentManager.changeBG(this,R.id.action_lvl4Fragment_to_booksFragment)
        requireActivity().supportFragmentManager
            .setFragmentResult("bookOpening", bundleOf("book" to book))
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


//    override fun onResume() {
//        super.onResume()
//
//        saveManager = SaveManager.getInstance()
//        saveManager.saveCurrentLevel(requireContext(), 4)
//    }

}