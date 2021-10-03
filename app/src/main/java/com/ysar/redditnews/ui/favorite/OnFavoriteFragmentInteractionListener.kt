package com.ysar.redditnews.ui.favorite

import com.ysar.redditnews.data.model.RedditNewsItem

interface OnFavoriteFragmentInteractionListener {

    fun showDeleteFavoriteDialog(redditNewsItem: RedditNewsItem)

    fun isFavorite(redditNewsItem: RedditNewsItem): Boolean

    fun gotoDetailPage(url: String)

}
