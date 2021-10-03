package com.ysar.redditnews.di.component


import com.ysar.redditnews.di.ActivityScope
import com.ysar.redditnews.ui.favorite.FavoriteFragment
import com.ysar.redditnews.ui.main.MainActivity
import com.ysar.redditnews.ui.news.NewsFragment
import com.ysar.redditnews.ui.search.SearchFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(mainActivity: MainActivity)
    fun inject(newsFragment: NewsFragment)
    fun inject(favoriteFragment: FavoriteFragment)
    fun inject(searchFragment: SearchFragment)
}