package com.pierfrancescosoffritti.youtubeplayer.player;

/**
 * Interface used to enforce contract between YouTubePlayer and YouTubePlayerView
 */
public interface YouTubePlayer {
    /**
     * Loads and automatically plays the specified video.
     * @param videoId id of the video
     * @param startSeconds the time from which the video should start playing
     */
    void loadVideo(final String videoId, final float startSeconds);

    /**
     * Loads the specified video's thumbnail and prepares the player to play the video. Does not automatically play the video.
     * @param videoId id of the video
     * @param startSeconds the time from which the video should start playing
     */
    void cueVideo(final String videoId, final float startSeconds);
    void play();
    void pause();
    void mute();
    void unMute();
    void seekTo(final int time);
    @PlayerConstants.PlayerState.State
    int getCurrentState();

    boolean addListener(WebViewYouTubePlayer.YouTubePlayerListener listener);
    boolean removeListener(WebViewYouTubePlayer.YouTubePlayerListener listener);

    interface YouTubePlayerListener {
        void onReady();
        void onStateChange(@PlayerConstants.PlayerState.State int state);
        void onPlaybackQualityChange(@PlayerConstants.PlaybackQuality.Quality int playbackQuality);
        void onPlaybackRateChange(@PlayerConstants.PlaybackRate.Rate String playbackRate);
        void onError(@PlayerConstants.PlayerError.Error int error);
        void onApiChange();
        void onCurrentSecond(float second);
        void onVideoDuration(float duration);
        void onMessage(String log);
        void onVideoTitle(String videoTitle);
        void onVideoId(String videoId);
    }

    interface YouTubePlayerInitListener {
        void onInitSuccess(YouTubePlayer youTubePlayer);
    }
}
