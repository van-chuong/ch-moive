package com.example.chmovie.data.repositories

import android.util.Log
import com.example.chmovie.data.models.RequestToken
import com.example.chmovie.data.source.remote.MovieApiClient
import okhttp3.ResponseBody
import org.json.JSONObject

class AuthRepository {
    suspend fun getRequestToken(): RequestToken? {
        return try {
            Log.d("OKKKKKKK", "getRequestToken")
            MovieApiClient.instance.getRequestToken()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun validateWithLogin(
        username: String,
        password: String,
        requestToken: RequestToken
    ): RequestToken? {
        return try {
            Log.d("OKKKKKKK", "validateWithLogin")
            MovieApiClient.instance.validateWithLogin(
                mapOf(
                    "username" to username,
                    "password" to password,
                    "request_token" to requestToken.requestToken
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun createSession(requestToken: RequestToken): String? {
        return try {
            Log.d("OKKKKKKK", "createSession")
            JSONObject(MovieApiClient.instance.createSession(
                mapOf("request_token" to requestToken.requestToken)
            ).string()).getString("session_id")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}