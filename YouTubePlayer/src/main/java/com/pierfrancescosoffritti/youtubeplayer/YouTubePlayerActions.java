package com.pierfrancescosoffritti.youtubeplayer;

/**
 * Interface used to enforce contract between YouTubePlayer and YouTubePlayerView
 */
interface YouTubePlayerActions {
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
    @YouTubePlayer.PlayerState.State int getCurrentState();
}
