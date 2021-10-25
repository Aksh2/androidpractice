package com.learning.moviesbrowser.data.local

import androidx.paging.PagedList
import androidx.room.PrimaryKey
import com.learning.moviesbrowser.data.local.dao.MovieDao
import com.learning.moviesbrowser.model.MovieListItem
import com.learning.moviesbrowser.model.MovieResponse
import com.learning.moviesbrowser.model.entities.Movie
import com.learning.moviesbrowser.network.MovieService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import kotlinx.coroutines.flow.flow as flow

class MovieListBoundaryCallback constructor(
        private val movieService: MovieService,
        private val movieDao: MovieDao,
        private val initialPage: Long
): PagedList.BoundaryCallback<MovieListItem>() {

    private var isRequestInProgress = false
    private var allPagesLoaded = false

    private var pageToRequest = initialPage

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        loadMoreData()
    }

    override fun onItemAtEndLoaded(itemAtEnd: MovieListItem) {
        super.onItemAtEndLoaded(itemAtEnd)
        loadMoreData()
    }

    override fun onItemAtFrontLoaded(itemAtFront: MovieListItem) {
        super.onItemAtFrontLoaded(itemAtFront)
    }

    fun loadMoreData(): Unit{
        if(isRequestInProgress) return
        if(allPagesLoaded) return

        val movieFlow = flow {
            val apiResponse = movieService.getMoviesList(pageNumber = pageToRequest)
            if(apiResponse.isSuccessful){
                val body = apiResponse.body()
                pageToRequest++
                body?.let {
                    emit(it)
                }

            }
        }.onStart { isRequestInProgress = true }.flowOn(Dispatchers.IO)

       GlobalScope.launch {
            val movieList = arrayListOf<Movie>()
            movieFlow.map { value: MovieResponse? ->  value?.results?.forEach {movie ->
                movieList.add(Movie(id = movie.id.toString(), posterPath = movie.posterPath, releaseDate = movie.releaseDate,
                title = movie.title, description = movie.description, genres = movie.genres.map { it.toString() }, isFavourite = false ))
            }
                movieDao.insertMovie(movieList)
                value
            }.collect {
                if(it?.results?.isEmpty()!! || it?.page == it.totalPages){
                    allPagesLoaded =true
                }
                isRequestInProgress=false
            }
        }
    }
}