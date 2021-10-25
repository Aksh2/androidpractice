package com.learning.moviesbrowser.network

import com.learning.moviesbrowser.model.GenreResponse
import com.learning.moviesbrowser.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    companion object{
        const val API_KEY = "848c4931eacbbc94672f81d6fecb9b75"
    }

    @GET("discover/movie")
    suspend fun getMoviesList(@Query("api_key") apiKey: String = API_KEY,
    @Query("language") language: String = "en-US",
    @Query("sort_by") sortBy: String = "popularity.desc",
    @Query("include_adult") includeAdult: Boolean = false,
    @Query("page") pageNumber: Long): Response<MovieResponse>

    @GET("genre/movie/list")
    suspend fun getGenreList(@Query("api_key") apiKey: String = API_KEY,
                             @Query("language") language: String = "en-US"
    ): Response<GenreResponse>

    @GET("search/movie")
    suspend fun getSearchMoviesList(@Query("api_key") apiKey: String = API_KEY,
                              @Query("language") language: String = "en-US",
                              @Query("query") query: String,
                              @Query("include_adult") includeAdult: Boolean = false,
                              @Query("page") pageNumber: Long): Response<MovieResponse>
}