package com.ysar.redditnews.ui.news


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.ysar.redditnews.data.model.RedditNewsItem
import com.ysar.redditnews.data.model.toEntity
import com.ysar.redditnews.data.model.toModel
import com.ysar.redditnews.data.repository.GetNewsRxRepository
import com.ysar.redditnews.data.repository.DataBaseRepository
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


class NewsViewModel @Inject constructor(
    private val dataBaseRepository: DataBaseRepository,
    private val getNewsRxRepository: GetNewsRxRepository,
) : ViewModel() {

    val favorites = MutableLiveData<List<RedditNewsItem>>()
    val compositeDisposable = CompositeDisposable()
    val isLoading = MutableLiveData(false)

   @ExperimentalCoroutinesApi
    fun searchNews(query: String): Flowable<PagingData<RedditNewsItem>> {
        return getNewsRxRepository.searchNews(query)
    }

    @ExperimentalCoroutinesApi
    fun getNews(): Flowable<PagingData<RedditNewsItem>> {
        return getNewsRxRepository.getNews()
    }

    fun getAllFavorites(): LiveData<List<RedditNewsItem>> {
        setLoading(true)
        val disposable = dataBaseRepository.getAllFavorites()
            .map { it -> it.toList().map { it.toModel() } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::fetchFavorites, this::onError)

        compositeDisposable.add(disposable)

        return favorites
    }

    fun setLoading(isVisible: Boolean) {
        this.isLoading.value = isVisible
    }

    private fun onError(throwable: Throwable) {
        setLoading(false)
        Log.e(TAG, "onError: ", throwable)
    }

    private fun fetchFavorites(users: List<RedditNewsItem>) {
        setLoading(false)
        this.favorites.value = users
    }

    fun loadFavorite(item: RedditNewsItem): RedditNewsItem? {
        return dataBaseRepository.getFavoriteByRecipeId(item.toEntity())?.toModel()
    }

    fun isFavorite(item: RedditNewsItem): Boolean {
        return loadFavorite(item) != null
    }

    fun deleteFavoriteFromDB(item: RedditNewsItem) {
        dataBaseRepository.deleteNews(item.toEntity())
    }

    fun addOrRemoveAsFavorite(item: RedditNewsItem) {
        if (loadFavorite(item) == null) dataBaseRepository.saveNews(item.toEntity())
        else dataBaseRepository.deleteNews(item.toEntity())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    companion object {
        private val TAG = NewsViewModel::class.java.name
    }
}
