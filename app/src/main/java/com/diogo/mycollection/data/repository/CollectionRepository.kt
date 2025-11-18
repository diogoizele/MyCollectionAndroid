package com.diogo.mycollection.data.repository

import com.diogo.mycollection.data.model.CategoryType
import com.diogo.mycollection.data.model.CollectionItem
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {

    suspend fun getAll(category: CategoryType?): List<CollectionItem>

    fun observeAll(category: CategoryType?): Flow<List<CollectionItem>>

    suspend fun save(item: CollectionItem)

    suspend fun update(item: CollectionItem)

    suspend fun delete(item: CollectionItem)

}