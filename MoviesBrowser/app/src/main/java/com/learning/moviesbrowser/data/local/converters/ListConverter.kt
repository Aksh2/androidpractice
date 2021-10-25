package com.learning.moviesbrowser.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListConverter {
    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>?): String {
        if (list.isNullOrEmpty()) {
            return Gson().toJson(emptyList<String>())
        } else {
            return Gson().toJson(list)
        }
    }
}