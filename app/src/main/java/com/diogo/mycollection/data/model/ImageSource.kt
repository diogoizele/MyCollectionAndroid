package com.diogo.mycollection.data.model

sealed class ImageSource {
    data class Remote(val url: String) : ImageSource()
    data class Local(val path: String) : ImageSource()
    object None : ImageSource()
}