package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.annotation.VisibleForTesting
import com.pierfrancescosoffritti.androidyoutubeplayer.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayerBridge
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayerStateProvider
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.toFloat
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


private class YouTubePlayerImpl(private val webView: WebView) : YouTubePlayer, YouTubePlayerStateProvider {
  private val mainThread: Handler = Handler(Looper.getMainLooper())
  val listeners = mutableSetOf<YouTubePlayerListener>()

  override fun loadVideo(videoId: String, startSeconds: Float) = webView.invoke("loadVideo", videoId, startSeconds)
  override fun cueVideo(videoId: String, startSeconds: Float) = webView.invoke("cueVideo", videoId, startSeconds)
  override fun play() = webView.invoke("playVideo")
  override fun pause() = webView.invoke("pauseVideo")
  override fun nextVideo() = webView.invoke("nextVideo")
  override fun previousVideo() = webView.invoke("previousVideo")
  override fun playVideoAt(index: Int) = webView.invoke("playVideoAt", index)
  override fun setLoop(loop: Boolean) = webView.invoke("setLoop", loop)
  override fun setShuffle(shuffle: Boolean) = webView.invoke("setShuffle", shuffle)
  override fun mute() = webView.invoke("mute")
  override fun unMute() = webView.invoke("unMute")
  override suspend fun isMuted(): Boolean = suspendCoroutine { continuation ->
    webView.evaluateWithResult("player.isMuted()") { result ->
      val isMuted = result.equals("true", ignoreCase = true)
      continuation.resume(isMuted)
    }
  }
  override fun isMuted(callback: (Boolean) -> Unit) {
    webView.evaluateWithResult("player.isMuted()") { result ->
      val isMuted = result.equals("true", ignoreCase = true)
      mainThread.post { callback(isMuted) }
    }
  }
  override fun setVolume(volumePercent: Int) {
    require(volumePercent in 0..100) { "Volume must be between 0 and 100" }
    webView.invoke("setVolume", volumePercent)
  }
  override fun seekTo(time: Float) = webView.invoke("seekTo", time)
  override fun setPlaybackRate(playbackRate: PlayerConstants.PlaybackRate) = webView.invoke("setPlaybackRate", playbackRate.toFloat())
  override fun toggleFullscreen() = webView.invoke("toggleFullscreen")
  override fun addListener(listener: YouTubePlayerListener) = listeners.add(listener)
  override fun removeListener(listener: YouTubePlayerListener) = listeners.remove(listener)

  fun release() {
    listeners.clear()
    mainThread.removeCallbacksAndMessages(null)
  }

  private fun WebView.invoke(function: String, vararg args: Any) {
    val stringArgs = args.map {
      if (it is String) {
        "'$it'"
      }
      else {
        it.toString()
      }
    }
    mainThread.post { loadUrl("javascript:$function(${stringArgs.joinToString(",")})") }
  }

  /**
   * Executes JavaScript and gets the return value via a callback.
   *
   * @param javascript The JavaScript code to execute
   * @param callback Function that will be called with the result
   */
  private fun WebView.evaluateWithResult(javascript: String, callback: (String) -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      // For API 19+, use evaluateJavascript directly
      evaluateJavascript(javascript) { result ->
        callback(result)
      }
    } else {
      // For API < 19, use a temporary JavaScript interface
      val interfaceName = "JSCallback_${System.currentTimeMillis()}"
      val jsInterface = object : Any() {
        @JavascriptInterface
        fun onResult(result: String) {
          mainThread.post {
            callback(result)
            // Remove the interface after getting result
            removeJavascriptInterface(interfaceName)
          }
        }
      }

      addJavascriptInterface(jsInterface, interfaceName)

      // Wrap the JavaScript in a function that calls our interface with the result
      val jsToExecute = """
            javascript:(function() {
                var result = (function() { return $javascript; })();
                window.$interfaceName.onResult(result !== undefined ? result.toString() : "null");
            })()
        """.trimIndent()

      mainThread.post { loadUrl(jsToExecute) }
    }
  }
}

internal object FakeWebViewYouTubeListener : FullscreenListener {
  override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {}
  override fun onExitFullscreen() {}
}

/**
 * WebView implementation of [YouTubePlayer]. The player runs inside the WebView, using the IFrame Player API.
 */
internal class WebViewYouTubePlayer constructor(
  context: Context,
  private val listener: FullscreenListener,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr), YouTubePlayerBridge.YouTubePlayerBridgeCallbacks {

  /** Constructor used by tools */
  constructor(context: Context) : this(context, FakeWebViewYouTubeListener)

  private val _youTubePlayer = YouTubePlayerImpl(this)
  internal val youtubePlayer: YouTubePlayer get() = _youTubePlayer

  private lateinit var youTubePlayerInitListener: (YouTubePlayer) -> Unit

  internal var isBackgroundPlaybackEnabled = false

  internal fun initialize(initListener: (YouTubePlayer) -> Unit, playerOptions: IFramePlayerOptions?, videoId: String?) {
    youTubePlayerInitListener = initListener
    initWebView(playerOptions ?: IFramePlayerOptions.default, videoId)
  }

  // create new set to avoid concurrent modifications
  override val listeners: Collection<YouTubePlayerListener> get() = _youTubePlayer.listeners.toSet()
  override fun getInstance(): YouTubePlayer = _youTubePlayer
  override fun onYouTubeIFrameAPIReady() = youTubePlayerInitListener(_youTubePlayer)
  fun addListener(listener: YouTubePlayerListener) = _youTubePlayer.listeners.add(listener)
  fun removeListener(listener: YouTubePlayerListener) = _youTubePlayer.listeners.remove(listener)

  override fun destroy() {
    _youTubePlayer.release()
    super.destroy()
  }

  @SuppressLint("SetJavaScriptEnabled")
  private fun initWebView(playerOptions: IFramePlayerOptions, videoId: String?) {
    settings.apply {
      javaScriptEnabled = true
      mediaPlaybackRequiresUserGesture = false
      cacheMode = WebSettings.LOAD_DEFAULT
    }

    addJavascriptInterface(YouTubePlayerBridge(this), "YouTubePlayerBridge")

    val htmlPage = readHTMLFromUTF8File(resources.openRawResource(R.raw.ayp_youtube_player))
      .replace("<<injectedVideoId>>", if (videoId != null) { "'$videoId'" } else { "undefined" })
      .replace("<<injectedPlayerVars>>", playerOptions.toString())

    loadDataWithBaseURL(playerOptions.getOrigin(), htmlPage, "text/html", "utf-8", null)

    webChromeClient = object : WebChromeClient() {

      override fun onShowCustomView(view: View, callback: CustomViewCallback) {
        super.onShowCustomView(view, callback)
        listener.onEnterFullscreen(view) { callback.onCustomViewHidden() }
      }

      override fun onHideCustomView() {
        super.onHideCustomView()
        listener.onExitFullscreen()
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
