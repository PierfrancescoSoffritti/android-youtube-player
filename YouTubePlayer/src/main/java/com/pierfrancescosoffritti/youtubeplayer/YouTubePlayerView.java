package com.pierfrancescosoffritti.youtubeplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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
        PlayerControlsWrapper playerControlsLogic = new PlayerControlsWrapper(this, playerControls);

        fullScreenListeners = new HashSet<>();
        fullScreenListeners.add(playerControlsLogic);

        youTubePlayer.addListener(playerControlsLogic);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        adjustControlsSize();

        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * Adjust the size of the controls view so that it fits the player
     */
    private void adjustControlsSize() {
        ViewGroup.LayoutParams params = playerControls.getLayoutParams();
        params.height = youTubePlayer.getLayoutParams().height;
        params.width = youTubePlayer.getLayoutParams().width;
        playerControls.setLayoutParams(params);
    }

    public void enterFullScreen() {
        if(isFullScreen)
            return;

        ViewGroup.LayoutParams viewParams = getLayoutParams();
        viewParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        viewParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        setLayoutParams(viewParams);

        youTubePlayer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        adjustControlsSize();

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
        adjustControlsSize();

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

    // calls to YouTubePlayer

    private boolean initialized = false;

    public void initialize(YouTubePlayer.YouTubeListener youTubeListener) {
        youTubePlayer.initialize(youTubeListener);

        initialized = true;
    }

    /**
     * See {@link YouTubePlayer#loadVideo(String, float)}
     */
    public void loadVideo(String videoId, int startSecond) {
        if(!initialized)
            throw new IllegalStateException("the player has not been initialized");

        youTubePlayer.loadVideo(videoId, startSecond);
    }

    /**
     * See {@link YouTubePlayer#cueVideo(String, float)}
     */
    public void cueVideo(String videoId, int startSeconds) {
        if(!initialized)
            throw new IllegalStateException("the player has not been initialized");

        youTubePlayer.cueVideo(videoId, startSeconds);
    }

    /**
     * Calls {@link WebView#destroy()} on the player.
     * Call this method before destroying the host Fragment/Activity
     */
    public void release() {
        if(!initialized)
            throw new IllegalStateException("the player has not been initialized");

        youTubePlayer.destroy();
    }

    /**
     * See {@link YouTubePlayer#seekTo(int)}
     */
    public void seekTo(int time) {
        if(!initialized)
            throw new IllegalStateException("the player has not been initialized");

        youTubePlayer.seekTo(time);
    }

    /**
     * See {@link YouTubePlayer#play()}
     */
    public void playVideo() {
        if(!initialized)
            throw new IllegalStateException("the player has not been initialized");

        youTubePlayer.play();
    }

    /**
     * See {@link YouTubePlayer#pause()}
     */
    public void pauseVideo() {
        if(!initialized)
            throw new IllegalStateException("the player has not been initialized");

        youTubePlayer.pause();
    }
}
