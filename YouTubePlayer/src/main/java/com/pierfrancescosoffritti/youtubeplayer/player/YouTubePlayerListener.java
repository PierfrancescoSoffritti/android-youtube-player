package com.pierfrancescosoffritti.youtubeplayer.player;

public interface YouTubePlayerListener {
    void onReady();
    void onStateChange(@PlayerConstants.PlayerState.State int state);
    void onPlaybackQualityChange(@PlayerConstants.PlaybackQuality.Quality String playbackQuality);
    void onPlaybackRateChange(@PlayerConstants.PlaybackRate.Rate String playbackRate);
    void onError(@PlayerConstants.PlayerError.Error int error);
    void onApiChange();
    void onCurrentSecond(float second);
    void onVideoDuration(float duration);
    void onMessage(String log);
//    void onVideoTitle(String videoTitle);
    void onVideoId(String videoId);
}
