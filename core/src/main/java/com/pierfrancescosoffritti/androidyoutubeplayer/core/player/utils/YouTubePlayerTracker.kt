package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

/**
 * Utility class responsible for tracking the state of YouTubePlayer.
 * This is a YouTubePlayerListener, therefore to work it has to be added as listener to a YouTubePlayer.
 */
class YouTubePlayerTracker : AbstractYouTubePlayerListener() {
  /**
   * @return the player state. A value from [PlayerConstants.PlayerState]
   */
  var state: PlayerConstants.PlayerState = PlayerConstants.PlayerState.UNKNOWN
    private set
  var currentSecond: Float = 0f
    private set
  var videoDuration: Float = 0f
    private set
  var videoId: String? = null
    private set

  override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
    this.state = state
  }

  override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
    currentSecond = second
  }

  override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {
    videoDuration = duration
  }

  override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {
    this.videoId = videoId
  }
}
