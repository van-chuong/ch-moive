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

class AirTodaySeriesAdapter(private var listener: ((Series) -> Unit)) : ListAdapter<Series, AirTodaySeriesAdapter.AirTodayViewHolder>(
    CustomDiffCallBack()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirTodayViewHolder {
        val binding = DataBindingUtil.inflate<ItemCommonHomeBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_common_home,
            parent,
            false
        )
        return AirTodayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AirTodayViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class AirTodayViewHolder(
        private val binding: ItemCommonHomeBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(series: Series) {
            binding.item = series
            binding.executePendingBindings()
            binding.root.setOnClickListener { listener.invoke(series) }
        }
    }
}