package com.learning.moviesbrowser.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import com.learning.moviesbrowser.R
import com.learning.moviesbrowser.model.entities.Movie
import com.learning.moviesbrowser.model.entities.MovieSearch
import com.learning.moviesbrowser.utils.State
import com.learning.moviesbrowser.utils.hide
import com.learning.moviesbrowser.utils.loadUrl
import com.learning.moviesbrowser.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_movie_detail.*

@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {
    private val mainViewModel : MainViewModel by viewModels()

    companion object{
        const val MOVIE_ID ="movieId"
        const val IS_SEARCH = "isSearch"
        const val MOVIE_IMAGE_URL_BASE = "https://image.tmdb.org/t/p/w500/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val movieId = intent.extras?.getString(MOVIE_ID)
            ?: throw IllegalArgumentException("movieId must be non-null")

        val isSearch = intent.extras?.getBoolean(IS_SEARCH) ?: false

        fetchDetails(movieId, isSearch)
        initializeButtons(movieId)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun fetchDetails(movieId: String, isSearch: Boolean){
        if(isSearch){
            favouriteBtn.hide()
            reviewTil.hide()
            submitBtn.hide()
            mainViewModel.fetchSearchMovieDetailById(movieId).observe(this){
                it?.let {
                    initializeViews(title = it.title, posterPath = it.posterPath!!,
                        description = it.description,releaseDate = it.releaseDate!!,
                        genres = it.genres)
                }

            }
        }else{
            mainViewModel.fetchMovieDetailsById(movieId).observe(this) {

                initializeViews(title = it.title, posterPath = it.posterPath!!,
                    description = it.description,releaseDate = it.releaseDate!!,
                    genres = it.genres)


                if(it.isFavourite){
                    favouriteBtn.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_red_dark),android.graphics.PorterDuff.Mode.SRC_IN)
                }

                val reviewText= if(it.review?.isEmpty()!!){
                    "No reviews"
                }else{
                    hideReviewTools()
                    it.review
                }

                reviewTv.text = "Reviews: ${reviewText}"

            }
        }

    }

    fun initializeViews(title: String, posterPath: String,
                        releaseDate: String, description: String, genres: List<String>){
        moviePosterIv.loadUrl(MOVIE_IMAGE_URL_BASE+posterPath)
        titleTv.text = getString(R.string.title_placeholder_text,title)
        releaseDateTv.text = getString(R.string.releasedate_placeholder_text,releaseDate)
        descriptionTv.text = getString(R.string.description_placeholder_text,description)
        if(genres.isNotEmpty()){
            mainViewModel.fetchGenreNames(genres).observe(this) {
                when(it){
                    is State.Success ->{
                        genreTv.text = getString(R.string.genre_placeholder_text,it.data.map { it.name })
                    }
                }

            }
        }

    }



    private fun initializeButtons(movieId: String){
        favouriteBtn.setOnClickListener {
            mainViewModel.markFavourite(movieId)
            favouriteBtn.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_red_dark),android.graphics.PorterDuff.Mode.SRC_IN)
        }
        submitBtn.setOnClickListener {
            hideReviewTools()
            mainViewModel.addMovieReview(movieId, reviewEt.text.toString())
        }
    }

    fun hideReviewTools(){
        reviewTil.hide()
        submitBtn.hide()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{ this.finish()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }
}