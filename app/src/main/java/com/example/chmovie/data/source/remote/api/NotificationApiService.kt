package com.example.chmovie.data.source.remote.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApiService {
    @POST("review-notification/{id}")
    suspend fun reviewNotification(
        @Path("id") id: String,
        @Query("username") username: String,
        @Query("type") type: String
    ): Response<ResponseBody>
}