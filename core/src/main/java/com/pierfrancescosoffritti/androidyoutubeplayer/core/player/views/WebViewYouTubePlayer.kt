package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.view.View
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayerBridge
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.R
import java.io.BufferedReader
import java.io.InputStreamReader

interface YouTubePlayerOwner {
    fun onEnterFullscreen(view: View, callback: () -> Unit)
    fun onExitFullscreen()
    fun getInstance(): YouTubePlayer?
}

private class YouTubePlayerImpl(private val webView: WebView) : YouTubePlayer {
    private val mainThread = Handler(Looper.getMainLooper())
    override val listeners = HashSet<YouTubePlayerListener>()

    override fun loadVideo(videoId: String, startSeconds: Float) {
        webView.loadUrl("javascript:player.loadVideo('$videoId', $startSeconds)")
    }

    override fun cueVideo(videoId: String, startSeconds: Float) {
        webView.loadUrl("javascript:player.cueVideo('$videoId', $startSeconds)")
    }

    override fun play() {
        webView.loadUrl("javascript:player.playVideo()")
    }

    override fun pause() {
        webView.loadUrl("javascript:player.pauseVideo()")
    }

    override fun setPlaybackRate(playbackRate: PlayerConstants.PlaybackRate) {
        webView.loadUrl("javascript:player.setPlaybackRate(${playbackRate.value})")
    }

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

    override fun seekTo(time: Float) {
        webView.loadUrl("javascript:player.seekTo($time)")
    }

    override fun toggleFullscreen() { }

    override fun addListener(listener: YouTubePlayerListener): Boolean = listeners.add(listener)
    override fun removeListener(listener: YouTubePlayerListener): Boolean = listeners.remove(listener)

    fun release() {
        listeners.clear()
    }
}

internal class WebViewYouTubePlayer internal constructor(
    context: Context,
    private val listener: YouTubePlayerOwner
) : WebView(context), YouTubePlayerBridge.YouTubePlayerBridgeCallbacks {

    private val _youTubePlayer = YouTubePlayerImpl(this)
    internal val youtubePlayer: YouTubePlayer get() = _youTubePlayer

    private lateinit var youTubePlayerInitListener: (YouTubePlayer) -> Unit

    internal var isBackgroundPlaybackEnabled = false

    internal fun initialize(youTubePlayerInitListener: (YouTubePlayer) -> Unit, playerOptions: IFramePlayerOptions?, videoId: String?) {
        this.youTubePlayerInitListener = youTubePlayerInitListener
        initWebView(playerOptions ?: IFramePlayerOptions.default, videoId)
    }

    override val listeners: Collection<YouTubePlayerListener> get() = _youTubePlayer.listeners
    override fun getInstance(): YouTubePlayer = _youTubePlayer
    override fun onYouTubeIFrameAPIReady() = youTubePlayerInitListener(_youTubePlayer)

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
            domStorageEnabled = true
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
                return result ?: Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
            }
        }
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        if (visibility != View.GONE) {
            _youTubePlayer.play()
        } else {
            _youTubePlayer.pause()
        }
        super.onWindowVisibilityChanged(visibility)
    }
}

@VisibleForTesting
internal fun readHTMLFromUTF8File(inputStream: java.io.InputStream): String {
    inputStream.use {
        try {
            val bufferedReader = BufferedReader(InputStreamReader(inputStream, "utf-8"))
            return bufferedReader.readLines().joinToString("\n")
        } catch (e: Exception) {
            throw RuntimeException("Can't parse HTML file.")
        }
    }
}
