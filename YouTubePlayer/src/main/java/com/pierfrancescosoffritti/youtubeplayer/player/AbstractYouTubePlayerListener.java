package com.pierfrancescosoffritti.youtubeplayer.player;

/**
 * Extend this class if you don't want to implement all the method of {@link YouTubePlayerListener}
 */
public abstract class AbstractYouTubePlayerListener implements YouTubePlayerListener {
    @Override public void onReady() { }
    @Override public void onStateChange(@PlayerConstants.PlayerState.State int state) { }
    @Override public void onPlaybackQualityChange(@PlayerConstants.PlaybackQuality.Quality String playbackQuality) { }
    @Override public void onPlaybackRateChange(@PlayerConstants.PlaybackRate.Rate String rate) { }
    @Override public void onError(@PlayerConstants.PlayerError.Error int error) { }
    @Override public void onApiChange() { }
    @Override public void onCurrentSecond(float second) { }
    @Override public void onVideoDuration(float duration) { }
    @Override public void onMessage(String log) { }
//    @Override public void onVideoTitle(String videoTitle) { }
    @Override public void onVideoId(String videoId) { }
}
