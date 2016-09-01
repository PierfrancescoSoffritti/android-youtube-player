package com.pierfrancescosoffritti.youtubeplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.HashSet;
import java.util.Set;

public class YouTubePlayerView extends FrameLayout {

    @NonNull private final YouTubePlayer youTubePlayer;
    @NonNull private final View playerControls;

    private final Set<YouTubePlayerFullScreenListener> fullScreenListeners;

    private boolean isFullScreen;

    public YouTubePlayerView(Context context) {
        this(context, null);
    }

    public YouTubePlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YouTubePlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        isFullScreen = false;

        youTubePlayer = new YouTubePlayer(context);
        addView(youTubePlayer, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        playerControls = inflate(context, R.layout.player_controls, this);
        PlayerControls playerControlsLogic = new PlayerControls(this, playerControls);

        fullScreenListeners = new HashSet<>();
        fullScreenListeners.add(playerControlsLogic);

        youTubePlayer.addListener(playerControlsLogic);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        ViewGroup.LayoutParams params = playerControls.getLayoutParams();
        params.height = youTubePlayer.getLayoutParams().height;
        params.width = youTubePlayer.getLayoutParams().width;
        playerControls.setLayoutParams(params);

        super.onLayout(changed, left, top, right, bottom);
    }

    public void initialize(YouTubePlayer.YouTubeListener youTubeListener) {
        youTubePlayer.initialize(youTubeListener);
    }

    public void loadVideo(String videoId, int startSecond) {
        youTubePlayer.loadVideo(videoId, startSecond);
    }

    public void release() {
        youTubePlayer.destroy();
    }

    public void enterFullScreen() {
        if(isFullScreen)
            return;

        ViewGroup.LayoutParams viewParams = getLayoutParams();
        viewParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        viewParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        setLayoutParams(viewParams);

        youTubePlayer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        ViewGroup.LayoutParams params = playerControls.getLayoutParams();
        params.height = youTubePlayer.getLayoutParams().height;
        params.width = youTubePlayer.getLayoutParams().width;
        playerControls.setLayoutParams(params);

        isFullScreen = true;

        for(YouTubePlayerFullScreenListener fullScreenListener : fullScreenListeners)
            fullScreenListener.onYouTubePlayerEnterFullScreen();
    }

    public void exitFullScreen() {
        if(!isFullScreen)
            return;

        ViewGroup.LayoutParams viewParams = getLayoutParams();
        viewParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        viewParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        setLayoutParams(viewParams);

        youTubePlayer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ViewGroup.LayoutParams params = playerControls.getLayoutParams();
        params.height = youTubePlayer.getLayoutParams().height;
        params.width = youTubePlayer.getLayoutParams().width;
        playerControls.setLayoutParams(params);

        isFullScreen = false;

        for(YouTubePlayerFullScreenListener fullScreenListener : fullScreenListeners)
            fullScreenListener.onYouTubePlayerExitFullScreen();
    }

    public void toggleFullScreen() {
        if(isFullScreen)
            exitFullScreen();
        else
            enterFullScreen();

    }

    public boolean addFullScreenListener(@NonNull YouTubePlayerFullScreenListener fullScreenListener) {
        return fullScreenListeners.add(fullScreenListener);
    }

    public boolean removeFullScreenListener(@NonNull YouTubePlayerFullScreenListener fullScreenListener) {
        return fullScreenListeners.remove(fullScreenListener);
    }

    public void seekTo(int time) {
        youTubePlayer.seekTo(time);
    }

    public void playVideo() {
        youTubePlayer.play();
    }

    public void pauseVideo() {
        youTubePlayer.pause();
    }
}
