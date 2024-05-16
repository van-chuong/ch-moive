package com.example.chmovie.presentation.movie_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chmovie.R
import com.example.chmovie.data.models.Cast
import com.example.chmovie.databinding.ItemPersonBinding
import com.example.chmovie.shared.utils.CustomDiffCallBack

class CastsAdapter(private var listener: ((Cast) -> Unit)) : ListAdapter<Cast, CastsAdapter.CastViewHolder>(CustomDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val binding = DataBindingUtil.inflate<ItemPersonBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_person,
            parent,
            false
        )
        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class CastViewHolder(
        private val binding: ItemPersonBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cast: Cast) {
            binding.cast = cast
            binding.executePendingBindings()
            binding.root.setOnClickListener { listener.invoke(cast) }
        }
    }
}