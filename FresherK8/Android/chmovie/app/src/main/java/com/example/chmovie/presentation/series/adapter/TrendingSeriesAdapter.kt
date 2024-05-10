package com.example.chmovie.presentation.series.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chmovie.R
import com.example.chmovie.data.models.Series
import com.example.chmovie.databinding.ItemTrendingNowBinding
import com.example.chmovie.shared.utils.CustomDiffCallBack

class TrendingSeriesAdapter(private var listener: ((Series) -> Unit)) : ListAdapter<Series, TrendingSeriesAdapter.TrendingViewHolder>(
    CustomDiffCallBack()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        val binding = DataBindingUtil.inflate<ItemTrendingNowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_trending_now,
            parent,
            false
        )
        return TrendingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class TrendingViewHolder(
        private val binding: ItemTrendingNowBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(series: Series) {
            binding.item = series
            binding.executePendingBindings()
            binding.root.setOnClickListener { listener.invoke(series) }
        }
    }

}
