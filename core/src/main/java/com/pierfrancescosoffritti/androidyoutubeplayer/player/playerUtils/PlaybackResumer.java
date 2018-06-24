package com.pierfrancescosoffritti.androidyoutubeplayer.player.playerUtils;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;

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

    public  void resume(YouTubePlayer youTubePlayer) {
        if(isPlaying && error == PlayerConstants.PlayerError.HTML_5_PLAYER)
            youTubePlayer.loadVideo(currentVideoId, currentSecond);
        else if(!isPlaying && error == PlayerConstants.PlayerError.HTML_5_PLAYER)
            youTubePlayer.cueVideo(currentVideoId, currentSecond);

        error = NO_ERROR;
    }

    @SuppressLint("SwitchIntDef")
    @Override
    public void onStateChange(@PlayerConstants.PlayerState.State int state) {
        switch (state) {
            case PlayerConstants.PlayerState.ENDED:
                isPlaying = false;
                return;
            case PlayerConstants.PlayerState.PAUSED:
                isPlaying = false;
                return;
            case PlayerConstants.PlayerState.PLAYING:
                isPlaying = true;
                return;
            default:
                return;
        }
    }

    @Override
    public void onError(@PlayerConstants.PlayerError.Error int error) {
        if(error == PlayerConstants.PlayerError.HTML_5_PLAYER)
            this.error = error;
    }

    @Override
    public void onCurrentSecond(float second) {
        this.currentSecond = second;
    }

    @Override
    public void onVideoId(@NonNull String videoId) {
        this.currentVideoId = videoId;
    }
}
