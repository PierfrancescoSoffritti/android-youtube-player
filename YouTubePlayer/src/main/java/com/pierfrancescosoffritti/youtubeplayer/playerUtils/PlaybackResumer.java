package com.pierfrancescosoffritti.youtubeplayer.playerUtils;

import android.annotation.SuppressLint;

import com.pierfrancescosoffritti.youtubeplayer.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.YouTubePlayerView;

/**
 * Class responsible for resuming the playback state in case of network problems.
 * eg: player is playing -> network goes out -> player stops -> network comes back -> player resumes playback automatically.
 */
public class PlaybackResumer extends AbstractYouTubePlayerListener {

    private static final int NO_ERROR = Integer.MIN_VALUE;

    private boolean isPlaying = false;
    private int error = NO_ERROR;

    private String currentVideoId;
    private float currentSecond;

    private YouTubePlayerView youTubePlayerView;

    public PlaybackResumer(YouTubePlayerView youTubePlayerView) {
        this.youTubePlayerView = youTubePlayerView;
    }

    public  void resume() {
        if(isPlaying && error == YouTubePlayer.PlayerError.HTML_5_PLAYER)
            youTubePlayerView.loadVideo(currentVideoId, currentSecond);
        else if(!isPlaying && error == YouTubePlayer.PlayerError.HTML_5_PLAYER)
            youTubePlayerView.cueVideo(currentVideoId, currentSecond);

        error = NO_ERROR;
    }

    @SuppressLint("SwitchIntDef")
    @Override
    public void onStateChange(@YouTubePlayer.PlayerState.State int state) {
        switch (state) {
            case YouTubePlayer.PlayerState.ENDED:
                isPlaying = false;
                return;
            case YouTubePlayer.PlayerState.PAUSED:
                isPlaying = false;
                return;
            case YouTubePlayer.PlayerState.PLAYING:
                isPlaying = true;
                return;
            default:
                return;
        }
    }

    @Override
    public void onError(@YouTubePlayer.PlayerError.Error int error) {
        if(error == YouTubePlayer.PlayerError.HTML_5_PLAYER)
            this.error = error;
    }

    @Override
    public void onCurrentSecond(float second) {
        this.currentSecond = second;
    }

    @Override
    public void onVideoId(String videoId) {
        this.currentVideoId = videoId;
    }
}
