package com.diogo.mycollection.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.diogo.mycollection.data.model.CategoryType
import java.util.UUID

@Entity(tableName = "collection_items")
data class CollectionItemEntity (
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val type: CategoryType,
    val author: String?,
    val description: String?,
    val rating: Float?,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "created_at")  val createdAt: Long = System.currentTimeMillis()
)