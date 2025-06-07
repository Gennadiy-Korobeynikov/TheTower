package com.tpu.thetower.fragments

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.LoadManager
import com.tpu.thetower.R
import com.tpu.thetower.SaveManager
import com.tpu.thetower.databinding.FragmentLvl5PuzzleBluetoothBinding

class Lvl5PuzzleBluetoothFragment : Fragment(R.layout.fragment_lvl5_puzzle_bluetooth) {

    private lateinit var binding: FragmentLvl5PuzzleBluetoothBinding

    private lateinit var ivBg: ImageView

    private lateinit var bluetoothReceiver : BroadcastReceiver
    private lateinit var saveManager: SaveManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl5PuzzleBluetoothBinding.bind(view)

        saveManager = SaveManager.getInstance()
        FragmentManager.showGoBackArrow(requireActivity())

        bindView()
        setListeners()

        if (LoadManager.getPuzzleStatus(requireActivity(), 5, "bluetooth") == "completed") {
            ivBg.setImageResource(R.drawable.lvl5_fish_bluetooth)
        }
    }

    private fun bindView() {
        ivBg = binding.ivBg
    }

    private fun setListeners() {
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        bluetoothReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val state = intent?.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                if (state == BluetoothAdapter.STATE_ON || state == BluetoothAdapter.STATE_OFF) {
                    ivBg.setImageResource(R.drawable.lvl5_fish_bluetooth)
                    saveManager.savePuzzleData(requireActivity(), 5, "bluetooth", status = "completed")
                }
            }
        }
        requireContext().registerReceiver(bluetoothReceiver, filter)
    }

}