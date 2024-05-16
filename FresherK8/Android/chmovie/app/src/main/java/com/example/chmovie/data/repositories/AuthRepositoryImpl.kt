package com.example.chmovie.data.repositories

import com.example.chmovie.data.models.ErrorResponse
import com.example.chmovie.data.models.RequestToken
import com.example.chmovie.data.models.toErrorResponse
import com.example.chmovie.data.source.AuthDataSource
import com.example.chmovie.shared.scheduler.DataResult
import org.json.JSONObject

class AuthRepositoryImpl(private val authDataSource: AuthDataSource.Remote) : AuthRepository {
    override suspend fun getRequestToken(): DataResult<RequestToken> {
        return try {
            val response = authDataSource.getRequestToken()
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

    override suspend fun validateWithLogin(
        username: String,
        password: String,
        requestToken: RequestToken
    ): DataResult<RequestToken> {
        return try {
            val response = authDataSource.validateWithLogin(username, password, requestToken)
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


    override suspend fun createSession(requestToken: RequestToken): DataResult<String> {
        return try {
            val response = authDataSource.createSession(requestToken)
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