package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.lifecycle.*
import com.pierfrancescosoffritti.androidyoutubeplayer.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.*
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo

private const val AUTO_INIT_ERROR = "YouTubePlayerView: If you want to initialize this view manually, " +
        "you need to set 'enableAutomaticInitialization' to false."

private val matchParent
  get() = FrameLayout.LayoutParams(
    LayoutParams.MATCH_PARENT,
    LayoutParams.MATCH_PARENT
  )

class YouTubePlayerView(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : SixteenByNineFrameLayout(context, attrs, defStyleAttr), LifecycleEventObserver {

  constructor(context: Context) : this(context, null, 0)
  constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0)

  private val fullscreenListeners = mutableListOf<FullscreenListener>()

  /**
   * A single [FullscreenListener] that is always added to the WebView,
   * responsible for calling all optional listeners added from clients of the library.
   */
  private val webViewFullscreenListener = object : FullscreenListener {
    override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
      if (fullscreenListeners.isEmpty()) {
        throw IllegalStateException("To enter fullscreen you need to first register a FullscreenListener.")
      }
      fullscreenListeners.forEach { it.onEnterFullscreen(fullscreenView, exitFullscreen) }
    }

    override fun onExitFullscreen() {
      if (fullscreenListeners.isEmpty()) {
        throw IllegalStateException("To enter fullscreen you need to first register a FullscreenListener.")
      }
      fullscreenListeners.forEach { it.onExitFullscreen() }
    }
  }

  private val legacyTubePlayerView = LegacyYouTubePlayerView(context, webViewFullscreenListener)

  // this is a publicly accessible API
  var enableAutomaticInitialization: Boolean

  init {
    addView(legacyTubePlayerView, matchParent)

    val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.YouTubePlayerView, 0, 0)

    enableAutomaticInitialization = typedArray.getBoolean(R.styleable.YouTubePlayerView_enableAutomaticInitialization, true)
    val autoPlay = typedArray.getBoolean(R.styleable.YouTubePlayerView_autoPlay, false)
    val handleNetworkEvents = typedArray.getBoolean(R.styleable.YouTubePlayerView_handleNetworkEvents, true)
    val videoId = typedArray.getString(R.styleable.YouTubePlayerView_videoId)

    typedArray.recycle()

    if (autoPlay && videoId == null) {
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

    if (enableAutomaticInitialization) {
      legacyTubePlayerView.initialize(
        youTubePlayerListener,
        handleNetworkEvents,
        IFramePlayerOptions.default,
        videoId
      )
    }
  }

  // TODO: Use @JvmOverloads instead of duplicating the method. Unfortunately that will cause a breaking change.
  fun initialize(youTubePlayerListener: YouTubePlayerListener, handleNetworkEvents: Boolean, playerOptions: IFramePlayerOptions, videoId: String?) {
    if (enableAutomaticInitialization) {
      throw IllegalStateException(AUTO_INIT_ERROR)
    }
    else {
      legacyTubePlayerView.initialize(youTubePlayerListener, handleNetworkEvents, playerOptions, videoId)
    }
  }

  /**
   * Initialize the player. You must call this method before using the player.
   * @param youTubePlayerListener listener for player events
   * @param handleNetworkEvents if set to true a broadcast receiver will be registered and network events will be handled automatically.
   * If set to false, you should handle network events with your own broadcast receiver.
   * @param playerOptions customizable options for the embedded video player.
   * @param videoId optional, used to load an initial video.
   */
  fun initialize(youTubePlayerListener: YouTubePlayerListener, handleNetworkEvents: Boolean, playerOptions: IFramePlayerOptions) {
    if (enableAutomaticInitialization) {
      throw IllegalStateException(AUTO_INIT_ERROR)
    }
    else {
      legacyTubePlayerView.initialize(youTubePlayerListener, handleNetworkEvents, playerOptions, null)
    }
  }

  /**
   * Initialize the player.
   * @param handleNetworkEvents if set to true a broadcast receiver will be registered and network events will be handled automatically.
   * If set to false, you should handle network events with your own broadcast receiver.
   *
   * @see YouTubePlayerView.initialize
   */
  fun initialize(youTubePlayerListener: YouTubePlayerListener, handleNetworkEvents: Boolean) {
    if (enableAutomaticInitialization) {
      throw IllegalStateException(AUTO_INIT_ERROR)
    }
    else {
      legacyTubePlayerView.initialize(youTubePlayerListener, handleNetworkEvents, IFramePlayerOptions.default)
    }
  }

  /**
   * Initialize the player with player options.
   *
   * @see YouTubePlayerView.initialize
   */
  fun initialize(youTubePlayerListener: YouTubePlayerListener, playerOptions: IFramePlayerOptions) {
    if (enableAutomaticInitialization) {
      throw IllegalStateException(AUTO_INIT_ERROR)
    }
    else {
      legacyTubePlayerView.initialize(youTubePlayerListener, true, playerOptions)
    }
  }

  /**
   * Initialize the player. Network events are automatically handled by the player.
   * @param youTubePlayerListener listener for player events
   *
   * @see YouTubePlayerView.initialize
   */
  fun initialize(youTubePlayerListener: YouTubePlayerListener) {
    if (enableAutomaticInitialization) {
      throw IllegalStateException(AUTO_INIT_ERROR)
    }
    else {
      legacyTubePlayerView.initialize(youTubePlayerListener, true)
    }
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

  override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
    when (event) {
      Lifecycle.Event.ON_RESUME -> onResume()
      Lifecycle.Event.ON_STOP -> onStop()
      Lifecycle.Event.ON_DESTROY -> release()
      Lifecycle.Event.ON_CREATE, Lifecycle.Event.ON_START, Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_ANY -> { }
    }
  }

  /**
   * Call this method before destroying the host Fragment/Activity, or register this View as an observer of its host lifecycle
   */
  fun release() = legacyTubePlayerView.release()

  private fun onResume() = legacyTubePlayerView.onResume()

  private fun onStop() = legacyTubePlayerView.onStop()

  fun addYouTubePlayerListener(youTubePlayerListener: YouTubePlayerListener) = legacyTubePlayerView.webViewYouTubePlayer.addListener(youTubePlayerListener)

  fun removeYouTubePlayerListener(youTubePlayerListener: YouTubePlayerListener) = legacyTubePlayerView.webViewYouTubePlayer.removeListener(youTubePlayerListener)

  fun addFullscreenListener(fullscreenListener: FullscreenListener) = fullscreenListeners.add(fullscreenListener)

  fun removeFullscreenListener(fullscreenListener: FullscreenListener) = fullscreenListeners.remove(fullscreenListener)

  /**
   * Convenience method to set the [YouTubePlayerView] width and height to match parent.
   */
  fun matchParent() {
    setLayoutParams(
      targetWidth = ViewGroup.LayoutParams.MATCH_PARENT,
      targetHeight = ViewGroup.LayoutParams.MATCH_PARENT
    )
  }

  /**
   * Convenience method to set the [YouTubePlayerView] width to match parent and
   * height to wrap content.
   */
  fun wrapContent() {
    setLayoutParams(
      targetWidth = ViewGroup.LayoutParams.MATCH_PARENT,
      targetHeight = ViewGroup.LayoutParams.WRAP_CONTENT
    )
  }

  @Suppress("SameParameterValue")
  private fun setLayoutParams(targetWidth: Int, targetHeight: Int) {
    layoutParams = layoutParams.apply {
      width = targetWidth
      height = targetHeight
    }
  }
}
