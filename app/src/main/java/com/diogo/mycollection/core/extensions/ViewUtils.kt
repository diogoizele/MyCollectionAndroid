package com.diogo.mycollection.core.extensions

import android.view.View
import android.view.Window
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

fun Window.applyKeyboardInsets(scroll: View) {
    WindowCompat.setDecorFitsSystemWindows(this, false)

    ViewCompat.setOnApplyWindowInsetsListener(scroll) { view, insets ->
        val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
        val nav = insets.getInsets(WindowInsetsCompat.Type.systemBars())

        view.updatePadding(bottom = maxOf(ime.bottom, nav.bottom))
        insets
    }
}
