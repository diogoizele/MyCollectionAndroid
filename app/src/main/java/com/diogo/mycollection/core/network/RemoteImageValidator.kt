package com.diogo.mycollection.core.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

object RemoteImageValidator {

    suspend fun isValidImageUrl(url: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val conn = URL(url).openConnection() as HttpURLConnection
                conn.requestMethod = "HEAD"
                conn.connectTimeout = 4000
                conn.readTimeout = 4000
                conn.connect()

                val type = conn.contentType ?: return@withContext false
                type.startsWith("image/")
            } catch (_: Exception) {
                false
            }
        }
    }
}
