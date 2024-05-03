package com.example.chmovie.shared.helper


interface OnItemClickListener<T> {
    fun onItemViewClick(item: T, position: Int = 0)
}
