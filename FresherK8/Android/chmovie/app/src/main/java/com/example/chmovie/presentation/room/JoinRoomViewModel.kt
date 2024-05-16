package com.example.chmovie.presentation.room

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chmovie.data.models.Room
import com.example.chmovie.data.models.RoomResponse
import com.example.chmovie.data.source.remote.firebase.FirebaseManager
import com.example.chmovie.data.source.remote.firebase.FirebaseManager.roomRef
import com.example.chmovie.shared.base.BaseViewModel
import com.example.chmovie.shared.constant.Constant.ROOM_REALTIME_DB
import com.example.chmovie.shared.extension.getObjectValue
import com.example.chmovie.shared.scheduler.DataResult
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.delay

class JoinRoomViewModel : BaseViewModel() {

    private val _roomCodeIsExist = MutableLiveData<DataResult<Boolean>>()
    val roomCodeIsExist: LiveData<DataResult<Boolean>> = _roomCodeIsExist

    private val _room = MutableLiveData<RoomResponse>()
    val room: LiveData<RoomResponse> = _room

    fun checkRoomCodeExist(roomCode: String) {
        _roomCodeIsExist.value = DataResult.Loading

        roomRef.child(roomCode).get().addOnSuccessListener {
            if (it.exists()) {
                _room.value = RoomResponse(it.key.toString(), it.getObjectValue<Room>()!!)
                _roomCodeIsExist.value = DataResult.Success(true)
            } else {
                _roomCodeIsExist.value = DataResult.Success(false)
            }

        }.addOnFailureListener {
            _roomCodeIsExist.value = DataResult.Error(it)
        }
    }
}