package com.demo.imagegif.shared.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.chmovie.R

fun ImageView.loadImageUrl(url: String) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.image_placeholder)
        .into(this)
}
