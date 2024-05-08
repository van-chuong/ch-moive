package com.example.chmovie.data.repositories

import com.example.chmovie.data.models.RequestToken
import com.example.chmovie.shared.scheduler.DataResult

interface AuthRepository {
    /**
     * Remote
     */
    suspend fun getRequestToken(): DataResult<RequestToken>
    suspend fun validateWithLogin(username: String, password: String, requestToken: RequestToken): DataResult<RequestToken>
    suspend fun createSession(requestToken: RequestToken): DataResult<String>
}