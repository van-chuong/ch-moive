package com.example.chmovie.presentation.movie.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.chmovie.data.models.MovieDetail


class MoviesDiffUtil(private var oldItems: MutableList<MovieDetail>,
                     private var newsItems: MutableList<MovieDetail>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newsItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldItems[oldItemPosition].id == newsItems[newItemPosition].id
                    && oldItems[oldItemPosition].title == newsItems[newItemPosition].title

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldItems[oldItemPosition] == newsItems[newItemPosition]
}
