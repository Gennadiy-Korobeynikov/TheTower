package com.tpu.thetower.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.transition.Visibility
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.PermissionManager
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl0Binding
import com.tpu.thetower.devicemanagers.FlashlightManager


class Lvl0Fragment : Fragment(R.layout.fragment_lvl0) {

    private lateinit var flashlightManager: FlashlightManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentLvl0Binding.bind(view)
        val btnToElevator : Button = binding.btnToElevator
        val btnToPuzzle1 : Button = binding.btnToPuzzle1
        //val btnTestDialog : Button = binding.btnTestDialog
        val ivDarkness : ImageView = binding.ivDarkness
        val ivDarknessFlashlight = binding.ivDarknessFlashlight
        val btnLightOn : ImageButton = binding.btnLightOn


        if (FragmentManager.light) {
            ivDarkness.visibility = View.GONE
            ivDarknessFlashlight.visibility = View.GONE
            btnLightOn.visibility = View.GONE
        }

        FragmentManager.hideGoBackArrow(requireActivity())


        btnToElevator.setOnClickListener {
            FragmentManager.changeBG(this,R.id.action_global_elevatorFragment)
            FragmentManager.showGoBackArrow(requireActivity())
        }

        btnToPuzzle1.setOnClickListener {
            FragmentManager.changeBG(this,R.id.action_lvl0Fragment_to_lvl0Puzzle1Fragment)
            FragmentManager.showGoBackArrow(requireActivity())
            //getPermissions()
        }

//        btnTestDialog.setOnClickListener {
//            FragmentManager.showDialog(requireActivity())
//            requireActivity().supportFragmentManager.setFragmentResult("testt", bundleOf("test" to "Джон"))
//
//        }

        ivDarkness.setOnClickListener {
            FragmentManager.showDialog(requireActivity())
            requireActivity().supportFragmentManager.setFragmentResult("testt", bundleOf("test" to "Да тут же кромешная тьма!\nНужен свет, иначе я себе точно что-то сломаю"))
            if (ContextCompat.checkSelfPermission( requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) //Временная заглушка для демо
                getPermissions()
        }

        btnLightOn.setOnClickListener {
            FragmentManager.light = true
            ivDarknessFlashlight.visibility = View.GONE
            btnLightOn.visibility = View.GONE
            FragmentManager.showDialog(requireActivity())
            requireActivity().supportFragmentManager.setFragmentResult("testt", bundleOf("test" to "Ааргх. Как ярко!\n Стоп, я, что, в подвале?.." ))

        }


        flashlightManager = FlashlightManager(requireContext()) { isFlashlightOn ->
            requireActivity().runOnUiThread {
                if (isFlashlightOn && !FragmentManager.light) {
                    FragmentManager.showDialog(requireActivity())
                    requireActivity().supportFragmentManager.setFragmentResult("testt", bundleOf("test" to "Откуда идёт этот свет?\n Хотя неважно, теперь я вижу выключатель" ))
                    ivDarkness.visibility = View.GONE
                } else {
                    if (!FragmentManager.light)
                        ivDarkness.visibility = View.VISIBLE
                }
            }
        }


    }

    private fun getPermissions() {
        FragmentManager.showPermissionRequestFragment(requireActivity())
    }


    override fun onDestroy() {
        super.onDestroy()
        flashlightManager.unregister()
    }



}




