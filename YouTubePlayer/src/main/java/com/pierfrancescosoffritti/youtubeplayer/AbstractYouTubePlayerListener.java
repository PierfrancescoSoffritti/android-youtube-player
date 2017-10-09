package com.pierfrancescosoffritti.youtubeplayer;

/**
 * Extend this class if you don't want to implement all the method of {@link YouTubePlayer.YouTubePlayerListener}
 */
public abstract class AbstractYouTubePlayerListener implements YouTubePlayer.YouTubePlayerListener {
    @Override public void onReady() { }
    @Override public void onStateChange(@YouTubePlayer.PlayerState.State int state) { }
    @Override public void onPlaybackQualityChange(@YouTubePlayer.PlaybackQuality.Quality int playbackQuality) { }
    @Override public void onPlaybackRateChange(@YouTubePlayer.PlaybackRate.Rate String rate) { }
    @Override public void onError(@YouTubePlayer.PlayerError.Error int error) { }
    @Override public void onApiChange() { }
    @Override public void onCurrentSecond(float second) { }
    @Override public void onVideoDuration(float duration) { }
    @Override public void onMessage(String log) { }
    @Override public void onVideoTitle(String videoTitle) { }
    @Override public void onVideoId(String videoId) { }
}
