package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.infrastructure

import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.ChromecastYouTubePlayerContext

/**
 * Implement this interface to be notified about changes in the cast connection.
 */
interface ChromecastConnectionListener {
    fun onChromecastConnecting()
    fun onChromecastConnected(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext)
    fun onChromecastDisconnected()
}