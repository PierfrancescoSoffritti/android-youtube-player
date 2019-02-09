package com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners

import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer

interface YouTubePlayerListener {
    /**
     * Called when the player is ready to play videos. You should start interacting with the player only after this method is called.
     * @param youTubePlayer The [YouTubePlayer] object.
     */
    fun onReady(youTubePlayer: YouTubePlayer)

    /**
     * Use this method to track the state of the playback. Check [PlayerConstants.PlayerState] to see all the possible states.
     * @param state a state from [PlayerConstants.PlayerState]
     */
    fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState)

    /**
     * Use this method to be notified when the quality of the playback changes. Check [PlayerConstants.PlaybackQuality] to see all the possible values.
     * @param playbackQuality a state from [PlayerConstants.PlaybackQuality]
     */
    fun onPlaybackQualityChange(youTubePlayer: YouTubePlayer, playbackQuality: PlayerConstants.PlaybackQuality)

    /**
     * Use this method to be notified when the speed of the playback changes. Check [PlayerConstants.PlaybackRate] to see all the possible values.
     * @param playbackRate a state from [PlayerConstants.PlaybackRate]
     */
    fun onPlaybackRateChange(youTubePlayer: YouTubePlayer, playbackRate: PlayerConstants.PlaybackRate)

    /**
     * Use this method to be notified when an error occurs in the player. Check [PlayerConstants.PlayerError] to see all the possible values.
     * @param error a state from [PlayerConstants.PlayerError]
     */
    fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError)

    fun onApiChange(youTubePlayer: YouTubePlayer)

    /**
     * This methods is called periodically by the player, the argument is the number of seconds that have been played.
     * @param second current second of the playback
     */
    fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float)

    /**
     * Use this method to know the duration in seconds of the currently playing video. <br></br><br></br>
     * Note that getDuration() will return 0 until the video's metadata is loaded, which normally happens just after the video starts playing.
     * @param duration total duration of the video
     */
    fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float)

    /**
     * This methods is called periodically by the player, the argument is the percentage of the video that has been buffered.
     * @param loadedFraction a number between 0 and 1 that represents the percentage of the video that has been buffered.
     */
    fun onVideoLoadedFraction(youTubePlayer: YouTubePlayer, loadedFraction: Float)

    /**
     * Use this method to know the id of the video being played.
     * @param videoId the id of the video being played
     */
    fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String)
}
