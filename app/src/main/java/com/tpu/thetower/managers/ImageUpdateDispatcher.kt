package com.tpu.thetower.managers

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

object ImageUpdateDispatcher {
    fun updateHintStateImg(activity: Activity, step: Int) {
        (activity as? AppCompatActivity)?.supportFragmentManager
            ?.setFragmentResult("hintImgUpdating", bundleOf("step" to step))
    }

    fun updateProgressBar(fragment: Fragment) {
        fragment.requireActivity().supportFragmentManager
            .setFragmentResult("updateProgressBar", bundleOf())
    }
}
