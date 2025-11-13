package com.diogo.mycollection.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.diogo.mycollection.data.model.CategoryType
import java.util.UUID

@Entity(tableName = "collection_items")
data class CollectionItemEntity (
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val author: String,
    val description: String?,
    val imageUrl: String?,
    val type: CategoryType,
    val rating: Double
)