//package com.tpu.thetower.fragments.common
//
//import android.os.Bundle
//import android.view.View
//import android.widget.SeekBar
//import androidx.fragment.app.Fragment
//import com.tpu.thetower.R
//import com.tpu.thetower.databinding.FragmentSettingsBinding
//import com.tpu.thetower.managers.MusicManager
//import com.tpu.thetower.managers.SaveRepository
//import com.tpu.thetower.managers.SoundManager
//import com.tpu.thetower.managers.UiVisibilityController
//import dagger.hilt.android.AndroidEntryPoint
//import javax.inject.Inject
//
//@AndroidEntryPoint
//class SettingsFragment : Fragment(R.layout.fragment_settings) {
//
//    private lateinit var binding: FragmentSettingsBinding
//
//    @Inject
//    lateinit var saveRepo: SaveRepository
//
//    @Inject
//    lateinit var musicManager: MusicManager
//
//    @Inject
//    lateinit var soundManager: SoundManager
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding = FragmentSettingsBinding.bind(view)
//
//        setInitialState()
//        setListeners()
//    }
//
//    private fun setInitialState() {
//        val gameData = saveRepo.get()
//        binding.musicVolumeSeekBar.progress = (gameData.gameSettings.musicVolume * 100).toInt()
//        binding.soundVolumeSeekBar.progress = (gameData.gameSettings.soundVolume * 100).toInt()
//    }
//
//    private fun setListeners() {
//        binding.musicVolumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                val volume = progress / 100f
//                saveRepo.saveMusicVolume(volume)
//                musicManager.setVolume(volume)
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar) = Unit
//
//            override fun onStopTrackingTouch(seekBar: SeekBar) = Unit
//        })
//
//        binding.soundVolumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                val volume = progress / 100f
//                saveRepo.saveSoundVolume(volume)
//                soundManager.setVolume(volume)
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar) = Unit
//
//            override fun onStopTrackingTouch(seekBar: SeekBar) = Unit
//        })
//
//        binding.btnBack.setOnClickListener {
//            UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.SETTINGS)
//        }
//    }
//}