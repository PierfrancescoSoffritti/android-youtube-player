package com.pierfrancescosoffritti.cyplayersample.examples.localPlayerExample

import androidx.mediarouter.app.MediaRouteButton

interface MediaRouteButtonContainer {
    fun addMediaRouteButton(mediaRouteButton: MediaRouteButton)
    fun removeMediaRouteButton(mediaRouteButton: MediaRouteButton)
}