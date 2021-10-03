package com.ysar.redditnews.ui.favorite

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.ysar.redditnews.R
import com.ysar.redditnews.data.model.RedditNewsItem
import com.ysar.redditnews.databinding.FragmentFavoriteBinding
import com.ysar.redditnews.ui.main.MainActivity
import com.ysar.redditnews.ui.news.NewsViewModel
import javax.inject.Inject
import javax.inject.Provider

class FavoriteFragment : Fragment(), OnFavoriteFragmentInteractionListener {

    private lateinit var binding: FragmentFavoriteBinding
    private var mAdapter = FavoriteNewsAdapter(this)
    private lateinit var viewModel: NewsViewModel

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavoriteBinding.inflate(inflater)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.favoriteRecyclerView.adapter = mAdapter


        with(viewModel) {
            getAllFavorites().observe(viewLifecycleOwner, {
                mAdapter.submitList(it)
                binding.favoriteEmptyContainer.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            })

            isLoading.observe(viewLifecycleOwner, {
                binding.newsProgressBar.visibility = if (it == true) View.VISIBLE else View.GONE
            })
        }
        return binding.root
    }

    override fun showDeleteFavoriteDialog(redditNewsItem: RedditNewsItem) {
        if (context == null) return
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.delete))
                .setMessage(getString(R.string.are_you_sure_want_to_delete_this_item_from_favorite_list))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes)) { dialog, i ->
                    dialog.dismiss()//Cancel the dialog
                    viewModel.deleteFavoriteFromDB(redditNewsItem)
                    Snackbar.make(binding.root, getString(R.string.deleted_this_news_from_your_favorite_list),
                            Snackbar.LENGTH_LONG).setAction("Action", null).show()

                }
                .setNegativeButton(getString(R.string.no)) { dialog, i ->
                    dialog.dismiss()// User cancelled the dialog
                }
        builder.create()
        builder.show()
    }



    override fun isFavorite(redditNewsItem: RedditNewsItem): Boolean = viewModel.isFavorite(redditNewsItem)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorites, menu)
    }

}
