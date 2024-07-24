package com.example.chmovie.shared.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.chmovie.shared.widget.dialogManager.DialogManagerImpl

abstract class BaseActivity<viewBinding : ViewBinding, viewModel : BaseViewModel> : AppCompatActivity() {

    protected abstract val viewModel: viewModel
    protected lateinit var viewBinding: viewBinding

    private val dialogManager by lazy {
        DialogManagerImpl(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = inflateViewBinding(layoutInflater)
        setContentView(viewBinding.root)
        initView()
        initData()
    }

    override fun onStart() {
        super.onStart()
        registerLiveData()
    }

    fun showLoading() {
        dialogManager.showLoading()
    }

    fun hideLoading() {
        dialogManager.hideLoading(0)
    }

    abstract fun inflateViewBinding(inflater: LayoutInflater): viewBinding
    abstract fun initView()
    abstract fun initData()

    open fun registerLiveData() {
        viewModel.run {
            isLoading.observe(this@BaseActivity) {
                if (it) showLoading() else hideLoading()
            }
        }
    }

}
