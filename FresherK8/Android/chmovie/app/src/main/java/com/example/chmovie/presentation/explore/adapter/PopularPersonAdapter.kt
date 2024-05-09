package com.example.chmovie.presentation.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chmovie.R
import com.example.chmovie.data.models.Cast
import com.example.chmovie.databinding.ItemPersonGridBinding
import com.example.chmovie.shared.utils.CustomDiffCallBack

class PopularPersonAdapter(private var listener: ((Cast) -> Unit)) :
    ListAdapter<Cast, PopularPersonAdapter.ItemViewHolder>(CustomDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = DataBindingUtil.inflate<ItemPersonGridBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_person_grid,
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
        private val binding: ItemPersonGridBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cast: Cast) {
            binding.cast = cast
            binding.executePendingBindings()
        }
    }
}