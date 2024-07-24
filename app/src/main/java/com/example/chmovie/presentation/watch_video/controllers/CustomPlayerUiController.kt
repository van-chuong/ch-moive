package com.example.chmovie.presentation.watch_video.controllers

import android.app.PictureInPictureParams
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.util.Rational
import android.view.Display
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import com.example.chmovie.R
import com.example.chmovie.presentation.watch_video.WatchVideoActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.utils.FadeViewHelper
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.views.YouTubePlayerSeekBar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.views.YouTubePlayerSeekBarListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker


@RequiresApi(Build.VERSION_CODES.O)
class CustomPlayerUiController(
    val view: View,
    val youtubePlayer: YouTubePlayer,
    private val activity: WatchVideoActivity,
    val lifecycle: Lifecycle,
) : AbstractYouTubePlayerListener() {

    private var playerTracker: YouTubePlayerTracker = YouTubePlayerTracker()
    private var fadeViewHelper:FadeViewHelper? = null
    companion object {
        var isLandscapeMode = false
    }

    init {
        youtubePlayer.addListener(playerTracker)
        setUpYoutubePlayer()
    }

    private fun setUpYoutubePlayer() {
        addFadeView()
        handleActionEvent()
        initPIP()
    }

    private fun addFadeView() {
        val layoutRoot: RelativeLayout = view.findViewById(R.id.control_video_layout_root)
        val layout: View = view.findViewById(R.id.control_video_layout)
        fadeViewHelper = FadeViewHelper(layout)
        fadeViewHelper!!.animationDuration = FadeViewHelper.DEFAULT_ANIMATION_DURATION
        fadeViewHelper!!.fadeOutDelay = FadeViewHelper.DEFAULT_FADE_OUT_DELAY
        youtubePlayer.addListener(fadeViewHelper!!)
        layoutRoot.setOnClickListener {
            fadeViewHelper!!.toggleVisibility()
        }
    }

    private fun removeFadeView() {
        fadeViewHelper?.let {
            it.toggleVisibility()
            youtubePlayer.removeListener(it)
        }
    }

    private fun handleActionEvent() {
        val playerSeekBar: YouTubePlayerSeekBar = view.findViewById(R.id.playerSeekBar)
        val btnPause: ImageButton = view.findViewById(R.id.btnPause)
        val btnToggleFullScreen: ImageButton = view.findViewById(R.id.btnToggleFullScreen)
        val btnFastForward: ImageButton = view.findViewById(R.id.btnFastForward)
        val btnRewind: ImageButton = view.findViewById(R.id.btnRewind)
        youtubePlayer.addListener(playerSeekBar)

        playerSeekBar.youtubePlayerSeekBarListener = object : YouTubePlayerSeekBarListener {
            override fun seekTo(time: Float) {
                youtubePlayer.seekTo(time)
            }
        }

        btnPause.setOnClickListener {
            if (playerTracker.state == PlayerConstants.PlayerState.PLAYING) {
                btnPause.setImageResource(R.drawable.baseline_play_arrow_24)
                youtubePlayer.pause()
            } else {
                btnPause.setImageResource(R.drawable.baseline_pause_24)
                youtubePlayer.play()
            }
        }

        btnToggleFullScreen.setOnClickListener {
            if (isLandscapeMode) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            } else {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
            isLandscapeMode = !isLandscapeMode
        }

        btnFastForward.setOnClickListener {
            youtubePlayer.seekTo(playerTracker.currentSecond + 10f)
        }

        btnRewind.setOnClickListener {
            youtubePlayer.seekTo(playerTracker.currentSecond - 10f)
        }
    }

    private fun initPIP() {
        val enterPipMode : ImageButton = view.findViewById(R.id.btnPip)
        enterPipMode.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val aspectRatio = Rational(16,9)
                val params: PictureInPictureParams = PictureInPictureParams.Builder()
                    .setAspectRatio(aspectRatio)
                    .build()
                removeFadeView()
                activity.enterPictureInPictureMode(params)
            }
        }
    }
}