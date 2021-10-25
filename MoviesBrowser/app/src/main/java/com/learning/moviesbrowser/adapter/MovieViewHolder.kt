package com.learning.moviesbrowser.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.learning.moviesbrowser.R
import com.learning.moviesbrowser.databinding.ItemMovieBinding
import com.learning.moviesbrowser.model.MovieListItem
import com.learning.moviesbrowser.utils.hide
import com.learning.moviesbrowser.utils.loadUrl
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieViewHolder(private val binding: ItemMovieBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(movieListItem: MovieListItem,onItemClickListener: MovieAdapter.OnItemClickListener? = null){
       val movieTitleTv =  binding.movieTitleTv
        movieTitleTv.text = movieListItem.title


        if(movieListItem.isFavourite == true){
            binding.favIconIv.visibility = View.VISIBLE
         }else{
            binding.favIconIv.hide()
        }

        var url: String = if(!movieListItem.posterPath.isNullOrEmpty()) "https://image.tmdb.org/t/p/w500/${movieListItem.posterPath!!}" else ""
        binding.moviePosterIv.loadUrl(url)
        onItemClickListener?.let { listener ->
            binding.root.setOnClickListener {
                listener.onItemClicked(movieListItem)
            }
        }
    }



}