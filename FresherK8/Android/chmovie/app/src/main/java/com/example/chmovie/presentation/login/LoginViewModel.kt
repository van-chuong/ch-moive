package com.example.chmovie.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chmovie.data.repositories.AuthRepository
import com.example.chmovie.data.source.local.PrefManager
import com.example.chmovie.shared.constant.Constant
import com.example.chmovie.shared.utils.ResponseResult
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val prefManager: PrefManager
) : ViewModel() {
    private val _loginResultLiveData = MutableLiveData<ResponseResult>()
    val loginResultLiveData: LiveData<ResponseResult>
        get() = _loginResultLiveData

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val requestToken = authRepository.getRequestToken()
                    ?: throw Exception("Failed to get request token")
                val validatedToken =
                    authRepository.validateWithLogin(username, password, requestToken)
                        ?: throw Exception("Invalid username and password")
                val session = authRepository.createSession(validatedToken)
                    ?: throw Exception("Failed to create session")
                prefManager.save(Constant.SESSION_KEY, session)
                _loginResultLiveData.value = ResponseResult.Success("Login successful")
            } catch (e: Exception) {
                _loginResultLiveData.value = ResponseResult.Error("Login failed: ${e.message}")
            }
        }
    }
}
