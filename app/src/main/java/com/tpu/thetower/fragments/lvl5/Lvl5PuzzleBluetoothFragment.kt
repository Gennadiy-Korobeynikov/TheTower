package com.tpu.thetower.fragments.lvl5

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.R
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.databinding.FragmentLvl5PuzzleBluetoothBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl5PuzzleBluetoothFragment : Fragment(R.layout.fragment_lvl5_puzzle_bluetooth) {

    private lateinit var binding: FragmentLvl5PuzzleBluetoothBinding

    private lateinit var bluetoothReceiver: BroadcastReceiver
    private var receiverRegistered = false

    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var loadManager: LoadManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl5PuzzleBluetoothBinding.bind(view)

        UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)

        setListeners()

        if (loadManager.getPuzzleStatus(5, "bluetooth") == "completed") {
            binding.ivBg.setImageResource(R.drawable.lvl5_fish_bluetooth)
        }
    }

    private fun setListeners() {
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        bluetoothReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val state = intent?.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                if (state == BluetoothAdapter.STATE_ON || state == BluetoothAdapter.STATE_OFF) {
                    binding.ivBg.setImageResource(R.drawable.lvl5_fish_bluetooth)
                    saveRepo.savePuzzleData(5, "bluetooth", status = "completed")
                }
            }
        }
        requireContext().registerReceiver(bluetoothReceiver, filter)
        receiverRegistered = true
    }

    override fun onDestroyView() {
        if (receiverRegistered) {
            runCatching { requireContext().unregisterReceiver(bluetoothReceiver) }
            receiverRegistered = false
        }
        super.onDestroyView()
    }
}