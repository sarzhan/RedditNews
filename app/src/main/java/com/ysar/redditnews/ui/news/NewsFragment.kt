package com.ysar.redditnews.ui.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.ysar.redditnews.R
import com.ysar.redditnews.data.model.RedditNewsItem
import com.ysar.redditnews.databinding.FragmentNewsBinding
import com.ysar.redditnews.ui.main.MainActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Provider

/**
 * Display a [News]. User can choose to view each news.
 */
class NewsFragment : Fragment(), OnNewsFragmentInteractionListener {


    private val mDisposable = CompositeDisposable()
    private lateinit var mAdapter: NewsRxAdapter
    private lateinit var binding: FragmentNewsBinding
    private lateinit var viewModel: NewsViewModel

    @Inject
    lateinit var viewModelProvider: Provider<NewsViewModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).mainComponent.inject(this)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                viewModelProvider.get() as T
        }).get(NewsViewModel::class.java)
    }

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNewsBinding.inflate(inflater)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mAdapter = NewsRxAdapter(this)
        binding.newsRecyclerView.adapter = mAdapter

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
        }
        mDisposable.add(viewModel.getNews().subscribe {
            mAdapter.submitData(lifecycle, it)
        })
        return binding.root

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
            println("The current device does not have a browser installed")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.news, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                gotoSearchPage()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun gotoSearchPage() {
        if (findNavController().currentDestination?.id == R.id.newsFragment) {
            findNavController().navigate(R.id.action_newsFragment_to_searchFragment)
        }
    }

    override fun onDestroyView() {
        mDisposable.dispose()
        super.onDestroyView()
    }

}
