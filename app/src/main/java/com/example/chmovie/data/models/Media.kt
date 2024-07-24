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
) {
    companion object {
        const val TV = "tv"
        const val MOVIE = "movie"

        fun <T> of(currentItem: T, watchlist: Boolean = true): Media {
            val mediaType = when (currentItem) {
                is MovieDetail -> MOVIE
                is Series -> TV
                else -> throw IllegalArgumentException("Unknown type")
            }
            return when (currentItem) {
                is MovieDetail -> Media(mediaId = currentItem.id, mediaType = mediaType, watchlist = watchlist)
                is Series -> Media(mediaId = currentItem.id, mediaType = mediaType, watchlist = watchlist)
                else -> throw IllegalArgumentException("Unknown type")
            }
        }
    }

}

fun Media.toMap(): Map<String, String> {
    val gson = Gson()
    val json = gson.toJson(this)
    val type = object : TypeToken<Map<String, String>>() {}.type
    return gson.fromJson(json, type)
}