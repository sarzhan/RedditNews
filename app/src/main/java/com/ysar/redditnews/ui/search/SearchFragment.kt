package com.ysar.redditnews.ui.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.contains
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.paging.filter
import androidx.paging.map
import androidx.recyclerview.widget.RecyclerView
import com.ysar.redditnews.R
import com.ysar.redditnews.data.model.RedditNewsItem
import com.ysar.redditnews.data.model.toEntity
import com.ysar.redditnews.databinding.FragmentSearchBinding
import com.ysar.redditnews.ui.main.MainActivity
import com.ysar.redditnews.ui.news.NewsViewModel
import com.ysar.redditnews.ui.news.OnNewsFragmentInteractionListener
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Provider


class SearchFragment : Fragment(), OnNewsFragmentInteractionListener {


    private var mQuery: String? = null
    private var mAdapter = SearchNewsRxAdapter(this)
    private var mMenuSearchItem: MenuItem? = null
    lateinit var binding: FragmentSearchBinding
    lateinit var viewModel: NewsViewModel
    private val mDisposable = CompositeDisposable()

    @Inject
    lateinit var viewModelProvider: Provider<NewsViewModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).mainComponent.inject(this)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = viewModelProvider.get() as T
        }).get(NewsViewModel::class.java)

    }

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        binding.searchRecyclerView.adapter = mAdapter

        mAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                binding.newsProgressBar.visibility = ProgressBar.VISIBLE
            } else {
                binding.newsProgressBar.visibility = ProgressBar.GONE

                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                error?.let {
                    Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_LONG).show()
                }
            }
            binding.searchEmptyContainer.visibility = if (mAdapter.itemCount == 0 ) View.VISIBLE else View.GONE
        }

        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search, menu)
        mMenuSearchItem = menu.findItem(R.id.search)
        val searchView = SearchView((context as MainActivity).supportActionBar?.themedContext)
        searchView.queryHint = getString(R.string.action_search)
        mMenuSearchItem?.actionView = searchView
        // These lines are deprecated in API 26 use instead
        mMenuSearchItem?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        searchView.isIconified = false


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mQuery = query
                mDisposable.add(viewModel.searchNews(query).subscribe {
                    mAdapter.submitData(lifecycle, it)
                })
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        searchView.setOnCloseListener {
            activity?.onBackPressed()
            false
        }
    }

    override fun addOrRemoveFavorites(item: RedditNewsItem) {
        viewModel.addOrRemoveAsFavorite(item)
        mAdapter.notifyDataSetChanged()
    }

    override fun isFavorite(item: RedditNewsItem): Boolean {
        return viewModel.isFavorite(item)
    }

    override fun gotoDetailPage(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.data = Uri.parse(url)
            startActivity(intent)
        } catch (e: Exception) {
            println("The current phone does not have a browser installed")
        }
    }

    override fun onDestroyView() {
        mDisposable.dispose()
        super.onDestroyView()
    }


}
