package com.tpu.thetower.fragments

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.LoadManager
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.databinding.FragmentLvl3Puzzle0Binding
import kotlin.math.pow
import kotlin.math.sqrt

class Lvl3Puzzle0Fragment : Fragment(R.layout.fragment_lvl3_puzzle0), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var lastUpdate: Long = 0
    private var lastShake: Long = 0
    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f

    private lateinit var iv0: ImageView
    private lateinit var iv1: ImageView

    private lateinit var binding: FragmentLvl3Puzzle0Binding

    private lateinit var saveManager: SaveManager

    // Пороговые значения
    private val shakeThreshold = 3500 // Чувствительность к тряске

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl3Puzzle0Binding.bind(view)

        iv0 = binding.iv0
        iv1 = binding.iv1

        saveManager = SaveManager.getInstance()

        when (LoadManager.getLevelProgress(requireActivity(), 3)) {
            1,2,3,4,5 -> {
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
        saveManager.savePuzzleData(requireContext(), 3, 0)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Не используется, но требуется реализовать
    }
}