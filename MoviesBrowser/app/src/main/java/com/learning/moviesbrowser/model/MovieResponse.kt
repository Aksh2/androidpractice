package com.learning.moviesbrowser.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
        val page:Int,
        val results: List<Result>,
        @SerializedName("total_pages")
        val totalPages: Int
)

data class Result(
        val id: Long,
        @SerializedName("poster_path")
        val posterPath: String,
        @SerializedName("release_date")
        val releaseDate: String,
        val title: String,
        @SerializedName("overview")
        val description: String,
       @SerializedName("genre_ids")
        val genres: List<Int>

)
