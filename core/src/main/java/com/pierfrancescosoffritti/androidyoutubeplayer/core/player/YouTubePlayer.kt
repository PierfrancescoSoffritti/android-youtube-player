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

  /** If the player is playing a playlist, play the next video. */
  fun nextVideo()
  /** If the player is playing a playlist, play the previous video. */
  fun previousVideo()
  /** If the player is playing a playlist, play the video at position [index]. */
  fun playVideoAt(index: Int)

  /** If the player is playing a playlist, enable or disable looping of the playlist. */
  fun setLoop(loop: Boolean)

  /** If the player is playing a playlist, enable or disable shuffling of the playlist. */
  fun setShuffle(shuffle: Boolean)

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

  fun setPlaybackRate(playbackRate: PlayerConstants.PlaybackRate)

  /**
   * Tries to enter or exit fullscreen in the player.
   *
   * Might require setting the `origin` parameter to "https://www.youtube.com".
   */
  fun toggleFullscreen()

  fun addListener(listener: YouTubePlayerListener): Boolean
  fun removeListener(listener: YouTubePlayerListener): Boolean
}
