package com.example.chmovie.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Parcelize
data class Message(
    val username: String = "",
    val message: String = "",
    val time: String = getCurrentTime(),
) : Parcelable {
    companion object {
        fun getCurrentTime(): String {
            return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        }
    }
}

@Parcelize
data class MessageResponse(
    val key: String = "",
    val value: Message = Message("", "", ""),
) : Parcelable

