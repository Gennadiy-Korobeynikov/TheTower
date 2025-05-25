package com.tpu.thetower

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import kotlin.concurrent.fixedRateTimer

class FragmentManager {

    companion object {

        fun changeBG(from : Fragment, to : Int, bundle: Bundle = Bundle()) {
            from.findNavController().navigate(to, bundle)
        }

        fun goBack(from : Fragment) {
            from.findNavController().popBackStack()
        }

        fun showTitleScreen(activity: Activity) {
            activity.findViewById<FragmentContainerView>(R.id.fcv_title_screen).visibility = View.VISIBLE
            hideHUD(activity)
            hideGoBackArrow(activity)
        }

        fun hideTitleScreen(activity: Activity) {
            activity.findViewById<FragmentContainerView>(R.id.fcv_title_screen).visibility = View.GONE
            showHUD(activity)
            showGoBackArrow(activity)
        }

        fun showMenu(activity: Activity) {
            activity.findViewById<FragmentContainerView>(R.id.fcv_menu).visibility = View.VISIBLE
        }

        fun hideMenu(activity: Activity) {
            activity.findViewById<FragmentContainerView>(R.id.fcv_menu).visibility = View.GONE
        }

        fun showSettings(activity: Activity) {
            hideGoBackArrow(activity)
            hideHUD(activity)
            //changeBG(this, R.id.action_titleScreenFragment_to_settingsFragment)
            activity.findViewById<FragmentContainerView>(R.id.fcv_settings).visibility = View.VISIBLE
        }

        fun hideSettings(activity: Activity) {
            activity.findViewById<FragmentContainerView>(R.id.fcv_settings).visibility = View.GONE
        }

        fun hideHUD(activity : Activity) {
            activity.findViewById<FragmentContainerView>(R.id.fcv_hud).visibility = View.GONE
        }

        private fun showHUD(activity : Activity) {
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

        fun changeAccessCardImg(fragment: Fragment, accessCardImgId : Int, ) {
            fragment.requireActivity().supportFragmentManager
                .setFragmentResult("accessCardUpgrading", bundleOf("accessCardImgId" to accessCardImgId))
        }

        fun changeUnlockedModules(fragment: Fragment, currAccessLevel : Int) {
            fragment.requireActivity().supportFragmentManager
                .setFragmentResult("moduleUnlocking", bundleOf("currAccessLevel" to currAccessLevel))
        }

        fun updateHintStateImg(activity: Activity, step : String ) { // Временно передаём строку, потом - Int

            (activity as? AppCompatActivity)?.supportFragmentManager
                ?.setFragmentResult("hintImgUpdating", bundleOf("step" to step))
        }

        fun changeDragAndDropImg(fragment: Fragment, dragAndDropImg : Int) {
            fragment.requireActivity().supportFragmentManager
                .setFragmentResult("drag&drop", bundleOf("dragAndDropImg" to dragAndDropImg))
        }

        fun updateProgressBar(fragment: Fragment) {
            fragment.requireActivity().supportFragmentManager
                .setFragmentResult("updateProgressBar", bundleOf())
        }
    }
}