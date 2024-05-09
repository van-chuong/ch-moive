package com.example.chmovie.presentation.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chmovie.R
import com.example.chmovie.data.models.MovieProvider
import com.example.chmovie.databinding.ItemProviderBinding
import com.example.chmovie.shared.utils.CustomDiffCallBack

class PopularProviderAdapter(private var listener: ((MovieProvider) -> Unit)) :
    ListAdapter<MovieProvider, PopularProviderAdapter.ItemViewHolder>(CustomDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = DataBindingUtil.inflate<ItemProviderBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_provider,
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
        private val binding: ItemProviderBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(provider: MovieProvider) {
            binding.provider = provider
            binding.executePendingBindings()
        }
    }
}