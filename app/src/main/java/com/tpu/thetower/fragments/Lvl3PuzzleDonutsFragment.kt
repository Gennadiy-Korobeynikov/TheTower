package com.tpu.thetower.fragments

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.tpu.thetower.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.LoadManager
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.databinding.FragmentLvl3PuzzleDonutsBinding
import kotlin.math.pow
import kotlin.math.sqrt

class Lvl3PuzzleDonutsFragment : Fragment(R.layout.fragment_lvl3_puzzle_donuts), SensorEventListener, Hintable {
    private lateinit var sensorManager: SensorManager
    private var lastUpdate: Long = 0
    private var lastShake: Long = 0
    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f

    private lateinit var iv0: ImageView
    private lateinit var iv1: ImageView

    private lateinit var binding: FragmentLvl3PuzzleDonutsBinding

    private lateinit var saveManager: SaveManager
    private lateinit var hintManager: HintManager

    // Пороговые значения
    private val shakeThreshold = 3500 // Чувствительность к тряске

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl3PuzzleDonutsBinding.bind(view)

        iv0 = binding.iv0
        iv1 = binding.iv1

        saveManager = SaveManager.getInstance()

        val puzzleStatus = LoadManager.getPuzzleStatus(requireActivity(), 3, "donuts")
        when (puzzleStatus) {
            "in_progress" -> { // До тряски коробки
                hintManager = HintManager(listOf("lvl3_puzzle0_hint1", "lvl3_puzzle0_hint2", "lvl3_puzzle0_hint3","lvl3_puzzle0_hint4",),
                    LoadManager.getPuzzleUsedHintsCount(requireActivity(),3, "donuts"),
                    3,"donuts")
            }
            "completed" -> { // После тряски коробки
                hintManager = HintManager(listOf("lvl3_puzzle0_hint5", "lvl3_puzzle0_hint6", "lvl3_puzzle0_hint7",),
                    LoadManager.getPuzzleUsedHintsCount(requireActivity(),3,"donuts"),
                    3,"donuts")
                iv0.visibility = View.GONE
                iv1.visibility = View.VISIBLE
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
        saveManager.saveCurrentLevel(requireContext(), 3)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val currentTime = System.currentTimeMillis()
            val diffTime = currentTime - lastUpdate

            if (diffTime > 100) { // Обновление каждые 100 мс
                lastUpdate = currentTime

                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val speed = sqrt(
                    (x - lastX).pow(2) +
                            (y - lastY).pow(2) +
                            (z - lastZ).pow(2)
                ) / diffTime * 10000

                if (speed > shakeThreshold) {
                    lastShake = currentTime
                    Completed()
                }

                lastX = x
                lastY = y
                lastZ = z
            }
        }
    }

    private fun Completed() {
        iv0.visibility = View.GONE
        iv1.visibility = View.VISIBLE
        saveManager.savePuzzleData(requireContext(), 3, "donuts", status = "completed")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Не используется, но требуется реализовать
    }

    override fun useHint() {
        hintManager.useHint(requireActivity())
    }
}