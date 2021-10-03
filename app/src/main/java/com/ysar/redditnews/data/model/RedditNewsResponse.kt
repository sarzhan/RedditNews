package com.ysar.redditnews.data.model


import com.squareup.moshi.Json

data class RedditNewsResponse(val data: RedditDataResponse)

data class RedditDataResponse(
    val children: List<RedditChildrenResponse>,
    val after: String?,
    val before: String?
)

data class RedditChildrenResponse(val data: RedditNewsDataResponse)

data class RedditNewsDataResponse(
    val id: String,
    val author: String,
    val title: String,
    @Json(name = "num_comments")val numOfComments: Int,
    val created: Long,
    val thumbnail: String,
    val url: String
)