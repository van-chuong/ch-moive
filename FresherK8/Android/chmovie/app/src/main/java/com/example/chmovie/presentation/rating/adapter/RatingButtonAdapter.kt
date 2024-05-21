package com.example.chmovie.presentation.rating.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chmovie.R
import com.example.chmovie.databinding.ItemRatingButtonBinding
import com.example.chmovie.shared.extension.setStartDrawableTint
import com.example.chmovie.shared.utils.CustomDiffCallBack

class RatingButtonAdapter(private var listener: ((Int) -> Unit)) :
    ListAdapter<Int, RatingButtonAdapter.ButtonViewHolder>(CustomDiffCallBack()) {

    private var clickedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val binding = DataBindingUtil.inflate<ItemRatingButtonBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_rating_button,
            parent,
            false
        )
        return ButtonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        holder.bind(getItem(position), position == clickedPosition)
    }

    inner class ButtonViewHolder(
        private val binding: ItemRatingButtonBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(buttonValue: Int, isClicked: Boolean) {
            binding.buttonValue = buttonValue
            binding.executePendingBindings()

            if (isClicked) {
                binding.cardViewRating.setBackgroundResource(R.drawable.button_rating_active_bg)
                binding.txtRating.setStartDrawableTint(R.color.tint_primary)
                binding.txtRating.setTextColor(ContextCompat.getColor(binding.root.context, R.color.tint_primary))
            } else {
                binding.cardViewRating.setBackgroundResource(R.drawable.button_rating_bg)
                binding.txtRating.setStartDrawableTint(R.color.tint_secondary)
                binding.txtRating.setTextColor(ContextCompat.getColor(binding.root.context, R.color.tint_secondary))
            }

            binding.root.setOnClickListener {
                val previousPosition = clickedPosition
                clickedPosition = layoutPosition

                notifyItemChanged(previousPosition)
                notifyItemChanged(clickedPosition)

                listener.invoke(buttonValue)
            }
        }
    }
}