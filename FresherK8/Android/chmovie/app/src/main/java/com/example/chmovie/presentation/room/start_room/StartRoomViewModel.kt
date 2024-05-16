package com.example.chmovie.presentation.room.start_room

import android.content.Intent
import android.os.Build
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chmovie.data.models.Message
import com.example.chmovie.data.models.RoomResponse
import com.example.chmovie.data.source.local.PrefManager
import com.example.chmovie.shared.base.BaseViewModel
import com.example.chmovie.shared.constant.Constant
import com.example.chmovie.shared.scheduler.DataResult
import com.example.chmovie.shared.widget.showAlertSnackbar
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class StartRoomViewModel(val realTimeDbRepository: DatabaseReference, private val prefManager: PrefManager) : BaseViewModel() {

    private val _room = MutableLiveData<RoomResponse>()
    val room: LiveData<RoomResponse> = _room

    private val roomRef = realTimeDbRepository.child(Constant.ROOM_REALTIME_DB)
    private val chatRef = realTimeDbRepository.child(Constant.CHAT_REALTIME_DB)
    private val usersRef = realTimeDbRepository.child(Constant.USERS_REALTIME_DB)

    private val _membersCount = MutableLiveData<Int>()
    val membersCount: LiveData<Int> = _membersCount

    private val _messages = MutableLiveData<MutableList<Message>>()
    val messages: LiveData<MutableList<Message>> = _messages

    private val username = prefManager.getString(Constant.USERNAME_KEY, "")

    private val _addMessageResult = MutableLiveData<DataResult<Boolean>>()
    val addMessageResult: LiveData<DataResult<Boolean>> = _addMessageResult

    fun getRoom(intent: Intent) {
        _room.value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(StartRoomActivity.EXTRA_ROOM_RESPONSE, RoomResponse::class.java)
        } else {
            intent.getParcelableExtra(StartRoomActivity.EXTRA_ROOM_RESPONSE)
        }
    }

    fun getMembersCount(roomCode: String) {
        usersRef.child(roomCode).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _membersCount.value = snapshot.childrenCount.toInt()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun addRoomUserListener(roomCode: String, view: View) {
        usersRef.child(roomCode).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                roomRef.child(roomCode).child("status").setValue("pause")

                if (snapshot.key.toString() != username) {
                    view.showAlertSnackbar("${snapshot.key} has joined the room")
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                usersRef.child(roomCode).get().addOnSuccessListener {
                    if (it.childrenCount.toInt() == 0) {
                        it.ref.removeValue()
                        roomRef.child(roomCode).removeValue()
                    }
                }

                if (snapshot.key.toString() != username) {
                    view.showAlertSnackbar("${snapshot.key} has left the room")
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun handleOutRoom(roomCode: String) {
        usersRef.child(roomCode).child(username.toString()).removeValue()
    }

    fun addMember(roomCode: String) {
        usersRef.child(roomCode).child(username.toString()).setValue("")
    }

    fun addMessage(roomCode: String, message: String) {
        _addMessageResult.value = DataResult.Loading
        val newMessageRef = chatRef.child(roomCode).push()

        newMessageRef.setValue(Message(username.toString(), message))
            .addOnSuccessListener {
                _addMessageResult.value = DataResult.Success(true)
            }.addOnFailureListener {
                _addMessageResult.value = DataResult.Error(it)
            }
    }

    fun loadMessage(roomCode: String) {
        chatRef.child(roomCode).get().addOnSuccessListener {
            if (it.exists()) {
                val value = it.children.mapNotNull { it.getValue(Message::class.java) }
                _messages.value = value.toMutableList()
            }
        }
    }

    fun addMessageListener(roomCode: String) {
        chatRef.child(roomCode).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(Message::class.java)?.let { message ->
                    val updatedMessages = _messages.value ?: mutableListOf()
                    updatedMessages.add(message)
                    _messages.value = updatedMessages
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                snapshot.getValue(Message::class.java)?.let { message ->
                    val updatedMessages = _messages.value ?: mutableListOf()
                    updatedMessages.remove(message)
                    _messages.value = updatedMessages
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}