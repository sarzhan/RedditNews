package com.ysar.redditnews.data.repository


import com.ysar.redditnews.data.source.dao.AppDatabase
import com.ysar.redditnews.data.source.dao.NewsEntity
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * [DataBaseRepository] for retrieving repo data.
 */
class DataBaseRepository @Inject constructor(
    var appDatabase: AppDatabase,
) {

    fun getAllFavorites(): Flowable<List<NewsEntity>> =
        appDatabase.newsDao().loadAll()

    fun saveNews(redditNewsItemEntity: NewsEntity) =
        appDatabase.newsDao().insert(redditNewsItemEntity)

    fun deleteNews(redditNewsItemEntity: NewsEntity) =
        appDatabase.newsDao().delete(redditNewsItemEntity)

    fun getFavoriteByRecipeId(redditNewsItemEntity: NewsEntity): NewsEntity? {
        return appDatabase.newsDao().loadOneByNewsId(redditNewsItemEntity.id)
    }

}
