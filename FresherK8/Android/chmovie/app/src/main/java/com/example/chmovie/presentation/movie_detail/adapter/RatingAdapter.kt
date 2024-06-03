package com.example.chmovie.presentation.movie_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chmovie.R
import com.example.chmovie.data.models.Rating
import com.example.chmovie.databinding.ItemRatingBinding
import com.example.chmovie.shared.utils.CustomDiffCallBack

class RatingAdapter : ListAdapter<Rating, RatingAdapter.RatingViewHolder>(CustomDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        val binding = DataBindingUtil.inflate<ItemRatingBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_rating,
            parent,
            false
        )
        return RatingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class RatingViewHolder(
        private val binding: ItemRatingBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(rating: Rating) {
            binding.rating = rating
            binding.executePendingBindings()
        }
    }
}