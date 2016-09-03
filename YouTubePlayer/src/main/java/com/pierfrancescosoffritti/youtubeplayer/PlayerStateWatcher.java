package com.pierfrancescosoffritti.youtubeplayer;

public class PlayerStateWatcher implements YouTubePlayer.YouTubeListener {

    private boolean isReady;

    public PlayerStateWatcher() {
        isReady = false;
    }

    @Override
    public void onReady() {
        isReady = true;
    }

    @Override
    public void onStateChange(@YouTubePlayer.State.YouTubePlayerState int state) {

    }

    @Override
    public void onPlaybackQualityChange(@YouTubePlayer.PlaybackQuality.Quality int playbackQuality) {

    }

    @Override
    public void onPlaybackRateChange(double rate) {

    }

    @Override
    public void onError(@YouTubePlayer.Error.PlayerError int error) {

    }

    @Override
    public void onApiChange() {

    }

    @Override
    public void onCurrentSecond(float second) {

    }

    @Override
    public void onVideoDuration(float duration) {

    }

    @Override
    public void onLog(String log) {

    }

    @Override
    public void onVideoTitle(String videoTitle) {

    }

    @Override
    public void onVideoId(String videoId) {

    }

    public boolean isReady() {
        return isReady;
    }
}
