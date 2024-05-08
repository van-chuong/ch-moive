package com.example.chmovie.shared.helper

object RuntimeFormatHelper {
    fun format(minutes: Int): String {
        require(minutes >= 0) { "Invalid duration" }

        val hours = minutes / 60
        val remainingMinutes = minutes % 60

        return buildString {
            if (hours > 0) append("$hours Hours ")
            if (remainingMinutes > 0) append("$remainingMinutes Minutes")
        }
    }
}