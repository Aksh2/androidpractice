package com.learning.moviesbrowser.view

import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.learning.moviesbrowser.R
import com.learning.moviesbrowser.adapter.MovieAdapter
import com.learning.moviesbrowser.model.MovieListItem
import com.learning.moviesbrowser.utils.State
import com.learning.moviesbrowser.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_search.*

@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), MovieAdapter.OnItemClickListener  {
    private val TAG = SearchActivity::class.java.simpleName
    private val mainViewModel : MainViewModel by viewModels()
    val adapter: MovieAdapter by lazy {  MovieAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        handleIntent(intent)
        initializeMovieList()

        initializeAdapter()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent!!)
    }

    private fun handleIntent(intent: Intent){
        if(Intent.ACTION_SEARCH == intent.action){
            val query = intent.getStringExtra(SearchManager.QUERY)
            Log.d(TAG, ">>>> Search Query: ${query}")
            mainViewModel.fetchSearchMoviesList(query = query)
        }
    }

    private fun initializeAdapter() {
        searchMoviesRv.adapter = adapter
    }

    private fun initializeMovieList() {
        mainViewModel.movieLiveData.observe(this) {
            when(it){
                is State.Loading ->{
                    Log.d(TAG, "Loading")
                }
                is State.Success ->{
                    Log.d(TAG, "Success")
                    adapter.submitList(it.data)
                }
                is State.Error ->{
                    Log.d(TAG, "Error ${it.message}")
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
             android.R.id.home ->{ this.finish()
             return true
             }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        mainViewModel.clearSearch()
    }


    override fun onItemClicked(movieListItem: MovieListItem) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.MOVIE_ID, movieListItem.id)
        intent.putExtra(MovieDetailActivity.IS_SEARCH, true)
        startActivity(intent)
        finish()
    }
}