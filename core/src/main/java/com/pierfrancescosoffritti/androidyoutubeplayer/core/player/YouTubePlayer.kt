package com.pierfrancescosoffritti.androidyoutubeplayer.core.player

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener

/**
 * Use this interface to control the playback of YouTube videos and to listen to their events.
 */
interface YouTubePlayer {
    /**
     * Loads and automatically plays the video.
     * @param videoId id of the video
     * @param startSeconds the time from which the video should start playing
     */
    fun loadVideo(videoId: String, startSeconds: Float)

    /**
     * Loads the video's thumbnail and prepares the player to play the video. Does not automatically play the video.
     * @param videoId id of the video
     * @param startSeconds the time from which the video should start playing
     */
    fun cueVideo(videoId: String, startSeconds: Float)

    fun play()
    fun pause()

    fun mute()
    fun unMute()

    /**
     * @param volumePercent Integer between 0 and 100
     */
    fun setVolume(volumePercent: Int)

    /**
     *
     * @param time The absolute time in seconds to seek to
     */
    fun seekTo(time: Float)

    /**
     * Sets the suggested playback rate for the video.
     *
     * The YouTubePlayer does not guarantee that the playback rate will actually change or change
     * to the specified value.
     * If confirmation of the change is required use {@code onPlaybackRateChange}.
     *
     * @param suggestedRate Any value between {@code 0.25} and {@code 2.0}. Values below {@code 0.25}
     *                      and above {@code 2.0} will be automatically increased to {@code 0.25}
     *                      and decreased to {@code 2.0} accordingly from the YouTube API if the
     *                      rate get changed. Values that are not divisible by {@code 0.05} might
     *                      be rounded down (e.g. {@code 0.57} to {@code 0.55}). If the value changes
     *                      the callback function {@code onPlaybackRateChange} will provide the actual
     *                      rate that has been applied.
     * @see YouTubePlayerListener.onPlaybackRateChange
     */
    fun setPlaybackRate(suggestedRate: Float)

    fun addListener(listener: YouTubePlayerListener): Boolean
    fun removeListener(listener: YouTubePlayerListener): Boolean
}
