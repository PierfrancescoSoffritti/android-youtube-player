package com.pierfrancescosoffritti.androidyoutubeplayer.player

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.pierfrancescosoffritti.androidyoutubeplayer.R
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.player.utils.FullScreenHelper
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.PlayerUIController
import com.pierfrancescosoffritti.androidyoutubeplayer.utils.SixteenNineRatioFrameLayout


class ManagedYouTubePlayerView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
        SixteenNineRatioFrameLayout(context, attrs, defStyleAttr), LifecycleObserver {

    constructor(context: Context): this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet? = null): this(context, attrs, 0)

    private val youTubePlayerView: YouTubePlayerView = YouTubePlayerView(context)
    private val fullScreenHelper = FullScreenHelper(this)

    private var canPlay = false

    init {
        addView(youTubePlayerView, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.ManagedYouTubePlayerView, 0, 0)

        val autoPlay = typedArray.getBoolean(R.styleable.ManagedYouTubePlayerView_autoPlay, false)
        val handleNetworkEvents = typedArray.getBoolean(R.styleable.ManagedYouTubePlayerView_handleNetworkEvents, true)
        val videoId = typedArray.getString(R.styleable.ManagedYouTubePlayerView_videoId)

        val useWebUI = typedArray.getBoolean(R.styleable.ManagedYouTubePlayerView_useWebUI, false)
        val enableLiveVideoUI = typedArray.getBoolean(R.styleable.ManagedYouTubePlayerView_enableLiveVideoUI, false)
        val showYouTubeButton = typedArray.getBoolean(R.styleable.ManagedYouTubePlayerView_showYouTubeButton, true)
        val showFullScreenButton = typedArray.getBoolean(R.styleable.ManagedYouTubePlayerView_showFullScreenButton, true)
        val showVideoCurrentTime = typedArray.getBoolean(R.styleable.ManagedYouTubePlayerView_showVideoCurrentTime, true)
        val showVideoDuration = typedArray.getBoolean(R.styleable.ManagedYouTubePlayerView_showVideoDuration, true)
        val showSeekBar = typedArray.getBoolean(R.styleable.ManagedYouTubePlayerView_showSeekBar, true)

        typedArray.recycle()

        val youTubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                videoId?.let {
                    if(autoPlay)
                        cueOrLoadVideo(videoId, 0f)
                    else
                        youTubePlayer.cueVideo(videoId, 0f)
                }

                youTubePlayer.removeListener(this)
            }
        }

        if(!useWebUI) {
            youTubePlayerView.getPlayerUIController().enableLiveVideoUI(enableLiveVideoUI)
            youTubePlayerView.getPlayerUIController().showYouTubeButton(showYouTubeButton)
            youTubePlayerView.getPlayerUIController().showFullscreenButton(showFullScreenButton)
            youTubePlayerView.getPlayerUIController().showCurrentTime(showVideoCurrentTime)
            youTubePlayerView.getPlayerUIController().showDuration(showVideoDuration)
            youTubePlayerView.getPlayerUIController().showSeekBar(showSeekBar)
            youTubePlayerView.initialize(youTubePlayerListener, handleNetworkEvents)
        } else {
            youTubePlayerView.initializeWithWebUI(youTubePlayerListener, handleNetworkEvents)
        }


        youTubePlayerView.addFullScreenListener(object : YouTubePlayerFullScreenListener {
            override fun onYouTubePlayerEnterFullScreen() {
                fullScreenHelper.enterFullScreen()
            }

            override fun onYouTubePlayerExitFullScreen() {
                fullScreenHelper.exitFullScreen()
            }
        })
    }

    fun cueOrLoadVideo(videoId: String, startSeconds: Float) {
        if (canPlay)
            youTubePlayerView.youTubePlayer.loadVideo(videoId, startSeconds)
        else
            youTubePlayerView.youTubePlayer.cueVideo(videoId, startSeconds)
    }

    fun addListener(youTubePlayerListener: YouTubePlayerListener) {
        youTubePlayerView.youTubePlayer.addListener(youTubePlayerListener)
    }

    fun removeListener(youTubePlayerListener: YouTubePlayerListener) {
        youTubePlayerView.youTubePlayer.removeListener(youTubePlayerListener)
    }

    fun getPlayerUIController(): PlayerUIController = youTubePlayerView.getPlayerUIController()

    /**
     * See [YouTubePlayerView.release]
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun release() = youTubePlayerView.release()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        canPlay = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onStop() {
        canPlay = false
        youTubePlayerView.youTubePlayer.pause()
    }

    fun enterFullScreen() {
        youTubePlayerView.enterFullScreen()
    }

    fun exitFullScreen() {
        youTubePlayerView.exitFullScreen()
    }

    fun toggleFullScreen() {
        youTubePlayerView.toggleFullScreen()
    }

    fun isFullScreen(): Boolean = fullScreenHelper.isFullScreen

    fun addFullScreenListener(fullScreenListener: YouTubePlayerFullScreenListener): Boolean =
            fullScreenHelper.addFullScreenListener(fullScreenListener)

    fun removeFullScreenListener(fullScreenListener: YouTubePlayerFullScreenListener): Boolean =
            fullScreenHelper.removeFullScreenListener(fullScreenListener)
}