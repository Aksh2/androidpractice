package com.learning.moviesbrowser.view

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.learning.moviesbrowser.adapter.MovieAdapter
import com.learning.moviesbrowser.model.MovieListItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseActivity: AppCompatActivity(), MovieAdapter.OnItemClickListener  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeMovieList()

    }

    abstract fun initializeAdapter()
    abstract fun initializeMovieList()


    override fun onItemClicked(movieListItem: MovieListItem) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.MOVIE_ID, movieListItem.id)
        startActivity(intent)
    }
}