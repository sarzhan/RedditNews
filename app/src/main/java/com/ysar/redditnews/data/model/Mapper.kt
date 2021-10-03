package com.ysar.redditnews.data.model

import android.os.Parcelable
import com.ysar.redditnews.data.source.dao.NewsEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RedditNews(
    val after: String,
    val before: String,
    val news: List<RedditNewsItem>) : Parcelable

@Parcelize
data class RedditNewsItem(
    val id: String,
    val author: String,
    val title: String,
    val url: String,
    val created: Long,
    val thumbnail: String,
    val numOfComments: Int
): Parcelable

fun RedditNewsItem.toEntity() =
        NewsEntity(
            id= id,
            author = author,
            title = title,
            url = url,
            false,
            created = created,
            thumbnail = thumbnail,
            numOfComments = numOfComments
        )

fun NewsEntity.toModel() =
        RedditNewsItem(
            id= id,
            author = author,
            title = title,
            url = url,
            created = created,
            thumbnail = thumbnail,
            numOfComments = numOfComments
        )

fun RedditNewsResponse.transform(): RedditNews {
    val dataResponse = this.data
    val news = dataResponse.children.map {
        val item = it.data
        RedditNewsItem(
            item.id, item.author, item.title, item.url,
            item.created, item.thumbnail, item.numOfComments
        )
    }
    return RedditNews(
        dataResponse.after.orEmpty(),
        dataResponse.before.orEmpty(),
        news)
}
