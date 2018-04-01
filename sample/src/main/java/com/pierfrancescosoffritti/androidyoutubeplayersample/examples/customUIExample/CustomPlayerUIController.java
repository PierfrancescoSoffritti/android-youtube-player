package com.pierfrancescosoffritti.androidyoutubeplayersample.examples.customUIExample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayersample.R;

class CustomPlayerUIController extends AbstractYouTubePlayerListener implements YouTubePlayerFullScreenListener {

    private final View playerUI;

    private Context context;
    private YouTubePlayer youTubePlayer;
    private YouTubePlayerView youTubePlayerView;

    private View panel;
    private View progressbar;
    private TextView videoCurrentTime;
    private TextView videoDuration;
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
        videoCurrentTime = playerUI.findViewById(R.id.video_current_time);
        videoDuration = playerUI.findViewById(R.id.video_duration);
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
        videoCurrentTime.setText(second+"");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onVideoDuration(float duration) {
        videoDuration.setText(duration+"");
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
