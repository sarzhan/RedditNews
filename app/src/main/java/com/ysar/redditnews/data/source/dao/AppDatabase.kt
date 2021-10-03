package com.ysar.redditnews.data.source.dao

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [(NewsEntity::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    companion object {

        val DB_NAME = "NewsDatabase.db"

    }

}
