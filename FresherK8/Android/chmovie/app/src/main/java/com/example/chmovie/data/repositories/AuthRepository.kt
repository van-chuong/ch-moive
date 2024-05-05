package com.example.chmovie.data.repositories

import android.util.Log
import com.example.chmovie.data.models.ErrorResponse
import com.example.chmovie.data.models.RequestToken
import com.example.chmovie.data.models.toErrorResponse
import com.example.chmovie.data.source.remote.MovieApiClient
import com.example.chmovie.shared.scheduler.DataResult
import org.json.JSONObject

class AuthRepository {
    suspend fun getRequestToken(): DataResult<RequestToken> {
        return try {
            val response = MovieApiClient.instance.getRequestToken()
            if (response.isSuccessful) {
                if (response.body() != null) {
                    DataResult.Success(response.body()!!)
                } else {
                    DataResult.Error(Exception("Response body is null"))
                }
            } else {
                val errorResponse: ErrorResponse? = response.errorBody()?.toErrorResponse()
                DataResult.Error(Exception(errorResponse?.message))
            }
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    suspend fun validateWithLogin(
        username: String,
        password: String,
        requestToken: RequestToken
    ): DataResult<RequestToken> {
        return try {
            val response = MovieApiClient.instance.validateWithLogin(
                mapOf(
                    "username" to username,
                    "password" to password,
                    "request_token" to requestToken.requestToken
                )
            )
            if (response.isSuccessful) {
                if (response.body() != null) {
                    DataResult.Success(response.body()!!)
                } else {
                    DataResult.Error(Exception("Response body is null"))
                }
            } else {
                val errorResponse: ErrorResponse? = response.errorBody()?.toErrorResponse()
                DataResult.Error(Exception(errorResponse?.message))
            }
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }


    suspend fun createSession(requestToken: RequestToken): DataResult<String> {
        return try {
            val response = MovieApiClient.instance.createSession(
                mapOf("request_token" to requestToken.requestToken)
            )
            if (response.isSuccessful) {
                if (response.body() != null) {
                    val sessionId = JSONObject(response.body()!!.string()).getString("session_id")
                    DataResult.Success(sessionId)
                } else {
                    DataResult.Error(Exception("Response body is null"))
                }
            } else {
                val errorResponse: ErrorResponse? = response.errorBody()?.toErrorResponse()
                DataResult.Error(Exception(errorResponse?.message))
            }
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }
}