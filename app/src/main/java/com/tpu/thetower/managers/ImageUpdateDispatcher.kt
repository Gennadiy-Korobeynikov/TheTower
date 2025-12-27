package com.tpu.thetower.managers

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

object ImageUpdateDispatcher {

    fun changeAccessCardImg(fragment: Fragment, accessCardImgId: Int) {
        fragment.requireActivity().supportFragmentManager
            .setFragmentResult("accessCardUpgrading", bundleOf("accessCardImgId" to accessCardImgId))
    }

    fun changeUnlockedModules(fragment: Fragment, currAccessLevel: Int) {
        fragment.requireActivity().supportFragmentManager
            .setFragmentResult("moduleUnlocking", bundleOf("currAccessLevel" to currAccessLevel))
    }

    fun updateHintStateImg(activity: Activity, step: Int) {
        (activity as? AppCompatActivity)?.supportFragmentManager
            ?.setFragmentResult("hintImgUpdating", bundleOf("step" to step))
    }

    fun changeDragAndDropImg(fragment: Fragment, dragAndDropImg: Int) {
        fragment.requireActivity().supportFragmentManager
            .setFragmentResult("drag&drop", bundleOf("dragAndDropImg" to dragAndDropImg))
    }

    fun updateProgressBar(fragment: Fragment) {
        fragment.requireActivity().supportFragmentManager
            .setFragmentResult("updateProgressBar", bundleOf())
    }

    fun openBook(fragment: Fragment, book: String) {
        fragment.requireActivity().supportFragmentManager
            .setFragmentResult("bookOpening", bundleOf("book" to book))
    }
}
