package com.pierfrancescosoffritti.androidyoutubeplayer.player.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.pierfrancescosoffritti.androidyoutubeplayer.R
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.player.utils.FullScreenHelper
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.PlayerUIController
import com.pierfrancescosoffritti.androidyoutubeplayer.utils.SixteenByNineFrameLayout


class ManagedYouTubePlayerView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
        SixteenByNineFrameLayout(context, attrs, defStyleAttr), LifecycleObserver {

    constructor(context: Context): this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet? = null): this(context, attrs, 0)

    private val youTubePlayerView: YouTubePlayerView = YouTubePlayerView(context)
    private val fullScreenHelper = FullScreenHelper(this)

    private var canPlay = false

    var enableAutomaticInitialization: Boolean

    init {
        addView(youTubePlayerView, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.ManagedYouTubePlayerView, 0, 0)

        enableAutomaticInitialization = typedArray.getBoolean(R.styleable.ManagedYouTubePlayerView_enableAutomaticInitialization, true)
        val autoPlay = typedArray.getBoolean(R.styleable.ManagedYouTubePlayerView_autoPlay, false)
        val handleNetworkEvents = typedArray.getBoolean(R.styleable.ManagedYouTubePlayerView_handleNetworkEvents, true)
        val videoId = typedArray.getString(R.styleable.ManagedYouTubePlayerView_videoId)

        val useWebUi = typedArray.getBoolean(R.styleable.ManagedYouTubePlayerView_useWebUi, false)
        val enableLiveVideoUI = typedArray.getBoolean(R.styleable.ManagedYouTubePlayerView_enableLiveVideoUi, false)
        val showYouTubeButton = typedArray.getBoolean(R.styleable.ManagedYouTubePlayerView_showYouTubeButton, true)
        val showFullScreenButton = typedArray.getBoolean(R.styleable.ManagedYouTubePlayerView_showFullScreenButton, true)
        val showVideoCurrentTime = typedArray.getBoolean(R.styleable.ManagedYouTubePlayerView_showVideoCurrentTime, true)
        val showVideoDuration = typedArray.getBoolean(R.styleable.ManagedYouTubePlayerView_showVideoDuration, true)
        val showSeekBar = typedArray.getBoolean(R.styleable.ManagedYouTubePlayerView_showSeekBar, true)

        typedArray.recycle()

        if(!enableAutomaticInitialization && useWebUi) {
            throw IllegalStateException("'enableAutomaticInitialization' is false and 'useWebUi' is set to true. " +
                    "This is not possible, if you want to manually initialize the player and use the web ui, " +
                    "you should manually initialize the YouTubePlayerView using 'initializeWithWebUI'")
        }

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

        if(!useWebUi) {
            youTubePlayerView.getPlayerUIController().enableLiveVideoUI(enableLiveVideoUI)
            youTubePlayerView.getPlayerUIController().showYouTubeButton(showYouTubeButton)
            youTubePlayerView.getPlayerUIController().showFullscreenButton(showFullScreenButton)
            youTubePlayerView.getPlayerUIController().showCurrentTime(showVideoCurrentTime)
            youTubePlayerView.getPlayerUIController().showDuration(showVideoDuration)
            youTubePlayerView.getPlayerUIController().showSeekBar(showSeekBar)
        }

        if(enableAutomaticInitialization) {
            if(useWebUi) youTubePlayerView.initializeWithWebUI(youTubePlayerListener, handleNetworkEvents)
            else youTubePlayerView.initialize(youTubePlayerListener, handleNetworkEvents)
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

    /**
     * @see YouTubePlayerView.initialize
     */
    fun initialize(youTubePlayerListener: YouTubePlayerListener, handleNetworkEvents: Boolean, playerOptions: IFramePlayerOptions?) {
        if (enableAutomaticInitialization) throw IllegalStateException("If you want to initialize this player manually, you need to set 'enableAutomaticInitialization' to false")
        else youTubePlayerView.initialize(youTubePlayerListener, handleNetworkEvents, playerOptions)
    }

    /**
     * @see YouTubePlayerView.initialize
     */
    fun initialize(youTubePlayerListener: YouTubePlayerListener, handleNetworkEvents: Boolean) {
        if(enableAutomaticInitialization) throw IllegalStateException("If you want to initialize this player manually, you need to set 'enableAutomaticInitialization' to false")
        else youTubePlayerView.initialize(youTubePlayerListener, handleNetworkEvents, null)
    }

    /**
     * @see YouTubePlayerView.initialize
     */
    fun initialize(youTubePlayerListener: YouTubePlayerListener) {
        if (enableAutomaticInitialization) throw IllegalStateException("If you want to initialize this player manually, you need to set 'enableAutomaticInitialization' to false")
        else youTubePlayerView.initialize(youTubePlayerListener, true)
    }

    /**
     * @see YouTubePlayerView.initializeWithWebUI
     */
    fun initializeWithWebUI(youTubePlayerListener: YouTubePlayerListener, handleNetworkEvents: Boolean) {
        if(enableAutomaticInitialization) throw IllegalStateException("If you want to initialize this player manually, you need to set 'enableAutomaticInitialization' to false")
        else youTubePlayerView.initializeWithWebUI(youTubePlayerListener, handleNetworkEvents)
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

    /**
     * @see YouTubePlayerView.getYouTubePlayerWhenReady
     */
    fun getYouTubePlayerWhenReady(youTubePlayerCallback: YouTubePlayerCallback) {
        youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayerCallback)
    }

    /**
     * @see YouTubePlayerView.inflateCustomPlayerUI
     */
    fun inflateCustomPlayerUI(@LayoutRes layoutId: Int): View = youTubePlayerView.inflateCustomPlayerUI(layoutId)

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

    /**
     * @see YouTubePlayerView.enableBackgroundPlayback
     */
    fun enableBackgroundPlayback(enable: Boolean) = youTubePlayerView.enableBackgroundPlayback(enable)

    fun getPlayerUIController(): PlayerUIController = youTubePlayerView.getPlayerUIController()

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