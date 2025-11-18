package com.diogo.mycollection.data.source.local

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import com.diogo.mycollection.data.repository.ImageRepository
import java.io.File
import java.io.FileOutputStream

class InMemoryImageRepository(private val context: Context) : ImageRepository {

    override fun save(bitmap: Bitmap): Uri {
        val file = File(context.cacheDir, "captured_${System.currentTimeMillis()}.jpg")

        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out)
        }

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }
}