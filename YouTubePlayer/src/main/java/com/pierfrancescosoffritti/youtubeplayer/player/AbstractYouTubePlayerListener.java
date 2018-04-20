package com.pierfrancescosoffritti.youtubeplayer.player;

import android.support.annotation.NonNull;

/**
 * Extend this class if you want to implement only some of the methods of {@link YouTubePlayerListener}
 */
public abstract class AbstractYouTubePlayerListener implements YouTubePlayerListener {
    @Override public void onReady() { }
    @Override public void onStateChange(@PlayerConstants.PlayerState.State int state) { }
    @Override public void onPlaybackQualityChange(@NonNull @PlayerConstants.PlaybackQuality.Quality String playbackQuality) { }
    @Override public void onPlaybackRateChange(@NonNull @PlayerConstants.PlaybackRate.Rate String rate) { }
    @Override public void onError(@PlayerConstants.PlayerError.Error int error) { }
    @Override public void onApiChange() { }
    @Override public void onCurrentSecond(float second) { }
    @Override public void onVideoDuration(float duration) { }
    @Override public void onVideoLoadedFraction(float fraction) { }
    @Override public void onVideoId(@NonNull String videoId) { }
}
