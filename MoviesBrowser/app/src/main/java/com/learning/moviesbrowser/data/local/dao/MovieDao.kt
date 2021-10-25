package com.learning.moviesbrowser.data.local.dao

import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.learning.moviesbrowser.model.MovieListItem
import com.learning.moviesbrowser.model.entities.Genre
import com.learning.moviesbrowser.model.entities.Movie
import com.learning.moviesbrowser.model.entities.MovieSearch
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertMovie(movies: List<Movie>)

    @Query("SELECT id,is_favourite,poster_path,title FROM ${Movie.MOVIE_TABLE_NAME}")
    abstract fun fetchMoviesList(): DataSource.Factory<Int, MovieListItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertSearchMovie(searchMovies: List<MovieSearch>)

    @Query("SELECT id,poster_path,title FROM ${MovieSearch.MOVIE_SEARCH_TABLE_NAME}")
    abstract fun fetchSearchMoviesList(): DataSource.Factory<Int, MovieListItem>


    @Query("SELECT * FROM ${Movie.MOVIE_TABLE_NAME} WHERE id=:id")
    abstract fun fetchMovieDetailById(id: String): Flow<Movie>

    @Query("SELECT * FROM ${MovieSearch.MOVIE_SEARCH_TABLE_NAME} WHERE id=:id")
    abstract fun fetchSearchMovieDetailById(id: String): Flow<MovieSearch>

    @Query("DELETE  FROM ${MovieSearch.MOVIE_SEARCH_TABLE_NAME}")
    abstract suspend fun clearSearchedMovies()

    @Query("UPDATE ${Movie.MOVIE_TABLE_NAME} SET is_favourite=1 WHERE id=:id")
    abstract suspend fun markFavourite(id: String): Int

    @Query("UPDATE ${Movie.MOVIE_TABLE_NAME} SET review = :movieReview WHERE id=:id")
    abstract suspend fun updateReview(id: String, movieReview: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertGenre(genres: List<Genre>)

    @Query("Select * FROM ${Genre.GENRE_TABLE_NAME} WHERE id in (:ids)")
    abstract fun fetchGenreNames(ids: List<String>): Flow<List<Genre>>


}