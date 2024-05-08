package com.example.chmovie.data.source.remote

import com.example.chmovie.data.models.RequestToken
import com.example.chmovie.data.source.AuthDataSource
import com.example.chmovie.data.source.remote.api.MovieApiClient
import okhttp3.ResponseBody
import retrofit2.Response

class AuthRemoteImpl : AuthDataSource.Remote {

    override suspend fun getRequestToken(): Response<RequestToken> = MovieApiClient.instance.getRequestToken()


    override suspend fun validateWithLogin(username: String, password: String, requestToken: RequestToken): Response<RequestToken> =
        MovieApiClient.instance.validateWithLogin(
            mapOf(
                "username" to username,
                "password" to password,
                "request_token" to requestToken.requestToken
            )
        )

    override suspend fun createSession(requestToken: RequestToken): Response<ResponseBody> = MovieApiClient.instance.createSession(
        mapOf(
            "request_token" to requestToken.requestToken
        )
    )
}