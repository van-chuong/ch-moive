package com.example.chmovie.presentation.series.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chmovie.R
import com.example.chmovie.data.models.Series
import com.example.chmovie.databinding.ItemCommonHomeBinding
import com.example.chmovie.shared.utils.CustomDiffCallBack

class OnTheAirSeriesAdapter(private var listener: ((Series) -> Unit)) : ListAdapter<Series, OnTheAirSeriesAdapter.OnTheAirViewHolder>(
    CustomDiffCallBack()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnTheAirViewHolder {
        val binding = DataBindingUtil.inflate<ItemCommonHomeBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_common_home,
            parent,
            false
        )
        return OnTheAirViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnTheAirViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class OnTheAirViewHolder(
        private val binding: ItemCommonHomeBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(series: Series) {
            binding.item = series
            binding.executePendingBindings()
            binding.root.setOnClickListener { listener.invoke(series) }
        }
    }
}