package com.example.chmovie.presentation.room.start_room

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.chmovie.R
import com.example.chmovie.data.models.RoomResponse
import com.example.chmovie.databinding.ActivityStartRoomBinding
import com.example.chmovie.presentation.room.start_room.adapter.MessageAdapter
import com.example.chmovie.presentation.room.start_room.controllers.CustomRoomPlayerUiController
import com.example.chmovie.shared.base.BaseActivity
import com.example.chmovie.shared.scheduler.DataResult
import com.example.chmovie.shared.widget.showFailedSnackbar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import org.koin.androidx.viewmodel.ext.android.viewModel

class StartRoomActivity : BaseActivity<ActivityStartRoomBinding, StartRoomViewModel>() {

    companion object {
        const val EXTRA_ROOM_RESPONSE = "room_response"

        fun newInstance(context: Context, roomResponse: RoomResponse) {
            val intent = Intent(context, StartRoomActivity::class.java).apply {
                putExtra(EXTRA_ROOM_RESPONSE, roomResponse)
            }
            context.startActivity(intent)
        }
    }

    override val viewModel: StartRoomViewModel by viewModel()

    private var messageAdapter: MessageAdapter = MessageAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleEvent(viewBinding.root)
    }

    override fun inflateViewBinding(inflater: LayoutInflater) = ActivityStartRoomBinding.inflate(inflater)

    override fun initView() {
        viewBinding.viewModel = viewModel
        viewBinding.lifecycleOwner = this@StartRoomActivity
        setUpYoutubePlayerView()
        bindView()
    }

    override fun initData() {
        viewModel.getRoom(intent)
    }

    private fun setUpYoutubePlayerView() {
        viewBinding.youtubePlayerView.enableAutomaticInitialization = false

        val view: View = viewBinding.youtubePlayerView.inflateCustomPlayerUi(R.layout.control_video_layout)
        val iFramePlayerOptions = IFramePlayerOptions.Builder().controls(0).fullscreen(1).build()

        val youtubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)

                val controller = CustomRoomPlayerUiController(
                    view,
                    viewBinding.youtubePlayerView,
                    viewBinding.sendMessageLayout,
                    youTubePlayer,
                    this@StartRoomActivity,
                    lifecycle,
                    viewModel.realTimeDbRepository,
                    viewModel.room.value,
                )

                youTubePlayer.addListener(controller)
                viewModel.room.value?.value?.let { youTubePlayer.loadOrCueVideo(lifecycle, it.videoKey, it.currentDuration) }
            }
        }

        viewBinding.youtubePlayerView.initialize(youtubePlayerListener, iFramePlayerOptions)
    }

    private fun bindView() {
        with(viewBinding) {
            rvMessage.adapter = messageAdapter
        }
    }

    override fun registerLiveData() = with(viewModel) {
        room.observe(this@StartRoomActivity) {
            viewBinding.txtRoomCode.text = getString(R.string.room_code, room.value?.key)

            viewModel.addMember(it.key)
            viewModel.getMembersCount(it.key)
            viewModel.loadMessage(it.key)
            viewModel.addMessageListener(it.key)
        }

        messages.observe(this@StartRoomActivity) {
            messageAdapter.submitList(it) {
                viewBinding.rvMessage.scrollToPosition(it.size - 1)
            }
        }

        addMessageResult.observe(this@StartRoomActivity) {
            when (it) {
                is DataResult.Success -> {
                    viewBinding.edtMessage.clearFocus()
                    viewBinding.edtMessage.text.clear()
                }

                is DataResult.Error -> {
                    viewBinding.root.showFailedSnackbar(it.exception.message.toString())
                }

                is DataResult.Loading -> {
                }
            }
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (CustomRoomPlayerUiController.isLandscapeMode) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                CustomRoomPlayerUiController.isLandscapeMode = !CustomRoomPlayerUiController.isLandscapeMode
                viewBinding.youtubePlayerView.wrapContent()
                viewBinding.sendMessageLayout.visibility = View.VISIBLE
            } else {
                finish()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleEvent(view: View) {

        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        view.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (viewBinding.edtMessage.isFocused) {
                    hiddenKeyBoard()
                }
            }
            true
        }

        viewBinding.btnSendMessage.setOnClickListener {
            hiddenKeyBoard()
            val message = viewBinding.edtMessage.text.toString().trim()

            if (message.isEmpty()) {
                viewBinding.root.showFailedSnackbar("The message is empty")
            } else {

                viewModel.room.value?.let { it1 -> viewModel.addMessage(it1.key, message) }
            }
        }

        viewBinding.rvMessage.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                if (e.action == MotionEvent.ACTION_DOWN) {
                    hiddenKeyBoard()
                }

                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

    private fun hiddenKeyBoard() {
        viewBinding.edtMessage.clearFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(viewBinding.edtMessage.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.room.value?.key?.let { viewModel.handleOutRoom(it) }
    }
}