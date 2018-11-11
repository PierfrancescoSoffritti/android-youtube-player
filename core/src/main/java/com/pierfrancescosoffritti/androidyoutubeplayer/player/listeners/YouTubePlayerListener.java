package com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners;

import androidx.annotation.NonNull;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;

public interface YouTubePlayerListener {
    /**
     * Called when the player is ready to play videos. You should start interacting with the player only after it is ready.
     */
    void onReady();

    /**
     * Use this method to track the state of the playback. Check {@link PlayerConstants.PlayerState} to see all the possible states.
     * @param state a state from {@link PlayerConstants.PlayerState}
     */
    void onStateChange(@NonNull PlayerConstants.PlayerState state);

    /**
     * Use this method to be notified when the quality of the playback changes. Check {@link PlayerConstants.PlaybackQuality} to see all the possible values.
     * @param playbackQuality a state from {@link PlayerConstants.PlaybackQuality}
     */
    void onPlaybackQualityChange(@NonNull PlayerConstants.PlaybackQuality playbackQuality);

    /**
     * Use this method to be notified when the speed of the playback changes. Check {@link PlayerConstants.PlaybackRate} to see all the possible values.
     * @param playbackRate a state from {@link PlayerConstants.PlaybackRate}
     */
    void onPlaybackRateChange(@NonNull PlayerConstants.PlaybackRate playbackRate);

    /**
     * Use this method to be notified when an error occurs in the player. Check {@link PlayerConstants.PlayerError} to see all the possible values.
     * @param error a state from {@link PlayerConstants.PlayerError}
     */
    void onError(@NonNull PlayerConstants.PlayerError error);

    void onApiChange();

    /**
     * This methods is called periodically by the player, the argument is the number of seconds that have been played.
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
     * This methods is called periodically by the player, the argument is the percentage of the video that has been buffered.
     * @param loadedFraction a number between 0 and 1 that represents the percentage of the video that has been buffered.
     */
    void onVideoLoadedFraction(float loadedFraction);

    /**
     * Use this method to know the id of the video being played.
     * @param videoId the id of the video being played
     */
    void onVideoId(@NonNull String videoId);
}
