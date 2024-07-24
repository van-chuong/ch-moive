package com.example.chmovie.data.models

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody

class ErrorResponse(
    @Expose
    @SerializedName("status_code")
    val status: Int = 0,
    @Expose
    @SerializedName("status_message")
    val message: String = ""
)

fun ResponseBody?.toErrorResponse(): ErrorResponse? {
    val gson: Gson = GsonBuilder().create()
    return this?.let {
        val json = it.string()
        gson.fromJson(json, ErrorResponse::class.java)
    }
}
