package com.pierfrancescosoffritti.youtubeplayer;

import android.support.annotation.NonNull;

public class InnerYouTubePlayerListener implements YouTubePlayer.YouTubeListener {

    private boolean autoPlay = true;

    public boolean isAutoPlay() {
        return autoPlay;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    @Override
    public void onReady(@NonNull YouTubePlayer youTubePlayer) {

    }

    @Override
    public void onStateChange(@YouTubePlayer.State.YouTubePlayerState int state, @NonNull YouTubePlayer youTubePlayer) {

    }

    @Override
    public void onPlaybackQualityChange(@YouTubePlayer.PlaybackQuality.Quality int playbackQuality, @NonNull YouTubePlayer youTubePlayer) {

    }

    @Override
    public void onPlaybackRateChange(double rate, @NonNull YouTubePlayer youTubePlayer) {

    }

    @Override
    public void onError(String arg, @NonNull YouTubePlayer youTubePlayer) {

    }

    @Override
    public void onApiChange(String arg, @NonNull YouTubePlayer youTubePlayer) {

    }

    @Override
    public void onCurrentSecond(double second, @NonNull YouTubePlayer youTubePlayer) {

    }

    @Override
    public void onDuration(double duration, @NonNull YouTubePlayer youTubePlayer) {

    }

    @Override
    public void onLog(String log, @NonNull YouTubePlayer youTubePlayer) {

    }
}
