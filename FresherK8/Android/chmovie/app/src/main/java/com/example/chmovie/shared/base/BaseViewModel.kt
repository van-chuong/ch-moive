package com.example.chmovie.shared.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chmovie.shared.scheduler.DataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()
    val isSuccess = MutableLiveData<Boolean>()
    val exception = MutableLiveData<Exception>()

    init {
        isLoading.postValue(true)
    }

    protected fun <T> launchTaskSync(
        onRequest: suspend CoroutineScope.() -> DataResult<T>,
        onSuccess: (T) -> Unit = {},
        onError: (Exception) -> Unit = {}
    ) = viewModelScope.launch {
        isLoading.postValue(true)
        when (val result = onRequest()) {
            is DataResult.Success -> {
                isLoading.postValue(false)
                isSuccess.postValue(true)
                onSuccess(result.data)
            }

            is DataResult.Error -> {
                isSuccess.postValue(false)
                onError(result.exception)
            }

            is DataResult.Loading -> {}
        }
        isLoading.postValue(false)
    }

    protected fun <T> viewModelScope(
        liveData: MutableLiveData<T>?,
        onSuccess: ((T) -> Unit)? = null,
        onError: ((Exception) -> Unit)? = null,
        onRequest: suspend CoroutineScope.() -> DataResult<T>
    ) {
        viewModelScope.launch {
            isLoading.postValue(true)
            when (val asynchronousTasks = onRequest(this)) {
                is DataResult.Success -> {
                    onSuccess?.invoke(asynchronousTasks.data) ?: kotlin.run {
                        liveData?.value = asynchronousTasks.data
                    }
                }

                is DataResult.Error -> {
                    onError?.invoke(asynchronousTasks.exception) ?: kotlin.run {
                        exception.value = asynchronousTasks.exception
                    }
                }

                is DataResult.Loading -> {}
            }
            isLoading.postValue(false)
        }
    }
}
