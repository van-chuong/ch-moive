package com.example.chmovie.presentation.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chmovie.R
import com.example.chmovie.data.models.MovieDetail
import com.example.chmovie.databinding.ItemCommonHomeBinding
import com.example.chmovie.shared.utils.CustomDiffCallBack

class InTheaterMoviesAdapter(private var listener: ((MovieDetail) -> Unit)) : ListAdapter<MovieDetail, InTheaterMoviesAdapter.InTheaterViewHolder>(
    CustomDiffCallBack()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InTheaterViewHolder {
        val binding = DataBindingUtil.inflate<ItemCommonHomeBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_common_home,
            parent,
            false
        )
        return InTheaterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InTheaterViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class InTheaterViewHolder(
        private val binding: ItemCommonHomeBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieDetail) {
            binding.item = movie
            binding.executePendingBindings()
            binding.root.setOnClickListener { listener.invoke(movie) }
        }
    }
}