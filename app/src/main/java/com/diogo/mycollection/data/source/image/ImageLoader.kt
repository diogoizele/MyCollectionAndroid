package com.diogo.mycollection.data.source.image

import com.diogo.mycollection.data.model.ImageSource

interface ImageLoader {
    suspend fun loadAsBase64(source: ImageSource): String?
}