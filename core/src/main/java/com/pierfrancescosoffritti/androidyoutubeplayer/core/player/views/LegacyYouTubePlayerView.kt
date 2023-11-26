package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.*
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.NetworkObserver
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.PlaybackResumer

/**
 * Legacy internal implementation of YouTubePlayerView. The user facing YouTubePlayerView delegates
 * most of its actions to this one.
 */
internal class LegacyYouTubePlayerView(
    context: Context,
    listener: FullscreenListener?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SixteenByNineFrameLayout(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, FakeWebViewYouTubeListener, null, 0)

    internal var webViewYouTubePlayer: WebViewYouTubePlayer? = null

    private val networkObserver = NetworkObserver(context.applicationContext)
    private val playbackResumer = PlaybackResumer()

    internal var isYouTubePlayerReady = false
    private var initialize = { }
    private val youTubePlayerCallbacks = mutableSetOf<YouTubePlayerCallback>()

    internal var canPlay = true
        private set

    init {
        //webview exception
        webViewYouTubePlayer = try {
            WebViewYouTubePlayer(context, listener)
        } catch (e: Exception) {
            null
        }
        if (webViewYouTubePlayer != null) {
            addView(
                webViewYouTubePlayer,
                LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }

        webViewYouTubePlayer?.addListener(playbackResumer)

        // stop playing if the user loads a video but then leaves the app before the video starts playing.
        webViewYouTubePlayer?.addListener(object : AbstractYouTubePlayerListener() {
            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                if (state == PlayerConstants.PlayerState.PLAYING && !isEligibleForPlayback()) {
                    youTubePlayer.pause()
                }
            }
        })

        webViewYouTubePlayer?.addListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                isYouTubePlayerReady = true

                youTubePlayerCallbacks.forEach { it.onYouTubePlayer(youTubePlayer) }
                youTubePlayerCallbacks.clear()

                youTubePlayer.removeListener(this)
            }
        })

        networkObserver.listeners.add(object : NetworkObserver.Listener {
            override fun onNetworkAvailable() {
                if (!isYouTubePlayerReady) {
                    initialize()
                } else {
                    webViewYouTubePlayer?.youtubePlayer?.let {
                        playbackResumer.resume(it)
                    }

                }
            }

            override fun onNetworkUnavailable() {}
        })
    }

    /**
     * Initialize the player. You must call this method before using the player.
     * @param youTubePlayerListener listener for player events
     * @param handleNetworkEvents if set to true a broadcast receiver will be registered and network events will be handled automatically.
     * If set to false, you should handle network events with your own broadcast receiver.
     * @param playerOptions customizable options for the embedded video player, can be null.
     */
    fun initialize(
        youTubePlayerListener: YouTubePlayerListener,
        handleNetworkEvents: Boolean,
        playerOptions: IFramePlayerOptions
    ) {
        if (isYouTubePlayerReady) {
            throw IllegalStateException("This YouTubePlayerView has already been initialized.")
        }

        if (handleNetworkEvents) {
            networkObserver.observeNetwork()
        }

        initialize = {
            webViewYouTubePlayer?.initialize(
                { it.addListener(youTubePlayerListener) },
                playerOptions
            )
        }

        if (!handleNetworkEvents) {
            initialize()
        }
    }

    /**
     * Initialize the player.
     * @param handleNetworkEvents if set to true a broadcast receiver will be registered and network events will be handled automatically.
     * If set to false, you should handle network events with your own broadcast receiver.
     *
     * @see LegacyYouTubePlayerView.initialize
     */
    fun initialize(youTubePlayerListener: YouTubePlayerListener, handleNetworkEvents: Boolean) =
        initialize(youTubePlayerListener, handleNetworkEvents, IFramePlayerOptions.default)

    /**
     * Initialize the player. Network events are automatically handled by the player.
     * @param youTubePlayerListener listener for player events
     *
     * @see LegacyYouTubePlayerView.initialize
     */
    fun initialize(youTubePlayerListener: YouTubePlayerListener) =
        initialize(youTubePlayerListener, true)

    /**
     * @param youTubePlayerCallback A callback that will be called when the YouTubePlayer is ready.
     * If the player is ready when the function is called, the callback is called immediately.
     * This function is called only once.
     */
    fun getYouTubePlayerWhenReady(youTubePlayerCallback: YouTubePlayerCallback) {
        if (isYouTubePlayerReady) {
            webViewYouTubePlayer?.youtubePlayer?.let {
                youTubePlayerCallback.onYouTubePlayer(it)
            }
        } else {
            youTubePlayerCallbacks.add(youTubePlayerCallback)
        }
    }

    /**
     * Use this method to replace the default Ui of the player with a custom Ui.
     *
     * You will be responsible to manage the custom Ui from your application,
     * the default controller obtained through [LegacyYouTubePlayerView.getPlayerUiController] won't be available anymore.
     * @param layoutId the ID of the layout defining the custom Ui.
     * @return The inflated View
     */
    fun inflateCustomPlayerUi(@LayoutRes layoutId: Int): View {
        if (childCount >= 1) {
            removeViews(1, childCount - 1)
        }
        return View.inflate(context, layoutId, this)
    }

    fun setCustomPlayerUi(view: View) {
        if (childCount >= 1) {
            removeViews(1, childCount - 1)
        }
        addView(view)
    }

    /**
     * Call this method before destroying the host Fragment/Activity, or register this View as an observer of its host lifecycle
     */
    fun release() {
        networkObserver.destroy()
        if (webViewYouTubePlayer != null) {
            removeView(webViewYouTubePlayer)
        }
        webViewYouTubePlayer?.removeAllViews()
        webViewYouTubePlayer?.destroy()
    }

    internal fun onResume() {
        playbackResumer.onLifecycleResume()
        canPlay = true
    }

    internal fun onStop() {
        webViewYouTubePlayer?.youtubePlayer?.pause()
        playbackResumer.onLifecycleStop()
        canPlay = false
    }

    /**
     * Checks whether the player is in an eligible state for playback in
     * respect of the {@link WebViewYouTubePlayer#isBackgroundPlaybackEnabled}
     * property.
     */
    internal fun isEligibleForPlayback(): Boolean {
        if (webViewYouTubePlayer == null) {
            return false
        }
        return canPlay || webViewYouTubePlayer!!.isBackgroundPlaybackEnabled
    }

    /**
     * Don't use this method if you want to publish your app on the PlayStore. Background playback is against YouTube terms of service.
     */
    fun enableBackgroundPlayback(enable: Boolean) {
        webViewYouTubePlayer?.isBackgroundPlaybackEnabled = enable
    }
}
