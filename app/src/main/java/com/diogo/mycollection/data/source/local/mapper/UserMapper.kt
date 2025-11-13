package com.diogo.mycollection.data.source.local.mapper

import com.diogo.mycollection.data.model.User
import com.diogo.mycollection.data.source.local.entity.UserEntity

fun UserEntity.toDomain() = User(
    email = email,
    name = name,
)

fun User.toEntity() = UserEntity(
    email = email,
    name = name
)