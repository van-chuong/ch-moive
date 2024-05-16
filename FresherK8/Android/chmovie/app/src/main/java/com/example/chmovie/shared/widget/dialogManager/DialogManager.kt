package com.example.chmovie.shared.widget.dialogManager

interface DialogManager {

    fun showLoading()

    fun hideLoading(delay: Long)

    fun onRelease()
}

fun DialogManager.hideLoadingWithDelay(delay: Long = 0) {
    hideLoading(delay)
}

