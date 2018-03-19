package com.pierfrancescosoffritti.youtubeplayer.ui;

import android.view.View;

import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerListener;

public abstract class AbstractPlayerUIController implements PlayerUIController, YouTubePlayerFullScreenListener, YouTubePlayerListener {
    public abstract void onControlsViewInflated(View controlsView);
}
