package com.learning.pagingapp.network

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.learning.pagingapp.data.News
import com.learning.pagingapp.data.State
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.disposables.ArrayCompositeDisposable

class NewsDataSource (
    private val networkService: NetworkService,
    private val compositeDisposable: CompositeDisposable
): PageKeyedDataSource<Int, News>(){

    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, News>
    ) {

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
        TODO("Not yet implemented")
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
        TODO("Not yet implemented")
    }
}