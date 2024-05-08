package com.example.chmovie.shared.utils

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.example.chmovie.data.models.Cast
import com.example.chmovie.data.models.MovieDetail

class CustomDiffCallBack<T : Any> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}
