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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.toFloat
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

/**
 * Implementation of [YouTubePlayer] using a [WebView].
 */
private class YouTubePlayerImpl(private val webView: WebView) : YouTubePlayer {
  private val mainThread: Handler = Handler(Looper.getMainLooper())
  override val listeners = HashSet<YouTubePlayerListener>()

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
  override fun setVolume(volumePercent: Int) {
    require(volumePercent in 0..100) { "Volume must be between 0 and 100" }
    webView.invoke("setVolume", volumePercent)
  }
  override fun seekTo(time: Float) = webView.invoke("seekTo", time)
  override fun setPlaybackRate(playbackRate: PlayerConstants.PlaybackRate) = webView.invoke("setPlaybackRate", playbackRate.toFloat())
  override fun setPlaybackQuality(quality: String) {
    webView.loadUrl("javascript:player.setPlaybackQuality('$quality')")
  }
  override fun showCaption() {
    webView.loadUrl("javascript:showCaption()")
  }
  override fun hideCaption() {
    webView.loadUrl("javascript:hideCaption()")
  }
  override fun toggleCaption() {
    webView.loadUrl("javascript:toggleCaption()")
  }
  override fun toggleFullscreen() = webView.invoke("toggleFullscreen")
  override fun addListener(listener: YouTubePlayerListener): Boolean = listeners.add(listener)
  override fun removeListener(listener: YouTubePlayerListener): Boolean = listeners.remove(listener)

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
}

internal object FakeWebViewYouTubeListener : FullscreenListener {
  override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {}
  override fun onExitFullscreen() {}
}

/**
 * WebView implementation of [YouTubePlayer]. The player runs inside the WebView, using the IFrame Player API.
 */
internal class WebViewYouTubePlayer internal constructor(
    context: Context,
    private val youTubePlayerOwner: YouTubePlayerOwner,
    private val mainThreadHandler: Handler
) : WebView(context) {

    private val _youTubePlayer = YouTubePlayerImpl(this)
    internal val youtubePlayer: YouTubePlayer get() = _youTubePlayer

    private lateinit var youTubePlayerInitListener: (YouTubePlayer) -> Unit

    internal var isBackgroundPlaybackEnabled = false

    internal fun initialize(youTubePlayerInitListener: (YouTubePlayer) -> Unit, playerOptions: IFramePlayerOptions?, videoId: String?) {
        this.youTubePlayerInitListener = youTubePlayerInitListener
        initWebView(playerOptions ?: IFramePlayerOptions.default, videoId)
    }

    // create new set to avoid concurrent modifications
    override val listeners: Collection<YouTubePlayerListener> get() = _youTubePlayer.listeners
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
            domStorageEnabled = true  // Enable DOM storage for video quality control
        }

        addJavascriptInterface(YouTubePlayerBridge(this), "YouTubePlayerBridge")

        val htmlPage = readHTMLFromUTF8File(resources.openRawResource(R.raw.ayp_youtube_player))
            .replace("<<injectedVideoId>>", if (videoId != null) { "'$videoId'" } else { "undefined" })
            .replace("<<injectedPlayerVars>>", playerOptions.toString())

        loadDataWithBaseURL(playerOptions.getOrigin(), htmlPage, "text/html", "utf-8", null)

        webChromeClient = object : WebChromeClient() {

            override fun onShowCustomView(view: View, callback: CustomViewCallback) {
                super.onShowCustomView(view, callback)
                youTubePlayerOwner.onEnterFullscreen(view) { callback.onCustomViewHidden() }
            }

            override fun onHideCustomView() {
                super.onHideCustomView()
                youTubePlayerOwner.onExitFullscreen()
            }

            override fun getDefaultVideoPoster(): Bitmap? {
                val result = super.getDefaultVideoPoster()
                // if the video's thumbnail is not in memory, show a black screen
                return result ?: Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
            }
        }
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        if (visibility != View.GONE) {
            mainThreadHandler.post { youTubePlayerOwner.getInstance()?.play() }
        } else {
            mainThreadHandler.post { youTubePlayerOwner.getInstance()?.pause() }
        }

        super.onWindowVisibilityChanged(visibility)
    }

    override fun setPlaybackQuality(quality: String) {
        mainThreadHandler.post { youTubePlayer.setPlaybackQuality(quality) }
    }

    override fun showCaption() {
        mainThreadHandler.post { loadUrl("javascript:showCaption()") }
    }

    override fun hideCaption() {
        mainThreadHandler.post { loadUrl("javascript:hideCaption()") }
    }

    override fun toggleCaption() {
        mainThreadHandler.post { loadUrl("javascript:toggleCaption()") }
    }

    override fun toggleFullscreen() {
        mainThreadHandler.post { youTubePlayer.toggleFullscreen() }
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
