package com.tpu.thetower.managers

import android.app.Activity
import android.view.View
import androidx.fragment.app.FragmentContainerView
import com.tpu.thetower.R

object UiVisibilityController {

    enum class UiContainer(val id: Int) {
        TITLE(R.id.fcv_title_screen),
        MENU(R.id.fcv_menu),
        SETTINGS(R.id.fcv_settings),
        HUD(R.id.fcv_hud),
        GO_BACK_ARROW(R.id.fcv_go_back_arrow),
        DIALOG(R.id.fcv_dialog),
        PERMISSION_DENIED(R.id.fcv_permission_denied),
        PERMISSION_REQUEST(R.id.fcv_permission_request)
    }

    private val containersWithoutHudAndBackArrow = listOf(
        UiContainer.TITLE,
        UiContainer.MENU,
        UiContainer.SETTINGS,
    )

    private fun Activity.setContainerVisible(container: UiContainer, visible: Boolean) {
        val view = findViewById<FragmentContainerView>(container.id)
        view?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun show(
        activity: Activity,
        vararg containers: UiContainer
    ) {
        containers.forEach { activity.setContainerVisible(it, true) }
        if (containers.any { it in containersWithoutHudAndBackArrow }) {
            activity.setContainerVisible(UiContainer.HUD, false)
            activity.setContainerVisible(UiContainer.GO_BACK_ARROW, false)
        }
    }

    fun hide(activity: Activity, vararg containers: UiContainer) {
        containers.forEach { activity.setContainerVisible(it, false) }
        if (containers.any { it in containersWithoutHudAndBackArrow }) {
            activity.setContainerVisible(UiContainer.HUD, true)
            activity.setContainerVisible(UiContainer.GO_BACK_ARROW, true)
        }
    }
}

