package com.example.chmovie.data.source

import com.example.chmovie.data.models.RequestToken
import okhttp3.ResponseBody
import retrofit2.Response

interface AuthDataSource {
    interface Local {
    }

    interface Remote {
        suspend fun getRequestToken(): Response<RequestToken>
        suspend fun validateWithLogin(username: String, password: String, requestToken: RequestToken): Response<RequestToken>
        suspend fun createSession(requestToken: RequestToken): Response<ResponseBody>
    }
}