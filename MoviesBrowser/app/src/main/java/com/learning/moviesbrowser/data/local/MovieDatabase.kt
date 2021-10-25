package com.learning.moviesbrowser.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.learning.moviesbrowser.data.local.converters.ListConverter
import com.learning.moviesbrowser.data.local.dao.MovieDao
import com.learning.moviesbrowser.model.entities.Genre
import com.learning.moviesbrowser.model.entities.Movie
import com.learning.moviesbrowser.model.entities.MovieSearch
import org.w3c.dom.Comment

@Database(
        entities = [Movie::class, MovieSearch::class, Genre::class],
        version = DatabaseMigrations.DB_VERSION,
        exportSchema = false

)
@TypeConverters(*[ ListConverter::class])
abstract class MovieDatabase: RoomDatabase() {
    abstract fun getMovieDao(): MovieDao

    companion object{
        const val DB_NAME = "movie_database"

        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase{
            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDatabase::class.java,
                        DB_NAME
                )
                        .addMigrations(*DatabaseMigrations.MIGRATIONS)
                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}