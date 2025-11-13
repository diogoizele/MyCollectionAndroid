package com.diogo.mycollection.data.source.local

import com.diogo.mycollection.data.model.User
import com.diogo.mycollection.data.repository.AuthRepository
import com.diogo.mycollection.data.source.local.entity.UserEntity

class RoomAuthRepository(
    db: AppDatabase
) : AuthRepository {

    private val userDao = db.userDao()

    override suspend fun login(
        email: String,
        password: String
    ): Result<User> {
        val user = User(email = email, name = null)
        userDao.insert(UserEntity(email = user.email, name = user.name))
        return Result.success(user)
    }

    override suspend fun logout() {
        userDao.clear()
    }

    override suspend fun isLoggedIn(): Boolean {
        return !userDao.getLoggedUser()?.email.isNullOrBlank()
    }
}