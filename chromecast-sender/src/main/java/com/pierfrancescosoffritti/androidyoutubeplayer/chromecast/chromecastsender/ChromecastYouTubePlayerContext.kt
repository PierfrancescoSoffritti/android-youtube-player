package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender

import com.google.android.gms.cast.framework.SessionManager
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.infrastructure.ChromecastConnectionListener
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.infrastructure.ChromecastManager
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener

class ChromecastYouTubePlayerContext(sessionManager: SessionManager, vararg chromecastConnectionListeners: ChromecastConnectionListener) : ChromecastConnectionListener {
    private val chromecastConnectionListeners = HashSet<ChromecastConnectionListener>()

    private val chromecastManager = ChromecastManager(this, sessionManager, this.chromecastConnectionListeners)
    private val chromecastYouTubePlayer = ChromecastYouTubePlayer(chromecastManager.chromecastCommunicationChannel)

    private var chromecastConnected = false

    init {
        chromecastConnectionListeners.forEach { this.chromecastConnectionListeners.add(it) }
        onCreate()
    }

    // does this really have to be called when the activity is created?
//    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() {
        chromecastManager.restoreSession()
        chromecastManager.addSessionManagerListener()
    }

//    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
//    private fun onResume() = chromecastManager.addSessionManagerListener()

    // if this is enabled the library can't know when a session is terminated (if the app is in the background), therefore it can't remove notifications etc.
//    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
//    private fun onPause() = chromecastManager.removeSessionManagerListener()

    fun initialize(youTubePlayerListener: YouTubePlayerListener) {
        if(!chromecastConnected)
            throw RuntimeException("ChromecastYouTubePlayerContext, can't initialize before Chromecast connection is established.")

        chromecastYouTubePlayer.initialize { youTubePlayer -> youTubePlayer.addListener(youTubePlayerListener) }
    }

    fun release() {
        endCurrentSession()
        chromecastManager.release()
        chromecastConnectionListeners.clear()
    }

    fun endCurrentSession() {
        chromecastManager.endCurrentSession()
    }

    override fun onChromecastConnecting() {
    }

    override fun onChromecastConnected(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {
        chromecastConnected = true
    }

    override fun onChromecastDisconnected() {
        chromecastConnected = false
    }

    fun addChromecastConnectionListener(chromecastConnectionListener: ChromecastConnectionListener) = chromecastConnectionListeners.add(chromecastConnectionListener)
    fun removeChromecastConnectionListener(chromecastConnectionListener: ChromecastConnectionListener) = chromecastConnectionListeners.remove(chromecastConnectionListener)
}