package com.learning.moviesbrowser.model.entities

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.learning.moviesbrowser.model.entities.Movie.Companion.MOVIE_TABLE_NAME

@Entity(tableName = MOVIE_TABLE_NAME)
data class Movie (
        @PrimaryKey
        var id: String,
        @ColumnInfo(name = "poster_path")
        var posterPath: String?,
        @ColumnInfo(name = "release_date")
        var releaseDate: String?,
        @ColumnInfo(name = "title")
        var title: String,
        @ColumnInfo(name = "description")
        var description: String,
        @ColumnInfo(name = "is_favourite")
        var isFavourite: Boolean,
        @ColumnInfo(name = "review")
        var review: String = "",
        @ColumnInfo(name= "genres")
        var genres: List<String>



){
    companion object{
        const val MOVIE_TABLE_NAME = "movie"
    }
}


