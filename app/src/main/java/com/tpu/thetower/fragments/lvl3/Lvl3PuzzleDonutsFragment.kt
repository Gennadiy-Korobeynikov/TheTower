package com.tpu.thetower.fragments.lvl3

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl3PuzzleDonutsBinding
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.sqrt

@AndroidEntryPoint
class Lvl3PuzzleDonutsFragment :
    Fragment(R.layout.fragment_lvl3_puzzle_donuts),
    SensorEventListener,
    Hintable {

    private var _binding: FragmentLvl3PuzzleDonutsBinding? = null
    private val binding get() = _binding!!

    private lateinit var sensorManager: SensorManager
    private var lastUpdate: Long = 0
    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f

    private lateinit var hintManager: HintManager

    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var soundManager: SoundManager
    @Inject lateinit var hintManagerFactory: HintManager.Factory

    private val shakeThreshold = 3500

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLvl3PuzzleDonutsBinding.bind(view)

        handleSounds()

        when (loadManager.getPuzzleStatus(3, "donuts")) {
            "in_progress" -> {
                hintManager = hintManagerFactory.create(
                    hints = listOf(
                        "lvl3_puzzle0_hint1",
                        "lvl3_puzzle0_hint2",
                        "lvl3_puzzle0_hint3",
                        "lvl3_puzzle0_hint4",
                    ),
                    level = 3,
                    puzzle = "donuts"
                )
            }
            "completed" -> {
                hintManager = hintManagerFactory.create(
                    hints = listOf("lvl3_puzzle0_hint5", "lvl3_puzzle0_hint6", "lvl3_puzzle0_hint7"),
                    level = 3,
                    puzzle = "donuts after shaking"
                )
                binding.iv0.visibility = View.GONE
                binding.iv1.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_UI
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        saveRepo.saveCurrentLevel(3)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val currentTime = System.currentTimeMillis()
        val diffTime = currentTime - lastUpdate
        if (diffTime <= 100) return
        lastUpdate = currentTime

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val speed = sqrt((x - lastX).pow(2) + (y - lastY).pow(2) + (z - lastZ).pow(2)) / diffTime * 10000
        if (speed > shakeThreshold) {
            soundManager.playSound(R.raw.sound_of_donuts_shaking)
            completed()
        }

        lastX = x
        lastY = y
        lastZ = z
    }

    private fun completed() {
        hintManager = hintManagerFactory.create(
            hints = listOf("lvl3_puzzle0_hint5", "lvl3_puzzle0_hint6", "lvl3_puzzle0_hint7"),
            level = 3,
            puzzle = "donuts after shaking"
        )
        binding.iv0.visibility = View.GONE
        binding.iv1.visibility = View.VISIBLE
        saveRepo.savePuzzleData(3, "donuts", status = "completed")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    override fun useHint() {
        hintManager.useHint(requireActivity())
    }

    private fun handleSounds() {
        soundManager.init(maxStreamsNumber = 1)
        soundManager.loadSound(listOf(R.raw.sound_of_donuts_shaking))
    }

    override fun skipPuzzle() {
        completed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}