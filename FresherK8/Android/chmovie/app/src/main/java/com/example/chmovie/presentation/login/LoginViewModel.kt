package com.example.chmovie.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.chmovie.data.repositories.AuthRepository
import com.example.chmovie.data.source.local.PrefManager
import com.example.chmovie.data.source.remote.firebase.FirebaseManager.devicesRef
import com.example.chmovie.shared.base.BaseViewModel
import com.example.chmovie.shared.constant.Constant
import com.example.chmovie.shared.scheduler.DataResult
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val prefManager: PrefManager
) : BaseViewModel() {

    private val _loginResultLiveData = MutableLiveData<DataResult<String>>()
    val loginResultLiveData: LiveData<DataResult<String>> get() = _loginResultLiveData

    init {
        isLoading.value = false
    }

    fun login(username: String, password: String) {
        _loginResultLiveData.value = DataResult.Loading

        launchTaskSync(
            onRequest = {
                when (val requestTokenResult = authRepository.getRequestToken()) {
                    is DataResult.Success -> {
                        val requestToken = requestTokenResult.data
                        val validatedTokenResult = authRepository.validateWithLogin(username, password, requestToken)
                        when (validatedTokenResult) {
                            is DataResult.Success -> {
                                val validatedToken = validatedTokenResult.data
                                val sessionResult = authRepository.createSession(validatedToken)
                                sessionResult
                            }

                            is DataResult.Error -> validatedTokenResult
                            is DataResult.Loading -> TODO()
                        }
                    }

                    is DataResult.Error -> requestTokenResult
                    DataResult.Loading -> TODO()
                }
            },
            onSuccess = { session ->
                prefManager.save(Constant.USERNAME_KEY, username)
                prefManager.save(Constant.SESSION_KEY, session)

                saveDeviceToken(username)
                _loginResultLiveData.value = DataResult.Success("successful")
            },
            onError = { exception ->
                _loginResultLiveData.value = DataResult.Error(exception)
            }
        )
    }

    private fun saveDeviceToken(username: String) {
        viewModelScope.launch {
            val token = FirebaseMessaging.getInstance().token.await()
            prefManager.save(Constant.DEVICE_TOKEN, token)
            devicesRef.child(username).child(token).setValue("")
        }
    }
}
