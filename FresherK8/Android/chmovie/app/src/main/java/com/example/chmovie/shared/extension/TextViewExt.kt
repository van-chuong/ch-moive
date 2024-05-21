package com.example.chmovie.shared.extension

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.core.content.ContextCompat

fun TextView.setStartDrawableTint(colorResId: Int) {
    val drawable: Drawable? = compoundDrawablesRelative[0]

    if (drawable != null && colorResId != 0) {
        val color = ContextCompat.getColor(context, colorResId)

        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)
    }
}