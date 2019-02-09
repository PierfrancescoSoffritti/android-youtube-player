package com.pierfrancescosoffritti.androidyoutubeplayer.player

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.pierfrancescosoffritti.androidyoutubeplayer.R
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.player.utils.FullScreenHelper
import com.pierfrancescosoffritti.androidyoutubeplayer.player.utils.PlaybackResumer
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.DefaultPlayerUIController
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.PlayerUIController
import com.pierfrancescosoffritti.androidyoutubeplayer.utils.NetworkListener
import com.pierfrancescosoffritti.androidyoutubeplayer.utils.SixteenNineRatioFrameLayout

class YouTubePlayerView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
        SixteenNineRatioFrameLayout(context, attrs, defStyleAttr), LifecycleObserver {

    constructor(context: Context): this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet? = null): this(context, attrs, 0)

    private val youTubePlayer: WebViewYouTubePlayer = WebViewYouTubePlayer(context)
    private val defaultPlayerUIController: DefaultPlayerUIController

    private val networkListener = NetworkListener()
    private val playbackResumer = PlaybackResumer()
    private val fullScreenHelper = FullScreenHelper(this)

    private var isInitialized = false
    private var asyncInitialization = { }

    var isUsingCustomUI = false
        private set

    init {
        addView(youTubePlayer, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        defaultPlayerUIController = DefaultPlayerUIController(this, youTubePlayer)

        fullScreenHelper.addFullScreenListener(defaultPlayerUIController)

        youTubePlayer.addListener(defaultPlayerUIController)
        youTubePlayer.addListener(playbackResumer)

        youTubePlayer.addListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                isInitialized = true
                youTubePlayer.removeListener(this)
            }
        })

        networkListener.onNetworkAvailable = {
            if (!isInitialized)
                asyncInitialization()
            else
                playbackResumer.resume(youTubePlayer)
        }
    }

    /**
     * Initialize the player. You must call this method before using the player.
     * @param youTubePlayerListener listener for player events
     * @param handleNetworkEvents if set to true a broadcast receiver will be registered and network events will be handled automatically.
     * If set to false, you should handle network events with your own broadcast receiver.
     * @param playerOptions customizable options for the embedded video player, can be null.
     */
    fun initialize(youTubePlayerListener: YouTubePlayerListener, handleNetworkEvents: Boolean, playerOptions: IFramePlayerOptions?) {
        if (handleNetworkEvents)
            context.registerReceiver(networkListener, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        asyncInitialization = {
            youTubePlayer.initialize(object : YouTubePlayerInitListener {
                override fun onInitSuccess(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.addListener(youTubePlayerListener)
                }
            }, playerOptions)
        }
    }

    /**
     * Initialize the player using the provided [IFramePlayerOptions] object.
     * @see YouTubePlayerView.initialize
     */
    fun initialize(youTubePlayerListener: YouTubePlayerListener, handleNetworkEvents: Boolean) =
            initialize(youTubePlayerListener, handleNetworkEvents, null)

    /**
     * Initialize the player. Network events are automatically handled by the player.
     * @see YouTubePlayerView.initialize
     */
    fun initialize(youTubePlayerListener: YouTubePlayerListener) =
            initialize(youTubePlayerListener, true)

    /**
     * Initialize a player using the web-base UI instead pf the native UI.
     * The default PlayerUIController will be removed and [YouTubePlayerView.getPlayerUIController] will throw exception.
     * @see YouTubePlayerView.initialize
     */
    fun initializeWithWebUI(youTubePlayerListener: YouTubePlayerListener, handleNetworkEvents: Boolean) {
        val iFramePlayerOptions = IFramePlayerOptions.Builder().controls(1).build()
        inflateCustomPlayerUI(R.layout.ayp_empty_layout)
        initialize(youTubePlayerListener, handleNetworkEvents, iFramePlayerOptions)
    }

    /**
     * Use this method to replace the default UI of the player with a custom UI.
     *
     * You will be responsible to manage the custom UI from your application,
     * the default controller obtained through [YouTubePlayerView.getPlayerUIController] won't be available anymore.
     * @param customLayoutID the ID of the layout defining the custom UI.
     * @return The inflated View
     */
    fun inflateCustomPlayerUI(@LayoutRes customLayoutID: Int): View {
        removeViews(1, childCount - 1)

        if (!isUsingCustomUI) {
            youTubePlayer.removeListener(defaultPlayerUIController)
            fullScreenHelper.removeFullScreenListener(defaultPlayerUIController)
        }

        isUsingCustomUI = true

        return View.inflate(context, customLayoutID, this)
    }

    /**
     * Call this method before destroying the host Fragment/Activity, or register this View as an observer of its host lifecycle
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun release() {
        removeView(youTubePlayer)
        youTubePlayer.removeAllViews()
        youTubePlayer.destroy()
        try {
            context.unregisterReceiver(networkListener)
        } catch (ignore: Exception) {
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onStop() =
            youTubePlayer.pause()

    fun enableBackgroundPlayback(enable: Boolean) {
        youTubePlayer.backgroundPlaybackEnabled = enable
    }

    fun getPlayerUIController(): PlayerUIController {
        if (isUsingCustomUI)
            throw RuntimeException("You have inflated a custom player UI. You must manage it with your own controller.")

        return defaultPlayerUIController
    }

    fun enterFullScreen() = fullScreenHelper.enterFullScreen()

    fun exitFullScreen() = fullScreenHelper.exitFullScreen()

    fun toggleFullScreen() = fullScreenHelper.toggleFullScreen()

    fun isFullScreen(): Boolean = fullScreenHelper.isFullScreen

    fun addFullScreenListener(fullScreenListener: YouTubePlayerFullScreenListener): Boolean =
            fullScreenHelper.addFullScreenListener(fullScreenListener)

    fun removeFullScreenListener(fullScreenListener: YouTubePlayerFullScreenListener): Boolean =
            fullScreenHelper.removeFullScreenListener(fullScreenListener)
}