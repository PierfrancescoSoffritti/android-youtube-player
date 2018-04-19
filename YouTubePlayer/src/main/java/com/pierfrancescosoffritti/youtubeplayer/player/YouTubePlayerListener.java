package com.pierfrancescosoffritti.youtubeplayer.player;

import android.support.annotation.NonNull;

public interface YouTubePlayerListener {
    void onReady();
    void onStateChange(@PlayerConstants.PlayerState.State int state);
    void onPlaybackQualityChange(@NonNull @PlayerConstants.PlaybackQuality.Quality String playbackQuality);
    void onPlaybackRateChange(@NonNull @PlayerConstants.PlaybackRate.Rate String playbackRate);
    void onError(@PlayerConstants.PlayerError.Error int error);
    void onApiChange();
    void onCurrentSecond(float second);
    void onVideoDuration(float duration);
    void onVideoLoadedFraction(float loadedFraction);
    void onMessage(@NonNull String message);
    void onVideoId(@NonNull String videoId);
}
