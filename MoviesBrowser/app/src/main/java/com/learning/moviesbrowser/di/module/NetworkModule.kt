package com.learning.moviesbrowser.di.module

import android.app.Application
import com.learning.moviesbrowser.BuildConfig
import com.learning.moviesbrowser.network.MovieService
import com.learning.moviesbrowser.network.NoConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun provideRetrofitService(client: OkHttpClient): MovieService = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create()
        )
        .client(client)
        .build()
        .create(MovieService::class.java)

    @Provides
    fun provideOkHttpClient(
        noConnectionInterceptor: NoConnectionInterceptor
    ): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val builder = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(noConnectionInterceptor)
        return builder.build()
    }

    @Provides
    fun provideNoConnectionInterceptor(application: Application): NoConnectionInterceptor =
        NoConnectionInterceptor(application)



}