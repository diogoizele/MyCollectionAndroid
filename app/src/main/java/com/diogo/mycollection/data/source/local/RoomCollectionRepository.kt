package com.diogo.mycollection.data.source.local

import com.diogo.mycollection.data.model.CategoryType
import com.diogo.mycollection.data.model.CollectionItem
import com.diogo.mycollection.data.repository.CollectionRepository

import com.diogo.mycollection.data.source.local.mapper.toDomain
import com.diogo.mycollection.data.source.local.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomCollectionRepository(
    db: AppDatabase
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

    override fun observeCollectionItems(category: CategoryType?): Flow<List<CollectionItem>> {
        return dao.observeAll().map { entities -> entities.map { it.toDomain() } }
    }
}