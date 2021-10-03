package com.ysar.redditnews.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.ysar.redditnews.data.model.RedditNewsItem
import com.ysar.redditnews.data.source.remote.RedditApi
import io.reactivex.Flowable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class GetNewsRxRepository @Inject constructor(
    private val redditApi: RedditApi) {

    @ExperimentalCoroutinesApi
    fun getNews(): Flowable<PagingData<RedditNewsItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 20,
                initialLoadSize = 20),
            pagingSourceFactory = { GetNewsRxPagingSource(redditApi) }
        ).flowable
    }

    @ExperimentalCoroutinesApi
    fun searchNews(query:String): Flowable<PagingData<RedditNewsItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 20,
                initialLoadSize = 20),
            pagingSourceFactory = { GetSearchNewsRxPagingSource(redditApi,query) }
        ).flowable
    }

}