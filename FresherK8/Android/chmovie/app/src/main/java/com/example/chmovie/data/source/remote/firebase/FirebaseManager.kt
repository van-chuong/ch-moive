package com.example.chmovie.data.source.remote.firebase

import com.example.chmovie.shared.constant.Constant.CHAT_REALTIME_DB
import com.example.chmovie.shared.constant.Constant.DEVICE_REALTIME_DB
import com.example.chmovie.shared.constant.Constant.RECOMMEND_REALTIME_DB
import com.example.chmovie.shared.constant.Constant.ROOM_REALTIME_DB
import com.example.chmovie.shared.constant.Constant.USERS_REALTIME_DB
import com.google.firebase.Firebase
import com.google.firebase.database.database

object FirebaseManager {
    /*Realtime Database*/
    private val originalRef = Firebase.database.reference
    val roomRef = originalRef.child(ROOM_REALTIME_DB)
    val chatRef = originalRef.child(CHAT_REALTIME_DB)
    val usersRef = originalRef.child(USERS_REALTIME_DB)
    val recommendRef = originalRef.child(RECOMMEND_REALTIME_DB)
    val devicesRef = originalRef.child(DEVICE_REALTIME_DB)
}
