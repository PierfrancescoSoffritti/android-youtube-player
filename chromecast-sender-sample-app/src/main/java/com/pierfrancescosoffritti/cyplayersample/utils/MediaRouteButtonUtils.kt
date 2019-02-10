package com.pierfrancescosoffritti.cyplayersample.utils

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.mediarouter.app.MediaRouteButton
import androidx.mediarouter.R
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

    fun addMediaRouteButtonToPlayerUi(
            mediaRouteButton: MediaRouteButton, tintColor: Int,
            disabledContainer: MediaRouteButtonContainer?, activatedContainer: MediaRouteButtonContainer) {

        setMediaRouterButtonTint(mediaRouteButton, tintColor)

        disabledContainer?.removeMediaRouteButton(mediaRouteButton)
        if(mediaRouteButton.parent != null) return
        activatedContainer.addMediaRouteButton(mediaRouteButton)
    }

    private fun setMediaRouterButtonTint(mediaRouterButton: MediaRouteButton, color: Int) {
        val castContext = ContextThemeWrapper(mediaRouterButton.context, R.style.Theme_MediaRouter)
        val styledAttributes = castContext.obtainStyledAttributes(null, R.styleable.MediaRouteButton, R.attr.mediaRouteButtonStyle, 0)
        val drawable = styledAttributes.getDrawable(R.styleable.MediaRouteButton_externalRouteEnabledDrawable)

        styledAttributes.recycle()
        DrawableCompat.setTint(drawable, ContextCompat.getColor(mediaRouterButton.context, color))

        mediaRouterButton.setRemoteIndicatorDrawable(drawable)
    }
}