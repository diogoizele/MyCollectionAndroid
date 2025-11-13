package com.diogo.mycollection.data.repository

import com.diogo.mycollection.data.model.CategoryType
import com.diogo.mycollection.data.model.CollectionItem

interface CollectionRepository {

    suspend fun getCollectionItems(category: CategoryType?): List<CollectionItem>

    suspend fun addCollectionItem(item: CollectionItem)

    suspend fun updateCollectionItem(item: CollectionItem)

    suspend fun deleteCollectionItem(item: CollectionItem)

}