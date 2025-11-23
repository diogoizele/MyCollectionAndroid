package com.diogo.mycollection.data.source.local.mapper

import com.diogo.mycollection.data.model.CollectionItem
import com.diogo.mycollection.data.source.local.entity.CollectionItemEntity
import java.util.UUID

fun CollectionItemEntity.toDomain(): CollectionItem {
    return CollectionItem(
        id = UUID.fromString(id),
        title = title,
        author = author,
        type = type,
        rating = rating,
        image = imageUrl,
        description = description
    )
}

fun CollectionItem.toEntity(): CollectionItemEntity {
    return CollectionItemEntity(
        id = id.toString(),
        title = title,
        author = author,
        type = type,
        rating = rating,
        description = description,
        imageUrl = image
    )
}
