package com.ysar.redditnews.data.source.remote

import com.ysar.redditnews.data.model.RedditNewsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditApi {

    @GET("/top.json")
    fun getListNews(@Query("after") after: String?,
                    @Query("limit") limit: String): Single<RedditNewsResponse>

    @GET("/search.json")
    fun searchNews(@Query("q") query: String,
                   @Query("after") after: String?,
                   @Query("limit") limit: String): Single<RedditNewsResponse>


}