package com.pierfrancescosoffritti.youtubeplayer.player;

import android.support.annotation.NonNull;

public interface YouTubePlayerListener {
    /**
     * Called when the player is ready to play videos. You should start interacting with the player only after it is ready.
     */
    void onReady();

    /**
     * Use this method to track the state of the playback. Check {@link PlayerConstants.PlayerState.State} to see all the possible states.
     * @param state a state from {@link PlayerConstants.PlayerState.State}
     */
    void onStateChange(@PlayerConstants.PlayerState.State int state);

    /**
     * Use this method to be notified when the quality of the playback changes. Check {@link PlayerConstants.PlaybackQuality.Quality} to see all the possible values.
     * @param playbackQuality a state from {@link PlayerConstants.PlaybackQuality.Quality}
     */
    void onPlaybackQualityChange(@NonNull @PlayerConstants.PlaybackQuality.Quality String playbackQuality);

    /**
     * Use this method to be notified when the speed of the playback changes. Check {@link PlayerConstants.PlaybackRate.Rate} to see all the possible values.
     * @param playbackRate a state from {@link PlayerConstants.PlaybackRate.Rate}
     */
    void onPlaybackRateChange(@NonNull @PlayerConstants.PlaybackRate.Rate String playbackRate);

    /**
     * Use this method to be notified when an error occurs in the player. Check {@link PlayerConstants.PlayerError.Error} to see all the possible values.
     * @param error a state from {@link PlayerConstants.PlayerError.Error}
     */
    void onError(@PlayerConstants.PlayerError.Error int error);

    void onApiChange();

    /**
     * Use this method to know at which second (of the video duration) the the currently playing video is.
     * @param second current second of the playback
     */
    void onCurrentSecond(float second);

    /**
     * Use this method to know the duration in seconds of the currently playing video. <br/><br/>
     * Note that getDuration() will return 0 until the video's metadata is loaded, which normally happens just after the video starts playing.
     * @param duration total duration of the video
     */
    void onVideoDuration(float duration);

    /**
     * Use this method to know the percentage of the video that the player shows as already buffered.
     * @param loadedFraction a number between 0 and 1 that specifies the percentage of the video that the player shows as buffered
     */
    void onVideoLoadedFraction(float loadedFraction);

    /**
     * Use this method to know the id of the video being played.
     * @param videoId the id of the video being played
     */
    void onVideoId(@NonNull String videoId);
}
