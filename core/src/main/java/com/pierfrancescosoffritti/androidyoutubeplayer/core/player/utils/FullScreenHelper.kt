package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils

import android.view.View
import android.view.ViewGroup

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener

import java.util.HashSet

internal class FullScreenHelper(private val targetView: View) {

    var isFullScreen: Boolean = false
        private set

    private val fullScreenListeners: MutableSet<YouTubePlayerFullScreenListener> = HashSet()

    fun enterFullScreen() {
        if (isFullScreen) return

        isFullScreen = true

        val viewParams = targetView.layoutParams
        viewParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        viewParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        targetView.layoutParams = viewParams

        for (fullScreenListener in fullScreenListeners)
            fullScreenListener.onYouTubePlayerEnterFullScreen()
    }

    fun exitFullScreen() {
        if (!isFullScreen) return

        isFullScreen = false

        val viewParams = targetView.layoutParams
        viewParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        viewParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        targetView.layoutParams = viewParams

        for (fullScreenListener in fullScreenListeners)
            fullScreenListener.onYouTubePlayerExitFullScreen()
    }

    fun toggleFullScreen() {
        if (isFullScreen) exitFullScreen()
        else enterFullScreen()
    }

    fun addFullScreenListener(fullScreenListener: YouTubePlayerFullScreenListener): Boolean {
        return fullScreenListeners.add(fullScreenListener)
    }

    fun removeFullScreenListener(fullScreenListener: YouTubePlayerFullScreenListener): Boolean {
        return fullScreenListeners.remove(fullScreenListener)
    }
}
