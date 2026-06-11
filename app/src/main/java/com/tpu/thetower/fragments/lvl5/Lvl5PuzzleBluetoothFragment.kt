package com.tpu.thetower.fragments.lvl5

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl5PuzzleBluetoothBinding
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.models.PuzzleStatus
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl5PuzzleBluetoothFragment : Fragment(R.layout.fragment_lvl5_puzzle_bluetooth), Hintable {

    private lateinit var binding: FragmentLvl5PuzzleBluetoothBinding

    private lateinit var bluetoothReceiver: BroadcastReceiver
    private var receiverRegistered = false

    private lateinit var hintManager: HintManager

    @Inject lateinit var hintManagerFactory: HintManager.Factory
    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var loadManager: LoadManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl5PuzzleBluetoothBinding.bind(view)

        if (loadManager.getPuzzleStatus(5, "bluetooth") == PuzzleStatus.COMPLETED.value) {
            hintManager = hintManagerFactory.create(
                hints = listOf(
                    "lvl5_bluetoothOn_hint1",
                    "lvl5_bluetoothOn_hint2"
                ),
                level = 5,
                puzzle = "bluetooth_on"
            )
        } else
            hintManager = hintManagerFactory.create(
                hints = listOf(
                    "lvl5_bluetoothOff_hint1",
                    "lvl5_bluetoothOff_hint2",
                    "lvl5_bluetoothOff_hint3"
                ),
                level = 5,
                puzzle = "bluetooth_off"
            )
    }

    override fun onStart() {
        super.onStart()
        registerBluetoothReceiverIfNeeded()
        updateUiForBluetoothState(BluetoothAdapter.getDefaultAdapter()?.state ?: BluetoothAdapter.ERROR)
    }


    private fun ensureReceiverCreated() {
        if (this::bluetoothReceiver.isInitialized) return

        bluetoothReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val state = intent?.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                    ?: BluetoothAdapter.ERROR

                updateUiForBluetoothState(state)

                if (state == BluetoothAdapter.STATE_ON) {
                    saveRepo.savePuzzleStatus(5, "bluetooth", status = PuzzleStatus.COMPLETED.value)
                    updateUiForBluetoothState(state)
                    hintManager = hintManagerFactory.create(
                        hints = listOf(
                            "lvl5_bluetoothOn_hint1",
                            "lvl5_bluetoothOn_hint2"
                        ),
                        level = 5,
                        puzzle = "bluetooth_on"
                    )
                }
            }
        }
    }

    private fun registerBluetoothReceiverIfNeeded() {
        if (receiverRegistered) return
        ensureReceiverCreated()
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        requireContext().registerReceiver(bluetoothReceiver, filter)
        receiverRegistered = true
    }

    private fun unregisterBluetoothReceiverIfNeeded() {
        if (!receiverRegistered) return
        runCatching { requireContext().unregisterReceiver(bluetoothReceiver) }
        receiverRegistered = false
    }

    private fun updateUiForBluetoothState(state: Int) {
        if (loadManager.getPuzzleStatus(5, "bluetooth") == PuzzleStatus.COMPLETED.value) {
            when (state) {
                BluetoothAdapter.STATE_ON -> binding.ivBg.setImageResource(R.drawable.lvl5_fish_bluetooth)
                BluetoothAdapter.STATE_OFF -> binding.ivBg.setImageResource(R.drawable.lvl5_fish_no_bluetooth)
                else -> Unit
            }
        }
    }

    override fun useHint() {
        hintManager.useHint(requireActivity())
    }

    override fun skipPuzzle() {
        saveRepo.savePuzzleStatus(5, "bluetooth", status = PuzzleStatus.COMPLETED.value)
        updateUiForBluetoothState(BluetoothAdapter.STATE_ON)
    }

    override fun onStop() {
        unregisterBluetoothReceiverIfNeeded()
        super.onStop()
    }

    override fun onDestroyView() {
        // на всякий: если onStop не вызвался
        unregisterBluetoothReceiverIfNeeded()
        super.onDestroyView()
    }
}