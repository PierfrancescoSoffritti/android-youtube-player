package com.pierfrancescosoffritti.youtubeplayer;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashSet;
import java.util.Set;

class FullScreenHandler {

    private boolean isFullScreen;
    private final Set<YouTubePlayerFullScreenListener> fullScreenListeners;

    FullScreenHandler() {
        isFullScreen = false;
        fullScreenListeners = new HashSet<>();
    }

    void enterFullScreen(@NonNull View view) {
        if(isFullScreen)
            return;

        ViewGroup.LayoutParams viewParams = view.getLayoutParams();
        viewParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        viewParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        view.setLayoutParams(viewParams);

        isFullScreen = true;

        for(YouTubePlayerFullScreenListener fullScreenListener : fullScreenListeners)
            fullScreenListener.onYouTubePlayerEnterFullScreen();
    }

    void exitFullScreen(@NonNull View view) {
        if(!isFullScreen)
            return;

        ViewGroup.LayoutParams viewParams = view.getLayoutParams();
        viewParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        viewParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        view.setLayoutParams(viewParams);

        isFullScreen = false;

        for(YouTubePlayerFullScreenListener fullScreenListener : fullScreenListeners)
            fullScreenListener.onYouTubePlayerExitFullScreen();
    }

    void toggleFullScreen(@NonNull View view) {
        if(isFullScreen)
            exitFullScreen(view);
        else
            enterFullScreen(view);
    }

    boolean isFullScreen() {
        return isFullScreen;
    }

    boolean addFullScreenListener(@NonNull YouTubePlayerFullScreenListener fullScreenListener) {
        return fullScreenListeners.add(fullScreenListener);
    }

    boolean removeFullScreenListener(@NonNull YouTubePlayerFullScreenListener fullScreenListener) {
        return fullScreenListeners.remove(fullScreenListener);
    }
}
