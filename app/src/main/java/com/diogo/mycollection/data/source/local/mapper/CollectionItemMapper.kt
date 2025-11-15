package com.diogo.mycollection.data.source.local.mapper

import com.diogo.mycollection.data.model.CollectionItem
import com.diogo.mycollection.data.source.local.entity.CollectionItemEntity
import java.util.UUID

fun CollectionItemEntity.toDomain() = CollectionItem(
    id = UUID.fromString(id),
    title = title,
    author = author,
    type = type,
    rating = rating,
    imageUrl = imageUrl,
    description = description
)

fun CollectionItem.toEntity() = CollectionItemEntity(
    id = id.toString(),
    title = title,
    author = author,
    type = type,
    rating = rating,
    imageUrl = imageUrl,
    description = description
)
