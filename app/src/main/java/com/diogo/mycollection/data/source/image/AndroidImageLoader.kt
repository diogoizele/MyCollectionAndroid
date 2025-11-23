package com.diogo.mycollection.data.source.image

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.diogo.mycollection.data.model.ImageSource
import java.io.File
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class AndroidImageLoader(
    private val context: Context
) : ImageLoader {

    override suspend fun loadAsBase64(source: ImageSource): String? = withContext(Dispatchers.IO) {
        when(source) {
            is ImageSource.Local -> {
                val path = source.path.takeIf { it.isNotBlank() } ?: return@withContext null
                val bytes = loadLocalBytes(path) ?: return@withContext null
                Base64.encodeToString(bytes, Base64.DEFAULT)
            }

            is ImageSource.Remote -> {
                val url = source.url.takeIf { it.isNotBlank() } ?: return@withContext null
                val bytes = downloadBytes(url) ?: return@withContext null
                Base64.encodeToString(bytes, Base64.DEFAULT)
            }

            ImageSource.None -> null
        }
    }

    private fun loadLocalBytes(path: String): ByteArray? {
        return try {
            val uri = path.toUri()
            if (uri.scheme != null) {
                context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            } else {
                File(path).inputStream().use { it.readBytes() }
            }
        } catch (t: Throwable) {
            null
        }
    }

    private fun downloadBytes(urlString: String): ByteArray? {
        var conn: HttpURLConnection? = null
        return try {
            val url = URL(urlString)
            conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 5_000
                readTimeout = 5_000
                doInput = true
                connect()
            }
            conn.inputStream.use { it.readBytes() }
        } catch (t: Throwable) {
            null
        } finally {
            conn?.disconnect()
        }
    }
}