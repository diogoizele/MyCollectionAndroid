package com.diogo.mycollection.data.source.local

import com.diogo.mycollection.data.model.CategoryType
import com.diogo.mycollection.data.model.CollectionItem
import com.diogo.mycollection.data.repository.CollectionRepository
import com.diogo.mycollection.data.source.local.entity.CollectionItemEntity
import com.diogo.mycollection.data.source.local.mapper.toDomain
import com.diogo.mycollection.data.source.local.mapper.toEntity

class RoomCollectionRepository(
    private val db: AppDatabase
) : CollectionRepository {

    private val dao = db.collectionItemDao()

    override suspend fun getCollectionItems(category: CategoryType?): List<CollectionItem> {
        val entities = if (category == null)
            dao.getAll()
        else
            dao.getByCategory(category)

        return entities.map {  it.toDomain() }
    }

    override suspend fun addCollectionItem(item: CollectionItem) {
        dao.insert(item.toEntity())
    }

    override suspend fun updateCollectionItem(item: CollectionItem) {
        dao.update(item.toEntity())
    }

    override suspend fun deleteCollectionItem(item: CollectionItem) {
        dao.delete(item.toEntity())
    }
}