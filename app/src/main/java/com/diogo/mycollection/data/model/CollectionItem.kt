package com.diogo.mycollection.data.model

import java.util.UUID

data class CollectionItem(
    val id: UUID,
    val title: String,
    val author: String?,
    val type: CategoryType,
    val rating: Float?,
    val image: String?,
    val description: String? = null,
)
