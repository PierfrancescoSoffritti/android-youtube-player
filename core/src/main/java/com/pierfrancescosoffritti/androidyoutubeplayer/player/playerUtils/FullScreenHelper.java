package com.pierfrancescosoffritti.androidyoutubeplayer.player.playerUtils;

import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerFullScreenListener;

import java.util.HashSet;
import java.util.Set;

public class FullScreenHelper {

    private boolean isFullScreen;
    private final Set<YouTubePlayerFullScreenListener> fullScreenListeners;

    public FullScreenHelper() {
        isFullScreen = false;
        fullScreenListeners = new HashSet<>();
    }

    public void enterFullScreen(@NonNull View view) {
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

    public void exitFullScreen(@NonNull View view) {
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

    public void toggleFullScreen(@NonNull View view) {
        if(isFullScreen)
            exitFullScreen(view);
        else
            enterFullScreen(view);
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public boolean addFullScreenListener(@NonNull YouTubePlayerFullScreenListener fullScreenListener) {
        return fullScreenListeners.add(fullScreenListener);
    }

    public boolean removeFullScreenListener(@NonNull YouTubePlayerFullScreenListener fullScreenListener) {
        return fullScreenListeners.remove(fullScreenListener);
    }
}
