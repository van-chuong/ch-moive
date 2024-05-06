package com.example.chmovie.shared.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.chmovie.data.models.MovieDetail

class MovieDiffCallBack : DiffUtil.ItemCallback<MovieDetail>() {
    override fun areItemsTheSame(oldItem: MovieDetail, newItem: MovieDetail): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieDetail, newItem: MovieDetail): Boolean {
        return oldItem == newItem
    }
}