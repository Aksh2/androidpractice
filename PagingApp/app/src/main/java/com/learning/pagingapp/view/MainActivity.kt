package com.learning.pagingapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.learning.pagingapp.R
import com.learning.pagingapp.data.State
import com.learning.pagingapp.view.adapter.NewsListAdapter
import com.learning.pagingapp.viewmodel.NewsListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: NewsListViewModel
    private lateinit var newsListAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this)
                .get(NewsListViewModel::class.java)
        initAdapter()
        initState()
    }

    private fun initAdapter(){
        newsListAdapter = NewsListAdapter { viewModel.retry() }
        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.adapter = newsListAdapter
        viewModel.newsList.observe(this, Observer {
            newsListAdapter.submitList(it)
        })
    }

    private fun initState(){
        txt_error.setOnClickListener{ viewModel.retry()}
        viewModel.getState().observe(this, Observer { state ->
            progress_bar.visibility = if(viewModel.listIsEmpty() && state == State.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if(viewModel.listIsEmpty() && state == State.ERROR) View.VISIBLE else View.GONE
            if(!viewModel.listIsEmpty()){
                newsListAdapter.setState(state ?: State.DONE)
            }
        })
    }
}