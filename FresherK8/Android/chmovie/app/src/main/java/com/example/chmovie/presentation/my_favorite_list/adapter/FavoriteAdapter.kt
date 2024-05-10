package com.example.chmovie.presentation.my_favorite_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chmovie.R
import com.example.chmovie.data.models.Favorite
import com.example.chmovie.databinding.ItemFavoriteBinding
import com.example.chmovie.shared.utils.CustomDiffCallBack

class FavoriteAdapter(private var listener: ((Favorite) -> Unit)) :
    ListAdapter<Favorite, FavoriteAdapter.FavoriteViewHolder>(CustomDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = DataBindingUtil.inflate<ItemFavoriteBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_favorite,
            parent,
            false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class FavoriteViewHolder(
        private val binding: ItemFavoriteBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: Favorite) {
            binding.item = favorite
            binding.executePendingBindings()
            binding.root.setOnClickListener { listener.invoke(favorite) }
        }
    }
}