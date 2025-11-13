package com.diogo.mycollection.ui.common

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import com.diogo.mycollection.R

class ToolbarController(
    private val activity: AppCompatActivity,
    private val toolbar: Toolbar,
    private val navController: NavController
) {
    fun setup () {
        activity.setSupportActionBar(toolbar)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> {
                    update(
                        title = "My Collection",
                        menu = R.menu.home_menu,
                        back = false,
                        navigationIcon = R.drawable.ic_layer_group_solid_full
                    )
                    toolbar.touchables.forEach { it.isClickable = false } // Desabilita o efeito de ripple
                }
                else -> {
                    update(
                        title = destination.label?.toString(),
                        menu = null,
                        back = true
                    )
                }
            }
        }
    }

    private fun update(
        title: String?,
        menu: Int?,
        back: Boolean,
        navigationIcon: Int? = null
    ) {
        activity.supportActionBar?.apply {
            title?.let { this.title = it }
            setDisplayHomeAsUpEnabled(back)
        }

        toolbar.navigationIcon = when {
            back -> null
            navigationIcon != null -> AppCompatResources.getDrawable(activity, navigationIcon)
            else -> null
        }

        if (activity is ToolbarMenuHandler) {
            activity.currentMenuRes = menu
        }

        activity.invalidateOptionsMenu()
    }
}

interface ToolbarMenuHandler {
    var currentMenuRes: Int?
}