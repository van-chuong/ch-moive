package com.example.chmovie.shared.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.chmovie.shared.extension.loadImageWithUrl

object BindingUtils {
    @JvmStatic
    @BindingAdapter("app:imageUrl")
    fun setImageUrl(imageView: ImageView, url: String?) {
        url?.let {
            imageView.loadImageWithUrl(it)
        }
    }
}
