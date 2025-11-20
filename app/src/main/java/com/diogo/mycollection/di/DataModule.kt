package com.diogo.mycollection.di

import android.content.Context
import androidx.room.Room
import com.diogo.mycollection.data.repository.AuthRepository
import com.diogo.mycollection.data.repository.CollectionRepository
import com.diogo.mycollection.data.repository.ImageRepository
import com.diogo.mycollection.data.source.local.AppDatabase
import com.diogo.mycollection.data.source.local.InMemoryAuthRepository
import com.diogo.mycollection.data.source.local.InMemoryCollectionRepository
import com.diogo.mycollection.data.source.local.InMemoryImageRepository
import com.diogo.mycollection.data.source.local.RoomAuthRepository
import com.diogo.mycollection.data.source.local.RoomCollectionRepository
import com.diogo.mycollection.data.source.local.dao.CollectionItemDao
import com.diogo.mycollection.data.source.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "my_collection_db").build()
    }

    @Provides
    @Singleton
    fun provideCollectionItemDao(db: AppDatabase): CollectionItemDao = db.collectionItemDao()

    @Provides
    @Singleton
    fun provideCollectionRepository(dao: CollectionItemDao): CollectionRepository =
        InMemoryCollectionRepository()

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()


    @Provides
    @Singleton
    fun provideAuthRepository(dao: UserDao): AuthRepository = InMemoryAuthRepository()

    @Provides
    @Singleton
    fun provideImageRepository(@ApplicationContext context: Context): ImageRepository = InMemoryImageRepository(context)
}