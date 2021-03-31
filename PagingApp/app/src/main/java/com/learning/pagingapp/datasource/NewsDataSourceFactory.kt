package com.learning.pagingapp.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.learning.pagingapp.data.News
import com.learning.pagingapp.network.NetworkService
import com.learning.pagingapp.network.NewsDataSource
import io.reactivex.disposables.CompositeDisposable

class NewsDataSourceFactory (
        private val compositeDisposable: CompositeDisposable,
        private val networkService: NetworkService)
    : DataSource.Factory<Int, News>(){

    public val newsDataSourceLiveData = MutableLiveData<NewsDataSource>()

    override fun create(): DataSource<Int, News> {
        val newsDataSource = NewsDataSource(networkService, compositeDisposable)
        newsDataSourceLiveData.postValue(newsDataSource)
        return newsDataSource
    }
}