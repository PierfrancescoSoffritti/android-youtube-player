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

  private var currentPlaylistId: String? = null
  private var currentPlaylistType: String? = null
  private var currentPlaylistIndex: Int? = null
  private var currentVideoList: List<String>? = null

  fun resume(youTubePlayer: YouTubePlayer) {
    val videoId = currentVideoId
    val playlistId = currentPlaylistId
    val playlistType = currentPlaylistType
    val playlistIndex = currentPlaylistIndex
    val videoList = currentVideoList
    if (videoId != null) {
      if (isPlaying && error == PlayerConstants.PlayerError.HTML_5_PLAYER) {
        youTubePlayer.loadOrCueVideo(canLoad, videoId, currentSecond)
      } else if (!isPlaying && error == PlayerConstants.PlayerError.HTML_5_PLAYER) {
        youTubePlayer.cueVideo(videoId, currentSecond)
      }

      error = null
    } else if (playlistId != null && playlistType != null && playlistIndex != null) {
      if (isPlaying && error == PlayerConstants.PlayerError.HTML_5_PLAYER) {
        youTubePlayer.loadPlaylist(playlistId, playlistType, playlistIndex, currentSecond)
      } else if (!isPlaying && error == PlayerConstants.PlayerError.HTML_5_PLAYER) {
        youTubePlayer.cuePlaylist(playlistId, playlistType, playlistIndex, currentSecond)
      }

      error = null
    } else if (videoList != null && playlistIndex != null) {
      if (isPlaying && error == PlayerConstants.PlayerError.HTML_5_PLAYER) {
        youTubePlayer.loadPlaylist(videoList, playlistIndex, currentSecond)
      } else if (!isPlaying && error == PlayerConstants.PlayerError.HTML_5_PLAYER) {
        youTubePlayer.cuePlaylist(videoList, playlistIndex, currentSecond)
      }

      error = null
    }
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

  override fun onPlaylistId(youTubePlayer: YouTubePlayer, playlistId: String) {
    currentPlaylistId = playlistId
  }

  override fun onPlaylistIndex(youTubePlayer: YouTubePlayer, index: Int) {
    currentPlaylistIndex = index
  }

  override fun onVideoList(instance: YouTubePlayer, list: List<String>) {
    currentVideoList = list
  }

  override fun onPlaylistType(instance: YouTubePlayer, playlistType: String) {
    currentPlaylistType = playlistType
  }

  fun onLifecycleResume() {
    canLoad = true
  }

  fun onLifecycleStop() {
    canLoad = false
  }
}
