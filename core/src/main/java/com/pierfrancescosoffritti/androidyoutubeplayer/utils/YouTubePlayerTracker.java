package com.pierfrancescosoffritti.androidyoutubeplayer.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;

/**
 * Utility class responsible for tracking the state of YouTubePlayer.
 * This is a YouTubePlayerListener, therefore is responsibility of the user to add and remove it as a listener on the YouTubePlayer object.
 */
public class YouTubePlayerTracker extends AbstractYouTubePlayerListener {
    private PlayerConstants.PlayerState currentState = PlayerConstants.PlayerState.UNKNOWN;
    private float currentSecond;
    private float videoDuration;
    private String videoId;

    @Override
    public void onStateChange(@NonNull PlayerConstants.PlayerState state) {
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
     * @return the player state. A value from {@link PlayerConstants.PlayerState}
     */
    public PlayerConstants.PlayerState getState() {
        return currentState;
    }

    public float getCurrentSecond() {
        return currentSecond;
    }

    public float getVideoDuration() {
        return videoDuration;
    }

    public @Nullable String getVideoId() {
        return videoId;
    }
}
