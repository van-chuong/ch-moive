package com.example.chmovie.shared.extension

import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.chmovie.R
import com.example.chmovie.shared.constant.Constant

fun ImageView.loadImageWithUrl(url: String) {
    Glide.with(this)
            .load(Constant.POSTER_BASE_URL + url)
            .placeholder(R.drawable.placeholder)
            .into(this)
}

