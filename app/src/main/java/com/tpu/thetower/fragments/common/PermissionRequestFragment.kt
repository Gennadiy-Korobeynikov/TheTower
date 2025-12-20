package com.tpu.thetower.fragments.common

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.tpu.thetower.managers.PermissionManager
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentPermissionRequestBinding
import com.tpu.thetower.managers.UiVisibilityController

class PermissionRequestFragment : Fragment(R.layout.fragment_permission_request) {

    private lateinit var permissionManager  : PermissionManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding : FragmentPermissionRequestBinding = FragmentPermissionRequestBinding.bind(view)


        permissionManager = PermissionManager(requireActivity(),requireActivity())


        val btnOk = binding.btnOk

        btnOk.setOnClickListener {
            permissionManager.getPermission(Manifest.permission.CAMERA)
            UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.PERMISSION_REQUEST)
        }

    }

}