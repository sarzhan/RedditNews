package com.ysar.redditnews.di.module

import android.app.Application
import androidx.room.Room
import com.ysar.redditnews.data.repository.GetNewsRxPagingSource
import com.ysar.redditnews.data.source.dao.AppDatabase
import com.ysar.redditnews.data.source.remote.RedditApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {

    @Provides
    @Singleton
    @JvmStatic
    fun provideAppDatabase(application: Application): AppDatabase =
            Room.databaseBuilder(application, AppDatabase::class.java, AppDatabase.DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()//allows database to be cleared after upgrading version
                    .build()

}