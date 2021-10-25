package com.learning.moviesbrowser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.learning.moviesbrowser.databinding.ItemMovieBinding
import com.learning.moviesbrowser.model.MovieListItem

class MovieAdapter(private val onItemClickListener: OnItemClickListener): PagedListAdapter<MovieListItem, MovieViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movieItem = getItem(position)
        print("id $movieItem.id favourite ${movieItem?.isFavourite}")
        holder.bind(movieItem!!, onItemClickListener)
    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieListItem>(){
            override fun areItemsTheSame(oldItem: MovieListItem, newItem: MovieListItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MovieListItem, newItem: MovieListItem): Boolean =
                oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(movieListItem: MovieListItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

}