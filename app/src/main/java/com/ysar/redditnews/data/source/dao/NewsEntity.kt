package com.ysar.redditnews.data.source.dao


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ysar.redditnews.data.source.dao.NewsEntity.Companion.NEWS_TABLE


@Entity(tableName = NEWS_TABLE)
class NewsEntity(
    @PrimaryKey @ColumnInfo(name = NEWS_ID) val id: String,
    @ColumnInfo(name = NEWS_AUTHOR) val author: String,
    @ColumnInfo(name = NEWS_TITLE) val title: String,
    @ColumnInfo(name = NEWS_URL) val url: String,
    @ColumnInfo(name = NEWS_IS_FAVORITE) val isFavorite: Boolean,
    @ColumnInfo(name = NEWS_CREATED) val created: Long,
    @ColumnInfo(name = NEWS_IMAGE) val thumbnail: String,
    @ColumnInfo(name = NEWS_COMMENTS) val numOfComments: Int
) {

    companion object {
        // TABLE
        const val NEWS_TABLE = "news"

        // COLUMN
        const val NEWS_ID = "id"
        const val NEWS_AUTHOR = "author"
        const val NEWS_TITLE = "title"
        const val NEWS_URL = "url"
        const val NEWS_IS_FAVORITE = "is_favorite"
        const val NEWS_CREATED = "created"
        const val NEWS_IMAGE = "thumbnail"
        const val NEWS_COMMENTS = "numOfComments"
    }

}
