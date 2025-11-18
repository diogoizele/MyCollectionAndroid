package com.diogo.mycollection.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.diogo.mycollection.data.model.CategoryType
import com.diogo.mycollection.data.model.ImageSourceType
import java.util.UUID

@Entity(tableName = "collection_items")
data class CollectionItemEntity (
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val author: String,
    val description: String?,
    @ColumnInfo(name = "image_type") val imageType: ImageSourceType,
    @ColumnInfo(name = "image_value") val imageValue: String?,
    val type: CategoryType,
    val rating: Float,
    @ColumnInfo(name = "created_at")  val createdAt: Long = System.currentTimeMillis()
)