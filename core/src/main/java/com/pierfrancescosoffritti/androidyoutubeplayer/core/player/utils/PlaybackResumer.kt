package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

/**
 * Class responsible for resuming the playback state in case of network problems.
 * eg: player is playing -> network goes out -> player stops -> network comes back -> player resumes playback automatically.
 */
internal class PlaybackResumer : AbstractYouTubePlayerListener() {

  private var canLoad = false
  private var isPlaying = false
  private var error: PlayerConstants.PlayerError? = null

  private var currentVideoId: String? = null
  private var currentSecond: Float = 0f

  fun resume(youTubePlayer: YouTubePlayer) {
    val videoId = currentVideoId ?: return
    if (isPlaying && error == PlayerConstants.PlayerError.HTML_5_PLAYER) {
      youTubePlayer.loadOrCueVideo(canLoad, videoId, currentSecond)
    }
    else if (!isPlaying && error == PlayerConstants.PlayerError.HTML_5_PLAYER) {
      youTubePlayer.cueVideo(videoId, currentSecond)
    }

    error = null
  }

  override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
    when (state) {
      PlayerConstants.PlayerState.ENDED, PlayerConstants.PlayerState.PAUSED -> isPlaying = false
      PlayerConstants.PlayerState.PLAYING -> isPlaying = true
      else -> { }
    }
  }

  override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
    if (error == PlayerConstants.PlayerError.HTML_5_PLAYER) {
      this.error = error
    }
  }

  override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
    currentSecond = second
  }

  override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {
    currentVideoId = videoId
  }

  fun onLifecycleResume() {
    canLoad = true
  }

  fun onLifecycleStop() {
    canLoad = false
  }
}
