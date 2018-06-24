package com.pierfrancescosoffritti.aytplayersample.examples.customUIExample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.aytplayersample.R;

class CustomPlayerUIController extends AbstractYouTubePlayerListener implements YouTubePlayerFullScreenListener {

    private final View playerUI;

    private Context context;
    private YouTubePlayer youTubePlayer;
    private YouTubePlayerView youTubePlayerView;

    // panel is used to intercept clicks on the WebView, I don't want the user to be able to click the WebView directly.
    private View panel;
    private View progressbar;
    private TextView videoCurrentTimeTextView;
    private TextView videoDurationTextView;
    private Button playPauseButton;
    private Button enterExitFullscreenButton;

    private boolean playing = true;
    private boolean fullscreen = false;

    CustomPlayerUIController(Context context, View customPlayerUI, YouTubePlayer youTubePlayer, YouTubePlayerView youTubePlayerView) {
        this.playerUI = customPlayerUI;
        this.context = context;
        this.youTubePlayer = youTubePlayer;
        this.youTubePlayerView = youTubePlayerView;

        initViews(customPlayerUI);
    }

    private void initViews(View playerUI) {
        panel = playerUI.findViewById(R.id.panel);
        progressbar = playerUI.findViewById(R.id.progressbar);
        videoCurrentTimeTextView = playerUI.findViewById(R.id.video_current_time);
        videoDurationTextView = playerUI.findViewById(R.id.video_duration);
        playPauseButton = playerUI.findViewById(R.id.play_pause_button);
        enterExitFullscreenButton = playerUI.findViewById(R.id.enter_exit_fullscreen_button);

        playPauseButton.setOnClickListener( (view) -> {
            if(playing) youTubePlayer.pause();
            else youTubePlayer.play();

            playing = !playing;
        });

        enterExitFullscreenButton.setOnClickListener( (view) -> {
            if(fullscreen) youTubePlayerView.exitFullScreen();
            else youTubePlayerView.enterFullScreen();

            fullscreen = !fullscreen;
        });
    }

    @Override
    public void onReady() {
        progressbar.setVisibility(View.GONE);
    }

    @Override
    public void onStateChange(@PlayerConstants.PlayerState.State int state) {
        if(state == PlayerConstants.PlayerState.PLAYING || state == PlayerConstants.PlayerState.PAUSED || state == PlayerConstants.PlayerState.VIDEO_CUED)
            panel.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        else
            if(state == PlayerConstants.PlayerState.BUFFERING)
                panel.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCurrentSecond(float second) {
        videoCurrentTimeTextView.setText(second+"");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onVideoDuration(float duration) {
        videoDurationTextView.setText(duration+"");
    }

    @Override
    public void onYouTubePlayerEnterFullScreen() {
        ViewGroup.LayoutParams viewParams = playerUI.getLayoutParams();
        viewParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        viewParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        playerUI.setLayoutParams(viewParams);
    }

    @Override
    public void onYouTubePlayerExitFullScreen() {
        ViewGroup.LayoutParams viewParams = playerUI.getLayoutParams();
        viewParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        viewParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        playerUI.setLayoutParams(viewParams);
    }
}
