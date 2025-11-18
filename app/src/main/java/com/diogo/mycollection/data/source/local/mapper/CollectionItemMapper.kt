package com.diogo.mycollection.data.source.local.mapper

import com.diogo.mycollection.data.model.CollectionItem
import com.diogo.mycollection.data.model.ImageSource
import com.diogo.mycollection.data.model.ImageSourceType
import com.diogo.mycollection.data.source.local.entity.CollectionItemEntity
import java.util.UUID

fun CollectionItemEntity.toDomain(): CollectionItem {
    val source = when(imageType) {
        ImageSourceType.REMOTE -> ImageSource.Remote(imageValue!!)
        ImageSourceType.LOCAL -> ImageSource.Local(imageValue!!)
        else -> ImageSource.None
    }

    return CollectionItem(
        id = UUID.fromString(id),
        title = title,
        author = author,
        type = type,
        rating = rating,
        image = source,
        description = description
    )
}

fun CollectionItem.toEntity(): CollectionItemEntity {
    val (imgType, imgValue) = when(image) {
        is ImageSource.Remote -> ImageSourceType.REMOTE to image.url
        is ImageSource.Local -> ImageSourceType.LOCAL to image.path
        ImageSource.None -> ImageSourceType.NONE to null
    }

    return CollectionItemEntity(
        id = id.toString(),
        title = title,
        author = author,
        type = type,
        rating = rating,
        description = description,
        imageType = imgType,
        imageValue =  imgValue,
    )
}
