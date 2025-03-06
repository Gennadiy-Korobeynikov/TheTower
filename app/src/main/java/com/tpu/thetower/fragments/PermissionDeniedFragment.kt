package com.tpu.thetower.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentDialogBinding
import com.tpu.thetower.databinding.FragmentPermissionDeniedBinding

class PermissionDeniedFragment : Fragment(R.layout.fragment_permission_denied) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding : FragmentPermissionDeniedBinding = FragmentPermissionDeniedBinding.bind(view)

        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:com.tpu.thetower"))


        val btnOpenSysSettings : Button = binding.btnOpenSysSettings
        val tvDeny = binding.tvDeny

        btnOpenSysSettings.setOnClickListener {
            startActivity(intent)
        }

        tvDeny.setOnClickListener {
            FragmentManager.hidePermissionDeniedFragment(requireActivity())
        }
    }

}