package com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners;

import androidx.annotation.NonNull;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;

/**
 * Extend this class if you want to implement only some of the methods of {@link YouTubePlayerListener}
 */
public abstract class AbstractYouTubePlayerListener implements YouTubePlayerListener {
    @Override public void onReady() { }
    @Override public void onStateChange(@NonNull PlayerConstants.PlayerState state) { }
    @Override public void onPlaybackQualityChange(@NonNull PlayerConstants.PlaybackQuality playbackQuality) { }
    @Override public void onPlaybackRateChange(@NonNull PlayerConstants.PlaybackRate rate) { }
    @Override public void onError(@NonNull PlayerConstants.PlayerError error) { }
    @Override public void onApiChange() { }
    @Override public void onCurrentSecond(float second) { }
    @Override public void onVideoDuration(float duration) { }
    @Override public void onVideoLoadedFraction(float fraction) { }
    @Override public void onVideoId(@NonNull String videoId) { }
}
