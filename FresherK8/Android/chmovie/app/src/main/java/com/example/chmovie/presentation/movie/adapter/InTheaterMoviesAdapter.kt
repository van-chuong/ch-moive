package com.example.chmovie.presentation.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chmovie.R
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.databinding.ItemCommonHomeBinding
import com.example.chmovie.shared.utils.MovieDiffCallBack

class InTheaterMoviesAdapter(private var listener: ((MovieDetail) -> Unit)) : ListAdapter<MovieDetail, InTheaterMoviesAdapter.ItemViewHolder>(
    MovieDiffCallBack()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = DataBindingUtil.inflate<ItemCommonHomeBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_common_home,
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
        holder.itemView.setOnClickListener {
            listener.invoke(getItem(position))
        }
    }


    inner class ItemViewHolder(
        private val binding: ItemCommonHomeBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieDetail?) {
            binding.movieDetail = movie
            binding.executePendingBindings()
        }
    }
}