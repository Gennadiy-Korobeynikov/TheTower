package com.tpu.thetower.managers

import android.app.Activity
import android.view.View
import androidx.fragment.app.FragmentContainerView
import com.tpu.thetower.R

object UiVisibilityController {

    enum class UiContainer(val id: Int) {
        TITLE(R.id.fcv_title_screen),
        MENU(R.id.fcv_menu),
        TOPBAR_UI(R.id.fcv_topbar_ui),
        GO_BACK_ARROW(R.id.fcv_go_back_arrow),
        DIALOG(R.id.fcv_dialog),
//        PERMISSION_DENIED(R.id.fcv_permission_denied),
//        PERMISSION_REQUEST(R.id.fcv_permission_request)
    }

    private fun Activity.setContainerVisible(container: UiContainer, visible: Boolean) {
        val view = findViewById<FragmentContainerView>(container.id)
        view?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun show(
        activity: Activity,
        vararg containers: UiContainer,
    ) {
        containers.forEach { activity.setContainerVisible(it, true) }
    }

    fun hide(activity: Activity, vararg containers: UiContainer) {
        containers.forEach { activity.setContainerVisible(it, false) }
    }
}

