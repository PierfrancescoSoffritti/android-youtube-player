package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.annotation.VisibleForTesting
import com.pierfrancescosoffritti.androidyoutubeplayer.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayerBridge
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.toFloat
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*


internal object FakeWebViewYouTubeListener : FullScreenListener {
  override fun onEnterFullScreen(fullScreenView: View, exitFullScreen: () -> Unit) {}
  override fun onExitFullScreen() {}
}

/**
 * WebView implementation of [YouTubePlayer]. The player runs inside the WebView, using the IFrame Player API.
 */
internal class WebViewYouTubePlayer constructor(
  context: Context,
  private val listener: FullScreenListener,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr), YouTubePlayer, YouTubePlayerBridge.YouTubePlayerBridgeCallbacks {

  /** Constructor used by tools */
  constructor(context: Context) : this(context, FakeWebViewYouTubeListener)

  private lateinit var youTubePlayerInitListener: (YouTubePlayer) -> Unit

  private val youTubePlayerListeners = mutableSetOf<YouTubePlayerListener>()
  private val mainThreadHandler: Handler = Handler(Looper.getMainLooper())

  internal var isBackgroundPlaybackEnabled = false

  internal fun initialize(initListener: (YouTubePlayer) -> Unit, playerOptions: IFramePlayerOptions?) {
    youTubePlayerInitListener = initListener
    initWebView(playerOptions ?: IFramePlayerOptions.default)
  }

  override fun onYouTubeIFrameAPIReady() = youTubePlayerInitListener(this)

  override fun getInstance(): YouTubePlayer = this

  override fun loadVideo(videoId: String, startSeconds: Float) {
    mainThreadHandler.post { loadUrl("javascript:loadVideo('$videoId', $startSeconds)") }
  }

  override fun cueVideo(videoId: String, startSeconds: Float) {
    mainThreadHandler.post { loadUrl("javascript:cueVideo('$videoId', $startSeconds)") }
  }

  override fun play() {
    mainThreadHandler.post { loadUrl("javascript:playVideo()") }
  }

  override fun pause() {
    mainThreadHandler.post { loadUrl("javascript:pauseVideo()") }
  }

  override fun mute() {
    mainThreadHandler.post { loadUrl("javascript:mute()") }
  }

  override fun unMute() {
    mainThreadHandler.post { loadUrl("javascript:unMute()") }
  }

  override fun setVolume(volumePercent: Int) {
    require(volumePercent in 0..100) { "Volume must be between 0 and 100" }
    mainThreadHandler.post { loadUrl("javascript:setVolume($volumePercent)") }
  }

  override fun seekTo(time: Float) {
    mainThreadHandler.post { loadUrl("javascript:seekTo($time)") }
  }

  override fun setPlaybackRate(playbackRate: PlayerConstants.PlaybackRate) {
    mainThreadHandler.post { loadUrl("javascript:setPlaybackRate(${playbackRate.toFloat()})") }
  }

  override fun toggleFullscreen() {
    loadUrl("javascript:toggleFullscreen()")
  }

  override fun destroy() {
    youTubePlayerListeners.clear()
    mainThreadHandler.removeCallbacksAndMessages(null)
    super.destroy()
  }

  // create new set to avoid concurrent modifications
  override val listeners: Collection<YouTubePlayerListener> get() = youTubePlayerListeners.toSet()

  override fun addListener(listener: YouTubePlayerListener): Boolean {
    return youTubePlayerListeners.add(listener)
  }

  override fun removeListener(listener: YouTubePlayerListener): Boolean {
    return youTubePlayerListeners.remove(listener)
  }

  @SuppressLint("SetJavaScriptEnabled")
  private fun initWebView(playerOptions: IFramePlayerOptions) {
    settings.apply {
      javaScriptEnabled = true
      mediaPlaybackRequiresUserGesture = false
      cacheMode = WebSettings.LOAD_DEFAULT
    }

    addJavascriptInterface(YouTubePlayerBridge(this), "YouTubePlayerBridge")

    val htmlPage = readHTMLFromUTF8File(resources.openRawResource(R.raw.ayp_youtube_player))
      .replace("<<injectedPlayerVars>>", playerOptions.toString())

    loadDataWithBaseURL(playerOptions.getOrigin(), htmlPage, "text/html", "utf-8", null)

    webChromeClient = object : WebChromeClient() {

      override fun onShowCustomView(view: View, callback: CustomViewCallback) {
        super.onShowCustomView(view, callback)
        listener.onEnterFullScreen(view) { callback.onCustomViewHidden() }
      }

      override fun onHideCustomView() {
        super.onHideCustomView()
        listener.onExitFullScreen()
      }

      override fun getDefaultVideoPoster(): Bitmap? {
        val result = super.getDefaultVideoPoster()
        // if the video's thumbnail is not in memory, show a black screen
        return result ?: Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
      }
    }
  }

  override fun onWindowVisibilityChanged(visibility: Int) {
    if (isBackgroundPlaybackEnabled && (visibility == View.GONE || visibility == View.INVISIBLE)) {
      return
    }

    super.onWindowVisibilityChanged(visibility)
  }
}

@VisibleForTesting
internal fun readHTMLFromUTF8File(inputStream: InputStream): String {
  inputStream.use {
    try {
      val bufferedReader = BufferedReader(InputStreamReader(inputStream, "utf-8"))
      return bufferedReader.readLines().joinToString("\n")
    } catch (e: Exception) {
      throw RuntimeException("Can't parse HTML file.")
    }
  }
}
