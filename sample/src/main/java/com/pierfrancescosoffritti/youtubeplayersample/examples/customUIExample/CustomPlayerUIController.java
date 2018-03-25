package com.pierfrancescosoffritti.youtubeplayersample.examples.customUIExample;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener;

class CustomPlayerUIController extends AbstractYouTubePlayerListener implements YouTubePlayerFullScreenListener {

    private Context context;
    private View panel;

    CustomPlayerUIController(Context context, View playerUI) {
        this.context = context;
        initViews(playerUI);
    }

    private void initViews(View playerUI) {
        panel = playerUI.findViewById(com.pierfrancescosoffritti.youtubeplayer.R.id.panel);
    }

    @Override
    public void onReady() {

    }

    @Override
    public void onStateChange(@PlayerConstants.PlayerState.State int state) {
        if(state == PlayerConstants.PlayerState.PLAYING || state == PlayerConstants.PlayerState.PAUSED || state == PlayerConstants.PlayerState.VIDEO_CUED)
            panel.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        else
            if(state == PlayerConstants.PlayerState.BUFFERING)
                panel.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
    }

    @Override
    public void onCurrentSecond(float second) {

    }

    @Override
    public void onVideoDuration(float duration) {

    }

    @Override
    public void onYouTubePlayerEnterFullScreen() {

    }

    @Override
    public void onYouTubePlayerExitFullScreen() {

    }
}
