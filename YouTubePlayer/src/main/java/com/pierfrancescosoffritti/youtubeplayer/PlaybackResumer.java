package com.pierfrancescosoffritti.youtubeplayer;

public class PlaybackResumer implements YouTubePlayer.YouTubeListener {

    private boolean isPlaying = false;
    private int error = -1;

    private String videoId;
    private float currentSecond;

    private YouTubePlayerView youTubePlayerView;

    public PlaybackResumer(YouTubePlayerView youTubePlayerView) {
        this.youTubePlayerView = youTubePlayerView;
    }

    public void resume() {
        if(isPlaying && error == YouTubePlayer.PlayerError.HTML_5_PLAYER)
            youTubePlayerView.loadVideo(videoId, currentSecond);
        else if(!isPlaying && error == YouTubePlayer.PlayerError.HTML_5_PLAYER)
            youTubePlayerView.cueVideo(videoId, currentSecond);

        error = -1;
    }

    @Override
    public void onReady() {

    }

    @Override
    public void onStateChange(@YouTubePlayer.PlayerState.State int state) {
        switch (state) {
            case YouTubePlayer.PlayerState.ENDED:
                isPlaying = false;
                break;
            case YouTubePlayer.PlayerState.PAUSED:
                isPlaying = false;
                break;
            case YouTubePlayer.PlayerState.PLAYING:
                isPlaying = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void onPlaybackQualityChange(@YouTubePlayer.PlaybackQuality.Quality int playbackQuality) {

    }

    @Override
    public void onPlaybackRateChange(@YouTubePlayer.PlaybackRate.Rate String rate) {

    }

    @Override
    public void onError(@YouTubePlayer.PlayerError.Error int error) {
        if(error == YouTubePlayer.PlayerError.HTML_5_PLAYER)
            this.error = error;
    }

    @Override
    public void onApiChange() {

    }

    @Override
    public void onCurrentSecond(float second) {
        this.currentSecond = second;
    }

    @Override
    public void onVideoDuration(float duration) {

    }

    @Override
    public void onMessage(String log) {

    }

    @Override
    public void onVideoTitle(String videoTitle) {

    }

    @Override
    public void onVideoId(String videoId) {
        this.videoId = videoId;
    }
}
