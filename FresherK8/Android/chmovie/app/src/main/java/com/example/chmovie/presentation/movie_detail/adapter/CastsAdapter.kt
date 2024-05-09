package com.example.chmovie.presentation.movie_detail.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chmovie.R
import com.example.chmovie.data.models.Cast
import com.example.chmovie.databinding.ItemPersonBinding
import com.example.chmovie.shared.utils.CustomDiffCallBack

class CastsAdapter(private var listener: ((Cast) -> Unit)) : ListAdapter<Cast, CastsAdapter.ItemViewHolder>(CustomDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = DataBindingUtil.inflate<ItemPersonBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_person,
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
        private val binding: ItemPersonBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cast: Cast) {
            binding.cast = cast
            binding.executePendingBindings()
        }
    }
}