package com.pierfrancescosoffritti.youtubeplayer.utils;

import android.support.annotation.NonNull;

import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.PlayerConstants;

/**
 * Utility class responsible for tracking the state of YouTubePlayer.
 * This is a YouTubePlayerListener, therefore is responsibility of the user to add and remove it as a listener on the YouTubePlayer object.
 */
public class YouTubePlayerStateTracker extends AbstractYouTubePlayerListener{
    @PlayerConstants.PlayerState.State private int currentState = PlayerConstants.PlayerState.UNKNOWN;
    private float currentSecond;
    private float videoDuration;
    private String videoId;

    @Override
    public void onStateChange(@PlayerConstants.PlayerState.State int state) {
        this.currentState = state;
    }

    @Override
    public void onCurrentSecond(float second) {
        currentSecond = second;
    }

    @Override
    public void onVideoDuration(float duration) {
        videoDuration = duration;
    }

    @Override
    public void onVideoId(@NonNull String videoId) {
        this.videoId = videoId;
    }

    /**
     * @return the player state. A value from {@link PlayerConstants.PlayerState.State}
     */
    public int getCurrentState() {
        return currentState;
    }

    public float getCurrentSecond() {
        return currentSecond;
    }

    public float getVideoDuration() {
        return videoDuration;
    }

    public String getVideoId() {
        return videoId;
    }
}
