package com.example.chmovie.shared.widget

import android.view.View
import androidx.core.content.ContextCompat
import com.example.chmovie.R
import com.google.android.material.snackbar.Snackbar

class CustomSnackbar {
    companion object {
        fun showSuccessMessage(view: View, message: String) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(ContextCompat.getColor(view.context, R.color.green))
                .setActionTextColor(ContextCompat.getColor(view.context, R.color.white))
                .show()
        }

        fun showFailedMessage(view: View, message: String) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(ContextCompat.getColor(view.context, R.color.red))
                .setActionTextColor(ContextCompat.getColor(view.context, R.color.white))
                .show()
        }
    }
}
