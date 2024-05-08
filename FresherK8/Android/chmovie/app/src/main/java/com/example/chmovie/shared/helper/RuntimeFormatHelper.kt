package com.example.chmovie.shared.helper

fun Int.formatRuntime(): String {
    require(this >= 0) { "Invalid duration" }

    val hours = this / 60
    val remainingMinutes = this % 60

    return buildString {
        if (hours > 0) append("$hours Hours ")
        if (remainingMinutes > 0) append("$remainingMinutes Minutes")
    }
}