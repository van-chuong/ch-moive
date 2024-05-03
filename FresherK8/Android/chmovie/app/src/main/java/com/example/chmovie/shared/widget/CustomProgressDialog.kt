package com.example.chmovie.shared.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.example.chmovie.R

class CustomProgressDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.progress_dialog)

        // Ngăn người dùng tương tác với dialog
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }
}