package com.example.chmovie.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chmovie.data.repositories.AuthRepository
import com.example.chmovie.data.source.local.PrefManager
import com.example.chmovie.shared.base.BaseViewModel
import com.example.chmovie.shared.constant.Constant
import com.example.chmovie.shared.scheduler.DataResult

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val prefManager: PrefManager
) : BaseViewModel() {

    private val _loginResultLiveData = MutableLiveData<DataResult<String>>()
    val loginResultLiveData: LiveData<DataResult<String>> get() = _loginResultLiveData

    fun login(username: String, password: String) {
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
                _loginResultLiveData.value = DataResult.Success("successful")
                prefManager.save(Constant.USERNAME_KEY, username)
                prefManager.save(Constant.SESSION_KEY, session)
            },
            onError = { exception ->
                _loginResultLiveData.value = DataResult.Error(exception)
            }
        )
    }
}
