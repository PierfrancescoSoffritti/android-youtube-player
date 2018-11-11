package com.pierfrancescosoffritti.androidyoutubeplayer.player.playerUtils;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;

/**
 * Class responsible for resuming the playback state in case of network problems.
 * eg: player is playing -> network goes out -> player stops -> network comes back -> player resumes playback automatically.
 */
public class PlaybackResumer extends AbstractYouTubePlayerListener {

    private boolean isPlaying = false;
    private PlayerConstants.PlayerError error = null;

    private String currentVideoId;
    private float currentSecond;

    public  void resume(YouTubePlayer youTubePlayer) {
        if(isPlaying && error == PlayerConstants.PlayerError.HTML_5_PLAYER)
            youTubePlayer.loadVideo(currentVideoId, currentSecond);
        else if(!isPlaying && error == PlayerConstants.PlayerError.HTML_5_PLAYER)
            youTubePlayer.cueVideo(currentVideoId, currentSecond);

        error = null;
    }

    @SuppressLint("SwitchIntDef")
    @Override
    public void onStateChange(@NonNull PlayerConstants.PlayerState state) {
        switch (state) {
            case ENDED:
                isPlaying = false;
                return;
            case PAUSED:
                isPlaying = false;
                return;
            case PLAYING:
                isPlaying = true;
                return;
            default:
                return;
        }
    }

    @Override
    public void onError(@NonNull PlayerConstants.PlayerError error) {
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
