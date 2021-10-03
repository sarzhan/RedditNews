package com.ysar.redditnews.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.ysar.redditnews.R
import com.ysar.redditnews.data.model.RedditNewsItem
import com.ysar.redditnews.databinding.HolderFavoriteBinding
import com.ysar.redditnews.util.formatDate

class FavoriteNewsAdapter(private val listener: OnFavoriteFragmentInteractionListener) : ListAdapter<RedditNewsItem, FavoriteNewsAdapter.FavoriteNewsRXViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteNewsRXViewHolder {
        val holderFavoriteBinding = HolderFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteNewsRXViewHolder(holderFavoriteBinding)
    }

    override fun onBindViewHolder(holder: FavoriteNewsRXViewHolder, position: Int) {
        getItem(position)?.let { holder.onBind(it) }
    }

    inner class FavoriteNewsRXViewHolder(private val binding: HolderFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(redditNewsItem: RedditNewsItem) {

            binding.itemPostTitle.text = redditNewsItem.title
            binding.itemPostCommentsNumber.text = redditNewsItem.numOfComments.toString()
            binding.itemPostAuthor.text = redditNewsItem.author
            binding.itemPostDate.text = formatDate(redditNewsItem.created)

            if (redditNewsItem.thumbnail.startsWith("https://")) {
                binding.itemPostThumbnail.visibility = ImageView.VISIBLE
                binding.itemPostThumbnail.load(redditNewsItem.thumbnail) {
                    placeholder(R.color.whiteSmoke)
                }
            } else
                binding.itemPostThumbnail.visibility = ImageView.GONE

            if (listener.isFavorite(redditNewsItem)) binding.itemFavoriteImageView.setBackgroundResource(
                R.drawable.ic_star_full_vector)
            else binding.itemFavoriteImageView.setBackgroundResource(R.drawable.ic_star_gray_empty_vector)

            binding.itemFavoriteImageView.setOnClickListener {
                listener.showDeleteFavoriteDialog(redditNewsItem)
            }

            itemView.setOnClickListener {
                listener.gotoDetailPage(redditNewsItem.url)
            }
        }
    }
    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<RedditNewsItem>() {
            override fun areItemsTheSame(
                oldItem: RedditNewsItem,
                newItem: RedditNewsItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: RedditNewsItem,
                newItem: RedditNewsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


}

