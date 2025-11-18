package com.diogo.mycollection.core.extensions

import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.diogo.mycollection.core.components.LabeledEditText
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText

fun Fragment.clearFocusAndHideKeyboard() {
    view?.let { root ->
        fun clearFocusRecursively(view: View) {
            if (view is TextInputEditText || view is LabeledEditText || view is MaterialAutoCompleteTextView) view.clearFocus()
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) clearFocusRecursively(view.getChildAt(i))
            }
        }

        clearFocusRecursively(root)

        val imm = ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(root.windowToken, 0)
    }
}
