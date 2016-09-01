package com.pierfrancescosoffritti.youtubeplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class YouTubePlayerView extends FrameLayout {

    @NonNull private final YouTubePlayer youTubePlayer;
    @NonNull private final View playerControls;

    public YouTubePlayerView(Context context) {
        this(context, null);
    }

    public YouTubePlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YouTubePlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        youTubePlayer = new YouTubePlayer(context);
        addView(youTubePlayer, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        playerControls = inflate(context, R.layout.player_controls, this);

        new PlayerControls(playerControls);
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
        ViewGroup.LayoutParams viewParams = getLayoutParams();
        viewParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        viewParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        setLayoutParams(viewParams);

        youTubePlayer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        ViewGroup.LayoutParams params = playerControls.getLayoutParams();
        params.height = youTubePlayer.getLayoutParams().height;
        params.width = youTubePlayer.getLayoutParams().width;
        playerControls.setLayoutParams(params);

    }

    public void exitFullScreen() {
        ViewGroup.LayoutParams viewParams = getLayoutParams();
        viewParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        viewParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        setLayoutParams(viewParams);

        youTubePlayer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ViewGroup.LayoutParams params = playerControls.getLayoutParams();
        params.height = youTubePlayer.getLayoutParams().height;
        params.width = youTubePlayer.getLayoutParams().width;
        playerControls.setLayoutParams(params);
    }
}
