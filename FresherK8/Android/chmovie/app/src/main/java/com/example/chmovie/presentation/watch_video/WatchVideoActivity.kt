package com.example.chmovie.presentation.watch_video

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.chmovie.R
import com.example.chmovie.databinding.ActivityWatchVideoBinding
import com.example.chmovie.presentation.watch_video.controllers.CustomPlayerUiController
import com.example.chmovie.presentation.watch_video.controllers.CustomPlayerUiController.Companion.isLandscapeMode
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo


class WatchVideoActivity : AppCompatActivity() {

    companion object {
        private const val ARGUMENT_WATCH_VIDEO = "ARGUMENT_WATCH_VIDEO"
        fun navigateToWatchVideo(activity: Activity, data: String) {
            val intent = Intent(activity, WatchVideoActivity::class.java)
            intent.putExtra(ARGUMENT_WATCH_VIDEO, data)
            activity.startActivity(intent)
        }
    }

    private val binding: ActivityWatchVideoBinding by lazy {
        ActivityWatchVideoBinding.inflate(layoutInflater)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isLandscapeMode) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                isLandscapeMode = !isLandscapeMode
            } else {
                finish()
            }
        }
    }

    private var videoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getData(savedInstanceState)
        setUpYoutubePlayerView()
        handleEvent()
    }

    private fun getData(savedInstanceState: Bundle?) {
        videoId = intent.extras?.getString(ARGUMENT_WATCH_VIDEO, "")
    }

    private fun setUpYoutubePlayerView() {
        binding.youtubePlayerView.enableAutomaticInitialization = false
        val view: View = binding.youtubePlayerView.inflateCustomPlayerUi(R.layout.control_video_layout)
        val youtubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                val controller = CustomPlayerUiController(view, youTubePlayer, this@WatchVideoActivity, lifecycle,)
                youTubePlayer.addListener(controller)
                videoId?.let { youTubePlayer.loadOrCueVideo(lifecycle, it, 0f) }
            }
        }
        val iFramePlayerOptions = IFramePlayerOptions.Builder().controls(0).fullscreen(1).build()
        binding.youtubePlayerView.initialize(youtubePlayerListener, iFramePlayerOptions)
    }

    private fun handleEvent() {
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }
}