package com.example.chmovie.shared.helper

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun Int.formatRuntime(): String {
    require(this >= 0) { "Invalid duration" }

    val hours = this / 60
    val remainingMinutes = this % 60

    return buildString {
        if (hours > 0) append("$hours Hours ")
        if (remainingMinutes > 0) append("$remainingMinutes Minutes")
    }
}

fun Double.formatVoteAverage(): String {
    val symbols = DecimalFormatSymbols(Locale.US).apply {
        decimalSeparator = '.'
    }
    val df = DecimalFormat("#.#", symbols).apply {
        roundingMode = java.math.RoundingMode.HALF_UP
    }
    return df.format(this)
}