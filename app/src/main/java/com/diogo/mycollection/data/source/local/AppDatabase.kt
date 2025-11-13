package com.diogo.mycollection.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.diogo.mycollection.data.source.local.dao.CollectionItemDao
import com.diogo.mycollection.data.source.local.dao.UserDao
import com.diogo.mycollection.data.source.local.entity.CollectionItemEntity
import com.diogo.mycollection.data.source.local.entity.UserEntity

@Database(
    entities = [CollectionItemEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun collectionItemDao(): CollectionItemDao
    abstract fun userDao(): UserDao
}