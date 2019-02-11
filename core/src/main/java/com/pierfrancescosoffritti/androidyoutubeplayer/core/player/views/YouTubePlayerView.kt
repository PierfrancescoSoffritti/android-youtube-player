package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views

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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.FullScreenHelper
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.PlayerUiController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo


class YouTubePlayerView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
        SixteenByNineFrameLayout(context, attrs, defStyleAttr), LifecycleObserver {

    constructor(context: Context): this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet? = null): this(context, attrs, 0)

    private val legacyTubePlayerView: LegacyYouTubePlayerView = LegacyYouTubePlayerView(context)
    private val fullScreenHelper = FullScreenHelper(this)

    var enableAutomaticInitialization: Boolean

    init {
        addView(legacyTubePlayerView, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.YouTubePlayerView, 0, 0)

        enableAutomaticInitialization = typedArray.getBoolean(R.styleable.YouTubePlayerView_enableAutomaticInitialization, true)
        val autoPlay = typedArray.getBoolean(R.styleable.YouTubePlayerView_autoPlay, false)
        val handleNetworkEvents = typedArray.getBoolean(R.styleable.YouTubePlayerView_handleNetworkEvents, true)
        val videoId = typedArray.getString(R.styleable.YouTubePlayerView_videoId)

        val useWebUi = typedArray.getBoolean(R.styleable.YouTubePlayerView_useWebUi, false)
        val enableLiveVideoUi = typedArray.getBoolean(R.styleable.YouTubePlayerView_enableLiveVideoUi, false)
        val showYouTubeButton = typedArray.getBoolean(R.styleable.YouTubePlayerView_showYouTubeButton, true)
        val showFullScreenButton = typedArray.getBoolean(R.styleable.YouTubePlayerView_showFullScreenButton, true)
        val showVideoCurrentTime = typedArray.getBoolean(R.styleable.YouTubePlayerView_showVideoCurrentTime, true)
        val showVideoDuration = typedArray.getBoolean(R.styleable.YouTubePlayerView_showVideoDuration, true)
        val showSeekBar = typedArray.getBoolean(R.styleable.YouTubePlayerView_showSeekBar, true)

        typedArray.recycle()

        if(!enableAutomaticInitialization && useWebUi) {
            throw IllegalStateException("YouTubePlayerView: 'enableAutomaticInitialization' is false and 'useWebUi' is set to true. " +
                    "This is not possible, if you want to manually initialize YouTubePlayerView and use the web ui, " +
                    "you should manually initialize the YouTubePlayerView using 'initializeWithWebUi'")
        }

        if(videoId == null && autoPlay)
            throw IllegalStateException("YouTubePlayerView: videoId is not set but autoPlay is set to true. This combination is not possible.")

        if(!useWebUi) {
            legacyTubePlayerView.getPlayerUiController()
                    .enableLiveVideoUi(enableLiveVideoUi)
                    .showYouTubeButton(showYouTubeButton)
                    .showFullscreenButton(showFullScreenButton)
                    .showCurrentTime(showVideoCurrentTime)
                    .showDuration(showVideoDuration)
                    .showSeekBar(showSeekBar)
        }

        val youTubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                videoId?.let {
                    youTubePlayer.loadOrCueVideo(legacyTubePlayerView.canPlay && autoPlay, videoId, 0f)
                }

                youTubePlayer.removeListener(this)
            }
        }

        if(enableAutomaticInitialization) {
            if(useWebUi) legacyTubePlayerView.initializeWithWebUi(youTubePlayerListener, handleNetworkEvents)
            else legacyTubePlayerView.initialize(youTubePlayerListener, handleNetworkEvents)
        }


        legacyTubePlayerView.addFullScreenListener(object : YouTubePlayerFullScreenListener {
            override fun onYouTubePlayerEnterFullScreen() {
                fullScreenHelper.enterFullScreen()
            }

            override fun onYouTubePlayerExitFullScreen() {
                fullScreenHelper.exitFullScreen()
            }
        })
    }

    /**
     * Initialize the player. You must call this method before using the player.
     * @param youTubePlayerListener listener for player events
     * @param handleNetworkEvents if set to true a broadcast receiver will be registered and network events will be handled automatically.
     * If set to false, you should handle network events with your own broadcast receiver.
     * @param playerOptions customizable options for the embedded video player, can be null.
     */
    fun initialize(youTubePlayerListener: YouTubePlayerListener, handleNetworkEvents: Boolean, playerOptions: IFramePlayerOptions?) {
        if (enableAutomaticInitialization) throw IllegalStateException("YouTubePlayerView: If you want to initialize this view manually, you need to set 'enableAutomaticInitialization' to false")
        else legacyTubePlayerView.initialize(youTubePlayerListener, handleNetworkEvents, playerOptions)
    }

    /**
     * Initialize the player.
     * @param handleNetworkEvents if set to true a broadcast receiver will be registered and network events will be handled automatically.
     * If set to false, you should handle network events with your own broadcast receiver.
     *
     * @see YouTubePlayerView.initialize
     */
    fun initialize(youTubePlayerListener: YouTubePlayerListener, handleNetworkEvents: Boolean) {
        if(enableAutomaticInitialization) throw IllegalStateException("YouTubePlayerView: If you want to initialize this view manually, you need to set 'enableAutomaticInitialization' to false")
        else legacyTubePlayerView.initialize(youTubePlayerListener, handleNetworkEvents, null)
    }

    /**
     * Initialize the player. Network events are automatically handled by the player.
     * @param youTubePlayerListener listener for player events
     *
     * @see YouTubePlayerView.initialize
     */
    fun initialize(youTubePlayerListener: YouTubePlayerListener) {
        if (enableAutomaticInitialization) throw IllegalStateException("YouTubePlayerView: If you want to initialize this view manually, you need to set 'enableAutomaticInitialization' to false")
        else legacyTubePlayerView.initialize(youTubePlayerListener, true)
    }

    /**
     * Initialize a player using the web-base Ui instead pf the native Ui.
     * The default PlayerUiController will be removed and [YouTubePlayerView.getPlayerUiController] will throw exception.
     *
     * @see YouTubePlayerView.initialize
     */
    fun initializeWithWebUi(youTubePlayerListener: YouTubePlayerListener, handleNetworkEvents: Boolean) {
        if(enableAutomaticInitialization) throw IllegalStateException("YouTubePlayerView: If you want to initialize this view manually, you need to set 'enableAutomaticInitialization' to false")
        else legacyTubePlayerView.initializeWithWebUi(youTubePlayerListener, handleNetworkEvents)
    }

    /**
     * @param youTubePlayerCallback A callback that will be called when the YouTubePlayer is ready.
     * If the player is ready when the function is called, the callback is called immediately.
     * This function is called only once.
     */
    fun getYouTubePlayerWhenReady(youTubePlayerCallback: YouTubePlayerCallback) =
        legacyTubePlayerView.getYouTubePlayerWhenReady(youTubePlayerCallback)

    /**
     * Use this method to replace the default Ui of the player with a custom Ui.
     *
     * You will be responsible to manage the custom Ui from your application,
     * the default controller obtained through [YouTubePlayerView.getPlayerUiController] won't be available anymore.
     * @param layoutId the ID of the layout defining the custom Ui.
     * @return The inflated View
     */
    fun inflateCustomPlayerUi(@LayoutRes layoutId: Int): View = legacyTubePlayerView.inflateCustomPlayerUi(layoutId)

    fun getPlayerUiController(): PlayerUiController = legacyTubePlayerView.getPlayerUiController()

    /**
     * Don't use this method if you want to publish your app on the PlayStore. Background playback is against YouTube terms of service.
     */
    fun enableBackgroundPlayback(enable: Boolean) = legacyTubePlayerView.enableBackgroundPlayback(enable)

    /**
     * Call this method before destroying the host Fragment/Activity, or register this View as an observer of its host lifecycle
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun release() = legacyTubePlayerView.release()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() = legacyTubePlayerView.onResume()

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onStop() = legacyTubePlayerView.onStop()

    fun addYouTubePlayerListener(youTubePlayerListener: YouTubePlayerListener) =
            legacyTubePlayerView.youTubePlayer.addListener(youTubePlayerListener)

    fun removeYouTubePlayerListener(youTubePlayerListener: YouTubePlayerListener) =
            legacyTubePlayerView.youTubePlayer.removeListener(youTubePlayerListener)

    fun enterFullScreen() = legacyTubePlayerView.enterFullScreen()

    fun exitFullScreen() = legacyTubePlayerView.exitFullScreen()

    fun toggleFullScreen() = legacyTubePlayerView.toggleFullScreen()

    fun isFullScreen(): Boolean = fullScreenHelper.isFullScreen

    fun addFullScreenListener(fullScreenListener: YouTubePlayerFullScreenListener): Boolean =
            fullScreenHelper.addFullScreenListener(fullScreenListener)

    fun removeFullScreenListener(fullScreenListener: YouTubePlayerFullScreenListener): Boolean =
            fullScreenHelper.removeFullScreenListener(fullScreenListener)
}