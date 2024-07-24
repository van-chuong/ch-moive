package com.example.chmovie.shared.widget.dialogManager

import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class DialogManagerImpl(ctx: Context?) : DialogManager {

    private var context: WeakReference<Context?>? = null
    private var progressDialog: ProgressDialog? = null
    private var qrCodeDialog: QRCodeDialog? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    init {
        context = WeakReference(ctx).apply {
            progressDialog = ProgressDialog(this.get()!!)
        }
    }

    override fun showLoading() {
        progressDialog?.show()
    }

    override fun hideLoading(delay: Long) {
        coroutineScope.launch {
            if (delay > 0) delay(delay)
            progressDialog?.dismiss()
        }

    }

    override fun onRelease() {
        progressDialog = null
    }

    fun showImageDialog(bitmap: Bitmap) {
        val ctx = context?.get()
        if (ctx != null) {
            qrCodeDialog = QRCodeDialog(ctx, bitmap)
            qrCodeDialog?.show()
        }
    }

    fun hideImageDialog() {
        qrCodeDialog?.dismiss()
    }
}
