package com.diogo.mycollection.data.repository

import com.diogo.mycollection.data.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
}