package com.learning.moviesbrowser.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.observe
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.learning.moviesbrowser.R
import com.learning.moviesbrowser.adapter.MovieAdapter
import com.learning.moviesbrowser.model.MovieListItem
import com.learning.moviesbrowser.utils.State
import com.learning.moviesbrowser.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),MovieAdapter.OnItemClickListener {

    private val TAG = MainActivity::class.java.simpleName
    private val mainViewModel : MainViewModel by viewModels()
    val adapter:MovieAdapter by lazy {  MovieAdapter(this)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeMovieList()
        mainViewModel.fetchMovieList()
        moviesRv.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener {
            mainViewModel.fetchMovieList()
        }

    }

    private fun initializeMovieList(){
        mainViewModel.movieLiveData.observe(this) {
            when(it){
                is State.Loading ->{
                    Log.d(TAG, "Loading")
                    showLoading(true)
                }
                is State.Success ->{
                        Log.d(TAG, "Success")
                            adapter.submitList(it.data)
                        showLoading(false)
                }
                is State.Error ->{
                    Log.d(TAG, "Error ${it.message}")
                    showLoading(false)
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onItemClicked(movieListItem: MovieListItem) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.MOVIE_ID, movieListItem.id)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean){
        swipeRefreshLayout.isRefreshing = isLoading
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu,menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id.search)?.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false)
        }
        return true
    }
}