package com.example.chmovie.data.models


import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

data class Media(
    @SerializedName("media_id")
    val mediaId: Int = 0,
    @SerializedName("media_type")
    val mediaType: String = "movie",
    @SerializedName("watchlist")
    val watchlist: Boolean = true
)
fun Media.toMap(): Map<String, String> {
    val gson = Gson()
    val json = gson.toJson(this)
    val type = object : TypeToken<Map<String, String>>() {}.type
    return gson.fromJson(json, type)
}