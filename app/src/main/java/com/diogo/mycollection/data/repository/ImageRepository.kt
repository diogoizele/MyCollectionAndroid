package com.diogo.mycollection.data.repository

import android.graphics.Bitmap
import android.net.Uri

interface ImageRepository {

    fun save(bitmap: Bitmap): Uri
}