package com.tpu.thetower

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.findNavController

class FragmentManager {

    companion object {

        var light : Boolean = false  // Временный костыль для демо

        fun changeBG(from : Fragment, to : Int) {
            from.findNavController().navigate(to)
        }

        fun goBack(from : Fragment) {
            from.findNavController().popBackStack()
        }

        fun hideHUD(activity : Activity) {
            activity.findViewById<FragmentContainerView>(R.id.fcv_hud).visibility = View.GONE
        }

        fun showHUD(activity : Activity) {
            activity.findViewById<FragmentContainerView>(R.id.fcv_hud).visibility = View.VISIBLE
        }

        fun hideGoBackArrow(activity : Activity) {
            activity.findViewById<FragmentContainerView>(R.id.fcv_go_back_arrow).visibility = View.GONE
        }

        fun showGoBackArrow(activity : Activity) {
            activity.findViewById<FragmentContainerView>(R.id.fcv_go_back_arrow).visibility = View.VISIBLE
        }

        fun hideDialog(activity : Activity) {
            activity.findViewById<FragmentContainerView>(R.id.fcv_dialog).visibility = View.GONE
        }

        fun showDialog(activity : Activity) {
            activity.findViewById<FragmentContainerView>(R.id.fcv_dialog).visibility = View.VISIBLE
        }

        fun showPermissionDeniedFragment(activity : Activity) {
            activity.findViewById<FragmentContainerView>(R.id.fcv_permission_denied).visibility = View.VISIBLE
        }

        fun hidePermissionDeniedFragment(activity : Activity) {
            activity.findViewById<FragmentContainerView>(R.id.fcv_permission_denied).visibility = View.GONE
        }

        fun showPermissionRequestFragment(activity : Activity) {
            activity.findViewById<FragmentContainerView>(R.id.fcv_permission_request).visibility = View.VISIBLE
        }

        fun hidePermissionRequestFragment(activity : Activity) {
            activity.findViewById<FragmentContainerView>(R.id.fcv_permission_request).visibility = View.GONE
        }
    }
}