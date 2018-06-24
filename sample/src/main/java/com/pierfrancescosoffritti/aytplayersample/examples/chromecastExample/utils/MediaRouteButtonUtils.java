package com.pierfrancescosoffritti.aytplayersample.examples.chromecastExample.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.MediaRouteButton;
import android.support.v7.view.ContextThemeWrapper;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.pierfrancescosoffritti.aytplayersample.R;
import com.pierfrancescosoffritti.aytplayersample.examples.chromecastExample.MediaRouteButtonContainer;

import javax.annotation.Nullable;

public class MediaRouteButtonUtils {
    static public MediaRouteButton initMediaRouteButton(Context context) {
        MediaRouteButton mediaRouteButton = new MediaRouteButton(context);
        CastButtonFactory.setUpMediaRouteButton(context, mediaRouteButton);

        return mediaRouteButton;
    }

    static public void addMediaRouteButtonToPlayerUI(
            MediaRouteButton mediaRouteButton, int tintColor,
            @Nullable MediaRouteButtonContainer disabledContainer, MediaRouteButtonContainer activatedContainer) {

        setMediaRouterButtonTint(mediaRouteButton, tintColor);

        if(disabledContainer != null)
        disabledContainer.removeMediaRouteButton(mediaRouteButton);

        if(mediaRouteButton.getParent() != null)
            return;

        activatedContainer.addMediaRouteButton(mediaRouteButton);
    }

    static private void setMediaRouterButtonTint(MediaRouteButton mediaRouterButton, int color) {
        ContextThemeWrapper castContext = new ContextThemeWrapper(mediaRouterButton.getContext(), R.style.Theme_MediaRouter);
        TypedArray styledAttributes = castContext.obtainStyledAttributes(null, android.support.v7.mediarouter.R.styleable.MediaRouteButton, android.support.v7.mediarouter.R.attr.mediaRouteButtonStyle, 0);
        Drawable drawable = styledAttributes.getDrawable(android.support.v7.mediarouter.R.styleable.MediaRouteButton_externalRouteEnabledDrawable);

        styledAttributes.recycle();
        DrawableCompat.setTint(drawable, ContextCompat.getColor(mediaRouterButton.getContext(), color));

        mediaRouterButton.setRemoteIndicatorDrawable(drawable);
    }
}
