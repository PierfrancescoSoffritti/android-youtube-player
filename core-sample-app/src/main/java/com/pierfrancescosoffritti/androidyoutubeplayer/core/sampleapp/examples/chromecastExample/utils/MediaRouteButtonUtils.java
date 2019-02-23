package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.chromecastExample.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.chromecastExample.ChromeCastExampleActivity;
import com.pierfrancescosoffritti.aytplayersample.R;

import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.mediarouter.app.MediaRouteButton;

public class MediaRouteButtonUtils {
    static public MediaRouteButton initMediaRouteButton(Context context) {
        MediaRouteButton mediaRouteButton = new MediaRouteButton(context);
        CastButtonFactory.setUpMediaRouteButton(context, mediaRouteButton);

        return mediaRouteButton;
    }

    static public void addMediaRouteButtonToPlayerUi(
            MediaRouteButton mediaRouteButton, int tintColor,
            @Nullable ChromeCastExampleActivity.MediaRouteButtonContainer disabledContainer, ChromeCastExampleActivity.MediaRouteButtonContainer activatedContainer) {

        setMediaRouterButtonTint(mediaRouteButton, tintColor);

        if(disabledContainer != null)
        disabledContainer.removeMediaRouteButton(mediaRouteButton);

        if(mediaRouteButton.getParent() != null)
            return;

        activatedContainer.addMediaRouteButton(mediaRouteButton);
    }

    static private void setMediaRouterButtonTint(MediaRouteButton mediaRouterButton, int color) {
        ContextThemeWrapper castContext = new ContextThemeWrapper(mediaRouterButton.getContext(), R.style.Theme_MediaRouter);
        TypedArray styledAttributes = castContext.obtainStyledAttributes(null, androidx.mediarouter.R.styleable.MediaRouteButton, androidx.mediarouter.R.attr.mediaRouteButtonStyle, 0);
        Drawable drawable = styledAttributes.getDrawable(androidx.mediarouter.R.styleable.MediaRouteButton_externalRouteEnabledDrawable);

        if(drawable == null) {
            Log.e("MediaRouteButtonUtils", "can't apply tint to MediaRouteButton");
            return;
        }

        styledAttributes.recycle();
        DrawableCompat.setTint(drawable, ContextCompat.getColor(mediaRouterButton.getContext(), color));

        mediaRouterButton.setRemoteIndicatorDrawable(drawable);
    }
}
