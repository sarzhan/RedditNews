package com.ysar.redditnews.data.repository

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.ysar.redditnews.data.model.RedditNewsItem
import com.ysar.redditnews.data.model.RedditNewsResponse
import com.ysar.redditnews.data.model.transform
import com.ysar.redditnews.data.source.remote.RedditApi
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class GetSearchNewsRxPagingSource(
    private val redditApi: RedditApi,
    var query:String
) : RxPagingSource<String, RedditNewsItem>() {

    override fun loadSingle(params: LoadParams<String>): Single<LoadResult<String, RedditNewsItem>> {

        val refreshKey = params.key.toString()

        val responseLimit = params.loadSize.toString()

        return redditApi.searchNews(query,refreshKey,responseLimit)
            .subscribeOn(Schedulers.io())
            .map { toLoadResult(it) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(data: RedditNewsResponse): LoadResult<String, RedditNewsItem> {

        val redditNews = data.transform()

        return LoadResult.Page(
            data = redditNews.news,
            prevKey = null,
            nextKey = redditNews.after
        )
    }
    override fun getRefreshKey(state: PagingState<String, RedditNewsItem>): String? {
        TODO("Not yet implemented")
    }

    override val keyReuseSupported: Boolean = true
}