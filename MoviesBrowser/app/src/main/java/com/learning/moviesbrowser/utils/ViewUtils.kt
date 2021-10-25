package com.learning.moviesbrowser.utils

import android.opengl.Visibility
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.learning.moviesbrowser.R

fun ImageView.loadUrl(url: String) {
    Glide.with(this).load("" + url)
        .centerCrop()
        .fallback(R.mipmap.ic_launcher)
        .apply(RequestOptions().placeholder(R.mipmap.ic_launcher))
        .into(this)
}

fun View.hide(){
    visibility = View.GONE
}