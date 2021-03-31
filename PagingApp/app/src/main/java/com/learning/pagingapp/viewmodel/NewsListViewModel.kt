package com.learning.pagingapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.learning.pagingapp.data.News
import com.learning.pagingapp.data.State
import com.learning.pagingapp.datasource.NewsDataSourceFactory
import com.learning.pagingapp.network.NetworkService
import com.learning.pagingapp.network.NewsDataSource
import io.reactivex.disposables.CompositeDisposable


class NewsListViewModel: ViewModel(){

    private val networkService = NetworkService.getService()
    var newsList: LiveData<PagedList<News>>
    private val compositeDisposable = CompositeDisposable()
    private val pageSize = 5
    private val newsDataSourceFactory: NewsDataSourceFactory

    init {
        newsDataSourceFactory = NewsDataSourceFactory(compositeDisposable, networkService)
        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(false)
                .build()

        newsList = LivePagedListBuilder<Int,News>(newsDataSourceFactory, config).build()
    }

    fun getState(): LiveData<State> = Transformations.switchMap<NewsDataSource,State>(newsDataSourceFactory.newsDataSourceLiveData, NewsDataSource::state)

    fun retry(){
        newsDataSourceFactory.newsDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty():Boolean{
        return newsList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}