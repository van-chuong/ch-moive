package com.example.chmovie.shared.extension

import kotlin.random.Random

fun Random.next5DigitId(): Int {
    return Random.nextInt(10000, 99999)
}