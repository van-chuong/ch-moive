package com.example.chmovie.data.models

import android.os.Parcelable
import com.example.chmovie.shared.utils.DateTimeUtil.getCurrentTime
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rating(
    @SerializedName("username")
    val username: String = "",
    @SerializedName("comment")
    val comment: String = "",
    @SerializedName("rating")
    val rating: Double = 0.0,
    @SerializedName("time")
    val time: String = getCurrentTime(),
) : Parcelable