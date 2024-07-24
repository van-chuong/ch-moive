package com.example.chmovie.data.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Room(
    @SerializedName("currentDuration")
    val currentDuration: Float = 0f,
    @SerializedName("members")
    val members: Int = 0,
    @SerializedName("owner")
    val owner: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("videoKey")
    val videoKey: String = "",
) : Parcelable

@Parcelize
data class RoomResponse(
    @SerializedName("key")
    val key: String = "",
    @SerializedName("value")
    val value: Room = Room(0f, 0, "", "", ""),
) : Parcelable
