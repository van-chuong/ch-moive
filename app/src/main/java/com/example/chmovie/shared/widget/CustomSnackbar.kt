package com.example.chmovie.shared.widget

import android.view.View
import androidx.core.content.ContextCompat
import com.example.chmovie.R
import com.google.android.material.snackbar.Snackbar

fun View.showSuccessSnackbar(message: String) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    snackbar.view.background = ContextCompat.getDrawable(context, R.drawable.container_snackbar_success)
    snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.white))
    snackbar.show()
}

fun View.showFailedSnackbar(message: String) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    snackbar.view.background = ContextCompat.getDrawable(context, R.drawable.container_snackbar_failed)
    snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.white))
    snackbar.show()
}

fun View.showAlertSnackbar(message: String) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    snackbar.view.background = ContextCompat.getDrawable(context, R.drawable.container_snackbar_alert)
    snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.white))
    snackbar.show()
}
