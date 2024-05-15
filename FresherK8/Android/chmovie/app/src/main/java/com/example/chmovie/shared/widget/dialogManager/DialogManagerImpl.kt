package com.example.chmovie.shared.widget.dialogManager

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class DialogManagerImpl(ctx: Context?) : DialogManager {

    private var context: WeakReference<Context?>? = null
    private var progressDialog: ProgressDialog? = null
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
}
