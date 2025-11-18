package com.diogo.mycollection.core.extensions

import android.content.Context
import com.diogo.mycollection.data.model.ImageSource
import java.io.File

fun ImageSource.toLoadable(context: Context): Any? = when (this) {
    is ImageSource.Remote -> url
    is ImageSource.Local -> File(path).takeIf { it.exists() } ?: path
    ImageSource.None -> null
}

fun ImageSource.asTextForPlaceholder(): String =
    when (this) {
        is ImageSource.Remote -> url
        is ImageSource.Local -> path
        ImageSource.None -> ""
    }