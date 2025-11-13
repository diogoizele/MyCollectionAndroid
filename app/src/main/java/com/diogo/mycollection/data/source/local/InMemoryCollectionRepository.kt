package com.diogo.mycollection.data.source.local

import com.diogo.mycollection.data.model.CategoryType
import com.diogo.mycollection.data.model.CollectionItem
import com.diogo.mycollection.data.repository.CollectionRepository

class InMemoryCollectionRepository : CollectionRepository {

    private val collectionItems = mutableListOf<CollectionItem>()

    override suspend fun getCollectionItems(category: CategoryType?): List<CollectionItem> {
        return if (category == null) {
            collectionItems
        } else {
            collectionItems.filter { it.type == category }
        }
    }

    override suspend fun addCollectionItem(item: CollectionItem) {
        collectionItems.add(item)
    }

    override suspend fun updateCollectionItem(item: CollectionItem) {
        val index = collectionItems.indexOfFirst { it.id == item.id }

        if (index != -1) {
            collectionItems[index] = item
        }
    }

    override suspend fun deleteCollectionItem(item: CollectionItem) {
        collectionItems.remove(item)
    }
}