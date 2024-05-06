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
import com.example.chmovie.shared.helper.OnItemClickListener

class InTheaterMoviesAdapter :
    ListAdapter<MovieDetail, InTheaterMoviesAdapter.ItemViewHolder>(MovieDiffCallBack()) {

    private val movies = mutableListOf<MovieDetail>()
    private var itemClickListener: OnItemClickListener<MovieDetail>? = null
    fun updateData(newData: MutableList<MovieDetail>) {
        submitList(newData)
        movies.clear()
        movies.addAll(newData)
    }

    fun registerItemClickListener(onItemClickListener: OnItemClickListener<MovieDetail>) {
        itemClickListener = onItemClickListener
    }

    fun unRegisterItemClickListener() {
        itemClickListener = null
    }

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
        holder.bind(movies[position])
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemViewClick(movies[position])
        }
    }

    override fun getItemCount() = movies.size

    inner class ItemViewHolder(
        private val binding: ItemCommonHomeBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieDetail?) {
            binding.movieDetail = movie
            binding.executePendingBindings()
        }
    }

    class MovieDiffCallBack : DiffUtil.ItemCallback<MovieDetail>() {
        override fun areItemsTheSame(oldItem: MovieDetail, newItem: MovieDetail): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieDetail, newItem: MovieDetail): Boolean {
            return oldItem == newItem
        }
    }
}