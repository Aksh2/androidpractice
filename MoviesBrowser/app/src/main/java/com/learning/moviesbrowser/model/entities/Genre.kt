package com.learning.moviesbrowser.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Genre (
        @PrimaryKey
        var id: String,
        var name: String
){
    companion object{
        const val GENRE_TABLE_NAME = "genre"
    }
}