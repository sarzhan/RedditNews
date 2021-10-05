package com.ysar.redditnews.ui.news

import com.ysar.redditnews.data.model.RedditNewsItem


/**
 * To make an interaction between [NewsFragment] and [NewsViewModel]
 */
interface OnNewsFragmentInteractionListener {

    fun addOrRemoveFavorites(item: RedditNewsItem)

    fun isFavorite(item: RedditNewsItem): Boolean

    fun gotoDetailPage(url: String)

}
