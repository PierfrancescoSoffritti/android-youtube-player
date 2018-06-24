package com.pierfrancescosoffritti.cyplayersample.utils

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.MediaRouteButton
import android.support.v7.mediarouter.R
import android.view.ContextThemeWrapper
import com.google.android.gms.cast.framework.CastButtonFactory
import com.pierfrancescosoffritti.cyplayersample.examples.localPlayerExample.MediaRouteButtonContainer

object MediaRouteButtonUtils {

    fun initMediaRouteButton(context: Context) : MediaRouteButton {
        val mediaRouteButton = MediaRouteButton(context)
        CastButtonFactory.setUpMediaRouteButton(context, mediaRouteButton)

        return mediaRouteButton
    }

    fun initMediaRouteButton(mediaRouteButton: MediaRouteButton) : MediaRouteButton {
        CastButtonFactory.setUpMediaRouteButton(mediaRouteButton.context, mediaRouteButton)
        return mediaRouteButton
    }

    fun addMediaRouteButtonToPlayerUI(
            mediaRouteButton: MediaRouteButton, tintColor: Int,
            disabledContainer: MediaRouteButtonContainer?, activatedContainer: MediaRouteButtonContainer) {

        setMediaRouterButtonTint(mediaRouteButton, tintColor)

        disabledContainer?.removeMediaRouteButton(mediaRouteButton)
        if(mediaRouteButton.parent != null) return
        activatedContainer.addMediaRouteButton(mediaRouteButton)
    }

    private fun setMediaRouterButtonTint(mediaRouterButton: MediaRouteButton, color: Int) {
        val castContext = ContextThemeWrapper(mediaRouterButton.context, R.style.Theme_MediaRouter)
        val styledAttributes = castContext.obtainStyledAttributes(null, android.support.v7.mediarouter.R.styleable.MediaRouteButton, android.support.v7.mediarouter.R.attr.mediaRouteButtonStyle, 0)
        val drawable = styledAttributes.getDrawable(android.support.v7.mediarouter.R.styleable.MediaRouteButton_externalRouteEnabledDrawable)

        styledAttributes.recycle()
        DrawableCompat.setTint(drawable, ContextCompat.getColor(mediaRouterButton.context, color))

        mediaRouterButton.setRemoteIndicatorDrawable(drawable)
    }
}