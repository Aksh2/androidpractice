package com.learning.moviesbrowser.di.module

import android.content.Context
import com.learning.moviesbrowser.data.local.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = MovieDatabase.getInstance(context)

    @Provides
    fun provideMovieDao(database: MovieDatabase) = database.getMovieDao()
}