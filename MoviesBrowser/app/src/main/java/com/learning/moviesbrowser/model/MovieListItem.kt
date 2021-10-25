package com.learning.moviesbrowser.model

import androidx.room.ColumnInfo
import androidx.room.Entity


data class MovieListItem(
        @ColumnInfo(name = "id") var id: String,
        @ColumnInfo(name = "poster_path")var posterPath: String?,
        @ColumnInfo(name = "title")var title: String,
        @ColumnInfo(name = "is_favourite") var isFavourite: Boolean? = false
){
    constructor() : this("","", "",false)
}