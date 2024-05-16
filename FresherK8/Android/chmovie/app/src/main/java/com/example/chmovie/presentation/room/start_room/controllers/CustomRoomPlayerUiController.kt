package com.example.chmovie.presentation.room.start_room.controllers

import android.content.pm.ActivityInfo
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import com.example.chmovie.R
import com.example.chmovie.data.models.RoomResponse
import com.example.chmovie.data.source.remote.firebase.FirebaseManager
import com.example.chmovie.presentation.room.start_room.StartRoomActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.utils.FadeViewHelper
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.views.YouTubePlayerSeekBar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.views.YouTubePlayerSeekBarListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class CustomRoomPlayerUiController(
    val view: View,
    private val youTubePlayerView: YouTubePlayerView,
    private val sendMessageLayout: ConstraintLayout,
    val youtubePlayer: YouTubePlayer,
    private val activity: StartRoomActivity,
    val lifecycle: Lifecycle,
    private val room: RoomResponse?,
) : AbstractYouTubePlayerListener() {

    private val roomRef = FirebaseManager.roomRef.child(room?.key.toString())
    private var playerTracker: YouTubePlayerTracker = YouTubePlayerTracker()

    private val btnPause: ImageButton = view.findViewById(R.id.btnPause)
    private val playerSeekBar: YouTubePlayerSeekBar = view.findViewById(R.id.playerSeekBar)
    private val btnToggleFullScreen: ImageButton = view.findViewById(R.id.btnToggleFullScreen)
    private val btnFastForward: ImageButton = view.findViewById(R.id.btnFastForward)
    private val btnRewind: ImageButton = view.findViewById(R.id.btnRewind)

    private val layoutRoot: RelativeLayout = view.findViewById(R.id.control_video_layout_root)
    val layout: View = view.findViewById(R.id.control_video_layout)
    private val fadeViewHelper = FadeViewHelper(layout)

    companion object {
        var isLandscapeMode = false
    }

    init {
        youtubePlayer.addListener(playerTracker)
        setUpYoutubePlayer()
    }

    private fun setUpYoutubePlayer() {
        initView()
        addFadeView()
        handleActionEvent()
    }

    private fun initView() {
        roomRef.child("status").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val status = snapshot.getValue(String::class.java)
                if (status == "play") {
                    btnPause.setImageResource(R.drawable.baseline_pause_24)
                    youtubePlayer.play()
                } else {
                    btnPause.setImageResource(R.drawable.baseline_play_arrow_24)
                    youtubePlayer.pause()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        roomRef.child("currentDuration").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentDuration = snapshot.getValue(Float::class.java)

                if (currentDuration != null) {
                    youtubePlayer.seekTo(currentDuration)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun addFadeView() {
        fadeViewHelper.animationDuration = FadeViewHelper.DEFAULT_ANIMATION_DURATION
        fadeViewHelper.fadeOutDelay = FadeViewHelper.DEFAULT_FADE_OUT_DELAY
        youtubePlayer.addListener(fadeViewHelper)

        layoutRoot.setOnClickListener {
            fadeViewHelper.toggleVisibility()
        }
    }

    private fun handleActionEvent() {
        youtubePlayer.addListener(playerSeekBar)

        playerSeekBar.youtubePlayerSeekBarListener = object : YouTubePlayerSeekBarListener {
            override fun seekTo(time: Float) {
                saveCurrentDuration(time)
                youtubePlayer.seekTo(time)
            }
        }

        btnPause.setOnClickListener {
            roomRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val currentDuration = snapshot.child("currentDuration").getValue(Float::class.java)
                    if (currentDuration != null) {
                        youtubePlayer.seekTo(currentDuration)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
            if (playerTracker.currentSecond != 0f) {
                saveCurrentDuration(playerTracker.currentSecond)
            }

            if (playerTracker.state == PlayerConstants.PlayerState.PLAYING) {
                roomRef.child("status").setValue("pause")
            } else {
                roomRef.child("status").setValue("play")
            }
        }

        btnToggleFullScreen.setOnClickListener {
            if (isLandscapeMode) {
                youTubePlayerView.wrapContent()
                sendMessageLayout.visibility = View.VISIBLE
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            } else {
                youTubePlayerView.matchParent()
                sendMessageLayout.visibility = View.GONE
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            isLandscapeMode = !isLandscapeMode
        }

        btnFastForward.setOnClickListener {
            saveCurrentDuration(playerTracker.currentSecond + 10)
            youtubePlayer.seekTo(playerTracker.currentSecond + 10f)

        }

        btnRewind.setOnClickListener {
            saveCurrentDuration(playerTracker.currentSecond - 10)
            youtubePlayer.seekTo(playerTracker.currentSecond - 10f)
        }
    }

    private fun saveCurrentDuration(time: Float) {
        roomRef.child("currentDuration").setValue(time)
    }

}