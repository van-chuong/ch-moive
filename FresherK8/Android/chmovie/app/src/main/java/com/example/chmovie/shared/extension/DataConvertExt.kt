package com.example.chmovie.shared.extension

import com.google.firebase.database.DataSnapshot

inline fun <reified T> DataSnapshot.getObjectValue(): T? {
    return this.getValue(T::class.java)
}