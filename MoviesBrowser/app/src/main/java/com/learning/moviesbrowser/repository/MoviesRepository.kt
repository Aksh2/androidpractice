package com.learning.moviesbrowser.repository

import androidx.annotation.MainThread
import androidx.lifecycle.asFlow
import androidx.paging.*
import com.learning.moviesbrowser.data.local.MovieListBoundaryCallback
import com.learning.moviesbrowser.data.local.dao.MovieDao
import com.learning.moviesbrowser.model.GenreResponse
import com.learning.moviesbrowser.model.MovieListItem
import com.learning.moviesbrowser.model.MovieResponse
import com.learning.moviesbrowser.model.entities.Genre
import com.learning.moviesbrowser.model.entities.Movie
import com.learning.moviesbrowser.model.entities.MovieSearch
import com.learning.moviesbrowser.network.MovieService
import com.learning.moviesbrowser.utils.State
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
class MoviesRepository @Inject constructor(
        private val movieService: MovieService,
        private val movieDao: MovieDao
){
    private val livePagedListBuilder by lazy { LivePagedListBuilder( movieDao.fetchMoviesList(), Config(
            pageSize = 50,
            prefetchDistance = 150,
            enablePlaceholders = true
    )).setBoundaryCallback(MovieListBoundaryCallback(movieService, movieDao,2)).build() }

    private val searchLivePagedListBuilder by lazy { LivePagedListBuilder( movieDao.fetchSearchMoviesList(), Config(
        pageSize = 50,
        prefetchDistance = 150,
        enablePlaceholders = true
    )).setBoundaryCallback(MovieListBoundaryCallback(movieService, movieDao,2)).build() }

    fun getAllMoviesList(): Flow<State<PagedList<MovieListItem>>>{
        return object : NetworkRepository<PagedList<MovieListItem>,MovieResponse>(){
            override suspend fun saveRemoteData(response: MovieResponse) {
                val movies = arrayListOf<Movie>()
                response.results.forEach {
                    movies.add(Movie(id = it.id.toString(),posterPath = it.posterPath, releaseDate = it.releaseDate,
                    title = it.title, description = it.description, isFavourite = false, genres = it.genres.map { it.toString() }))
                }
                movieDao.insertMovie(movies)
            }

            override suspend fun fetchFromLocal(): Flow<PagedList<MovieListItem>> {
               return livePagedListBuilder.asFlow()
            }

            override suspend fun fetchFromRemote(): Response<MovieResponse> = movieService.getMoviesList(pageNumber = 1)
        }.asFlow().flowOn(Dispatchers.IO)
    }

    fun getSearchMoviesList(query: String): Flow<State<PagedList<MovieListItem>>>{
        return object : NetworkRepository<PagedList<MovieListItem>,MovieResponse>(){
            override suspend fun saveRemoteData(response: MovieResponse) {
                val movies = arrayListOf<MovieSearch>()
                response.results.forEach {
                    movies.add(MovieSearch(id = it.id.toString(),posterPath = it.posterPath, releaseDate = it.releaseDate,
                        title = it.title, description = it.description, isFavourite = false,genres = it.genres.map { it.toString() }))
                }
                movieDao.insertSearchMovie(movies)
            }

            override suspend fun fetchFromLocal(): Flow<PagedList<MovieListItem>> {
                return searchLivePagedListBuilder.asFlow()
            }

            override suspend fun fetchFromRemote(): Response<MovieResponse> = movieService.getSearchMoviesList(query=query,
                pageNumber = 1)
        }.asFlow().flowOn(Dispatchers.IO)
    }

    fun getGenreNames(genreIds: List<String>):Flow<State<List<Genre>>>{
        return object : NetworkRepository<List<Genre>, GenreResponse>(){
            override suspend fun saveRemoteData(response: GenreResponse) {
                val genreList= arrayListOf<Genre>()
                response.genres.forEach {
                    genreList.add(Genre(it.id.toString(),it.name))
                }
                movieDao.insertGenre(genreList)
            }

            override suspend fun fetchFromLocal(): Flow<List<Genre>> {
               return movieDao.fetchGenreNames(genreIds)
            }

            override suspend fun fetchFromRemote(): Response<GenreResponse> = movieService.getGenreList()
        }.asFlow().flowOn(Dispatchers.IO)
    }

    suspend fun markFavourite(id: String) = movieDao.markFavourite(id)

    suspend fun addReviewToMovie(movieId: String, movieReview: String) = movieDao.updateReview(movieId, movieReview)

    @MainThread
    fun getMovieDetailById(id:String): Flow<Movie> = movieDao.fetchMovieDetailById(id)

    @MainThread
    fun getSearchMovieDetailById(id:String): Flow<MovieSearch> = movieDao.fetchSearchMovieDetailById(id)

    suspend fun clearSearchMovies() = movieDao.clearSearchedMovies()





}