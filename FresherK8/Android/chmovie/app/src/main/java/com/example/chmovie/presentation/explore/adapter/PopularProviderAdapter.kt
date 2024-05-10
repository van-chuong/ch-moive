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
    ListAdapter<MovieProvider, PopularProviderAdapter.ProviderViewHolder>(CustomDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProviderViewHolder {
        val binding = DataBindingUtil.inflate<ItemProviderBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_provider,
            parent,
            false
        )
        return ProviderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProviderViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ProviderViewHolder(
        private val binding: ItemProviderBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(provider: MovieProvider) {
            binding.provider = provider
            binding.executePendingBindings()
            binding.root.setOnClickListener { listener.invoke(provider) }
        }
    }
}