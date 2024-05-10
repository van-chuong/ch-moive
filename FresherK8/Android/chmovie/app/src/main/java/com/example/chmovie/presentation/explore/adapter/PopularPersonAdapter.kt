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
    ListAdapter<Cast, PopularPersonAdapter.PersonViewHolder>(CustomDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val binding = DataBindingUtil.inflate<ItemPersonGridBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_person_grid,
            parent,
            false
        )
        return PersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class PersonViewHolder(
        private val binding: ItemPersonGridBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cast: Cast) {
            binding.cast = cast
            binding.executePendingBindings()
            binding.root.setOnClickListener { listener.invoke(cast) }
        }
    }
}