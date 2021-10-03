package com.ysar.redditnews.data.source.dao


import androidx.room.*
import com.ysar.redditnews.data.source.dao.NewsEntity.Companion.NEWS_ID
import com.ysar.redditnews.data.source.dao.NewsEntity.Companion.NEWS_TABLE
import io.reactivex.Flowable


@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(newsEntity: NewsEntity)

    @Query("SELECT * FROM $NEWS_TABLE")
    fun loadAll(): Flowable<List<NewsEntity>>

    @Delete
    fun delete(newsEntity: NewsEntity)

    @Query("SELECT * FROM  $NEWS_TABLE where $NEWS_ID = :newsId")
    fun loadOneByNewsId(newsId: String): NewsEntity?

    @Update
    fun update(newsEntity: NewsEntity)
}