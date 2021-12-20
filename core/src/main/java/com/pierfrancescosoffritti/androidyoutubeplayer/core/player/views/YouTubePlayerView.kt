package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
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

    // this is a publicly accessible API
    var enableAutomaticInitialization: Boolean

    init {
        addView(legacyTubePlayerView, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.YouTubePlayerView, 0, 0)

        enableAutomaticInitialization = typedArray.getBoolean(R.styleable.YouTubePlayerView_enableAutomaticInitialization, true)
        val autoPlay = typedArray.getBoolean(R.styleable.YouTubePlayerView_autoPlay, false)
        val handleNetworkEvents = typedArray.getBoolean(R.styleable.YouTubePlayerView_handleNetworkEvents, true)
        val videoId = typedArray.getString(R.styleable.YouTubePlayerView_videoId)

        typedArray.recycle()

        if(autoPlay && videoId == null) {
            throw IllegalStateException("YouTubePlayerView: videoId is not set but autoPlay is set to true. This combination is not allowed.")
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
            legacyTubePlayerView.initialize(youTubePlayerListener, handleNetworkEvents, IFramePlayerOptions.default)
        }
    }

    /**
     * Initialize the player. You must call this method before using the player.
     * @param youTubePlayerListener listener for player events
     * @param handleNetworkEvents if set to true a broadcast receiver will be registered and network events will be handled automatically.
     * If set to false, you should handle network events with your own broadcast receiver.
     * @param playerOptions customizable options for the embedded video player.
     */
    fun initialize(youTubePlayerListener: YouTubePlayerListener, handleNetworkEvents: Boolean, playerOptions: IFramePlayerOptions) {
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
        else legacyTubePlayerView.initialize(youTubePlayerListener, handleNetworkEvents, IFramePlayerOptions.default)
    }

    /**
     * Initialize the player with player options.
     *
     * @see YouTubePlayerView.initialize
     */
    fun initialize(youTubePlayerListener: YouTubePlayerListener, playerOptions: IFramePlayerOptions) {
        if(enableAutomaticInitialization) throw IllegalStateException("YouTubePlayerView: If you want to initialize this view manually, you need to set 'enableAutomaticInitialization' to false")
        else legacyTubePlayerView.initialize(youTubePlayerListener, true, playerOptions)
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
     * @param youTubePlayerCallback A callback that will be called when the YouTubePlayer is ready.
     * If the player is ready when the function is called, the callback will return immediately.
     * This function is called only once.
     */
    fun getYouTubePlayerWhenReady(youTubePlayerCallback: YouTubePlayerCallback) = legacyTubePlayerView.getYouTubePlayerWhenReady(youTubePlayerCallback)

    /**
     * Use this method to add your own custom UI to the player.
     *
     * You will be responsible to manage the custom Ui from your application.
     *
     * WARNING: if yoy intend to publish your app on the PlayStore, using a custom UI might break YouTube terms of service.
     *
     * @param layoutId the ID of the layout defining the custom Ui.
     * @return The inflated View
     */
    fun inflateCustomPlayerUi(@LayoutRes layoutId: Int) = legacyTubePlayerView.inflateCustomPlayerUi(layoutId)

    fun setCustomPlayerUi(view: View) = legacyTubePlayerView.setCustomPlayerUi(view)

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

    fun addYouTubePlayerListener(youTubePlayerListener: YouTubePlayerListener) = legacyTubePlayerView.youTubePlayer.addListener(youTubePlayerListener)

    fun removeYouTubePlayerListener(youTubePlayerListener: YouTubePlayerListener) = legacyTubePlayerView.youTubePlayer.removeListener(youTubePlayerListener)

    fun enterFullScreen() = fullScreenHelper.enterFullScreen()

    fun exitFullScreen() = fullScreenHelper.exitFullScreen()

    fun toggleFullScreen() = fullScreenHelper.toggleFullScreen()

    fun isFullScreen() = fullScreenHelper.isFullScreen

    /**
     * Adds a [YouTubePlayerFullScreenListener] to the view.
     *
     * [YouTubePlayerFullScreenListener.onYouTubePlayerEnterFullScreen] and [YouTubePlayerFullScreenListener.onYouTubePlayerExitFullScreen]
     * are called when [YouTubePlayerView.enterFullScreen] and [YouTubePlayerView.exitFullScreen] are called.
     */
    fun addFullScreenListener(fullScreenListener: YouTubePlayerFullScreenListener) = fullScreenHelper.addFullScreenListener(fullScreenListener)

    fun removeFullScreenListener(fullScreenListener: YouTubePlayerFullScreenListener) = fullScreenHelper.removeFullScreenListener(fullScreenListener)
}