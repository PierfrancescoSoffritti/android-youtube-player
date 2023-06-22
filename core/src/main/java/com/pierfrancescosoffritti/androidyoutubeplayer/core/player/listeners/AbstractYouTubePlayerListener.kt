package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer

/**
 * Extend this class if you want to implement only some of the methods of [YouTubePlayerListener]
 */
abstract class AbstractYouTubePlayerListener : YouTubePlayerListener {
  override fun onReady(youTubePlayer: YouTubePlayer) {}
  override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {}
  override fun onPlaybackQualityChange(youTubePlayer: YouTubePlayer, playbackQuality: PlayerConstants.PlaybackQuality) {}
  override fun onPlaybackRateChange(youTubePlayer: YouTubePlayer, playbackRate: PlayerConstants.PlaybackRate) {}
  override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {}
  override fun onApiChange(youTubePlayer: YouTubePlayer) {}
  override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {}
  override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {}
  override fun onVideoLoadedFraction(youTubePlayer: YouTubePlayer, loadedFraction: Float) {}
  override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {}
  override fun onPlaylistId(youTubePlayer: YouTubePlayer, playlistId: String) {}
  override fun onPlaylistType(instance: YouTubePlayer, playlistType: String) {}
  override fun onPlaylistIndex(youTubePlayer: YouTubePlayer, index: Int) {}
  override fun onPlaylistLength(instance: YouTubePlayer, length: Int) {}
  override fun onVideoList(instance: YouTubePlayer, list: List<String>) {}
  override fun onLoopStatus(instance: YouTubePlayer, loop: Boolean) {}
  override fun onShuffleStatus(instance: YouTubePlayer, loop: Boolean) {}
}
