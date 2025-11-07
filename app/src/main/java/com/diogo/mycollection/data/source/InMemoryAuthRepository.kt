package com.diogo.mycollection.data.source

import com.diogo.mycollection.data.model.User
import com.diogo.mycollection.data.repository.AuthRepository
import kotlinx.coroutines.delay

class InMemoryAuthRepository : AuthRepository {

    private var loggedIn = false
    private var user: User? = null

    override suspend fun login(email: String, password: String): Result<User> {
        delay(1000)
        user = User(email = email)
        loggedIn = true
        return Result.success(user!!)
    }

    override suspend fun logout() {
        loggedIn = false
        user = null
    }

    override fun isLoggedIn(): Boolean = loggedIn
}