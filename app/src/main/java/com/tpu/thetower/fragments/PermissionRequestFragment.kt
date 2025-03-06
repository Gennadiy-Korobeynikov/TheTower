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
import com.tpu.thetower.PermissionManager
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentDialogBinding
import com.tpu.thetower.databinding.FragmentPermissionDeniedBinding
import com.tpu.thetower.databinding.FragmentPermissionRequestBinding

class PermissionRequestFragment : Fragment(R.layout.fragment_permission_request) {

    private lateinit var permissionManager  : PermissionManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding : FragmentPermissionRequestBinding = FragmentPermissionRequestBinding.bind(view)


        permissionManager = PermissionManager(requireActivity(),requireActivity())

        val btnOk = binding.btnOk

        btnOk.setOnClickListener {
            permissionManager.getPermission(android.Manifest.permission.CAMERA)
            FragmentManager.hidePermissionRequestFragment(requireActivity())
        }

    }

}