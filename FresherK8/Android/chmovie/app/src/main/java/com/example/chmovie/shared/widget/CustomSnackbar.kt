package com.example.chmovie.shared.widget

import android.view.View
import androidx.core.content.ContextCompat
import com.example.chmovie.R
import com.google.android.material.snackbar.Snackbar

fun View.showSuccessSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(ContextCompat.getColor(context, R.color.green))
        .setActionTextColor(ContextCompat.getColor(context, R.color.white))
        .show()
}

fun View.showFailedSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(ContextCompat.getColor(context, R.color.red))
        .setActionTextColor(ContextCompat.getColor(context, R.color.white))
        .show()
}
