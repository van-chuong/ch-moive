package com.example.chmovie.data.models

import android.os.Parcelable
import com.example.chmovie.shared.utils.DateTimeUtil.getCurrentTime
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    val username: String = "",
    val message: String = "",
    val time: String = getCurrentTime(),
) : Parcelable

@Parcelize
data class MessageResponse(
    val key: String = "",
    val value: Message = Message("", "", ""),
) : Parcelable

