package com.example.chmovie.shared.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.chmovie.R
import com.example.chmovie.shared.constant.Constant

fun ImageView.loadImageWithUrl(url: String) {
    Glide.with(this)
        .load(Constant.POSTER_BASE_URL + url)
        .placeholder(R.drawable.gif_place_holder)
        .error(R.drawable.error_place_holder)
        .into(this)
}

