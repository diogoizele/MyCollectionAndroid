package com.diogo.mycollection.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.diogo.mycollection.data.model.CategoryType
import com.diogo.mycollection.data.source.local.entity.CollectionItemEntity

@Dao
interface CollectionItemDao {

    @Query("SELECT * FROM collection_items")
    suspend fun getAll(): List<CollectionItemEntity>

    @Query("SELECT * FROM collection_items WHERE type = :categoryType")
    suspend fun getByCategory(categoryType: CategoryType): List<CollectionItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CollectionItemEntity)

    @Update
    suspend fun update(item: CollectionItemEntity)

    @Delete
    suspend fun delete(item: CollectionItemEntity)
}