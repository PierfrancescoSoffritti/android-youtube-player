package com.pierfrancescosoffritti.youtubeplayer;

class PlaybackResumer extends AbstractYouTubePlayerListener {

    private boolean isPlaying = false;
    private int error = Integer.MIN_VALUE;

    private String videoId;
    private float currentSecond;

    private YouTubePlayerView youTubePlayerView;

    PlaybackResumer(YouTubePlayerView youTubePlayerView) {
        this.youTubePlayerView = youTubePlayerView;
    }

    void resume() {
        if(isPlaying && error == YouTubePlayer.PlayerError.HTML_5_PLAYER)
            youTubePlayerView.loadVideo(videoId, currentSecond);
        else if(!isPlaying && error == YouTubePlayer.PlayerError.HTML_5_PLAYER)
            youTubePlayerView.cueVideo(videoId, currentSecond);

        error = -1;
    }

    @Override
    public void onStateChange(@YouTubePlayer.PlayerState.State int state) {
        switch (state) {
            case YouTubePlayer.PlayerState.ENDED:
                isPlaying = false;
                return;
            case YouTubePlayer.PlayerState.PAUSED:
                isPlaying = false;
                return;
            case YouTubePlayer.PlayerState.PLAYING:
                isPlaying = true;
                return;
            default:
                return;
        }
    }

    @Override
    public void onError(@YouTubePlayer.PlayerError.Error int error) {
        if(error == YouTubePlayer.PlayerError.HTML_5_PLAYER)
            this.error = error;
    }

    @Override
    public void onCurrentSecond(float second) {
        this.currentSecond = second;
    }

    @Override
    public void onVideoId(String videoId) {
        this.videoId = videoId;
    }
}
