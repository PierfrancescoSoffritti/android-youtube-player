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

  /**
   * @param videos The required playlist parameter specifies an array of YouTube video IDs. In the YouTube Data API, the video resource's id property identifies that video's ID.
   * @param index Start index. The optional index property specifies the index of the first video in the list that will play. The parameter uses a zero-based index, and the default parameter value is 0, so the default behavior is to load and play the first video in the list. If the index is out of range, youtube will only play the first video on the playlist, then stop.
   * @param startSeconds The optional startSeconds property accepts a float/integer and specifies the time from which the first video in the list should start playing when the playVideo() function is called. If you specify a startSeconds value and then call seekTo(), then the player plays from the time specified in the seekTo() call. If you cue a list and then call the playVideoAt() function, the player will start playing at the beginning of the specified video.
   */
  fun loadPlaylist(videos: List<String>, index: Int, startSeconds: Float)
  /**
   * @param index Start index. The optional index property specifies the index of the first video in the list that will play. The parameter uses a zero-based index, and the default parameter value is 0, so the default behavior is to load and play the first video in the list. If the index is out of range, youtube will only play the first video on the playlist, then stop.
   * @param list The required list property contains a key that identifies the particular list of videos that YouTube should return.
   * @param listType If the listType property value is playlist, then the list property specifies the playlist ID or an array of video IDs. In the YouTube Data API, the playlist resource's id property identifies a playlist's ID, and the video resource's id property specifies a video ID. If the listType property value is user_uploads, then the list property identifies the user whose uploaded videos will be returned.
   * @param startSeconds The optional startSeconds property accepts a float/integer and specifies the time from which the first video in the list should start playing when the playVideo() function is called. If you specify a startSeconds value and then call seekTo(), then the player plays from the time specified in the seekTo() call. If you cue a list and then call the playVideoAt() function, the player will start playing at the beginning of the specified video.
   */
  fun loadPlaylist(list: String, listType: String, index: Int, startSeconds: Float)

  /**
   * @param videos The required playlist parameter specifies an array of YouTube video IDs. In the YouTube Data API, the video resource's id property identifies that video's ID.
   * @param index Start index. The optional index property specifies the index of the first video in the list that will play. The parameter uses a zero-based index, and the default parameter value is 0, so the default behavior is to load and play the first video in the list. If the index is out of range, youtube will only play the first video on the playlist, then stop.
   * @param startSeconds The optional startSeconds property accepts a float/integer and specifies the time from which the first video in the list should start playing when the playVideo() function is called. If you specify a startSeconds value and then call seekTo(), then the player plays from the time specified in the seekTo() call. If you cue a list and then call the playVideoAt() function, the player will start playing at the beginning of the specified video.
   */
  fun cuePlaylist(videos: List<String>, index: Int, startSeconds: Float)
  /**
   * @param index Start index. The optional index property specifies the index of the first video in the list that will play. The parameter uses a zero-based index, and the default parameter value is 0, so the default behavior is to load and play the first video in the list. If the index is out of range, youtube will only play the first video on the playlist, then stop.
   * @param list The required list property contains a key that identifies the particular list of videos that YouTube should return.
   * @param listType If the listType property value is playlist, then the list property specifies the playlist ID or an array of video IDs. In the YouTube Data API, the playlist resource's id property identifies a playlist's ID, and the video resource's id property specifies a video ID.
  If the listType property value is user_uploads, then the list property identifies the user whose uploaded videos will be returned.
   * @param startSeconds The optional startSeconds property accepts a float/integer and specifies the time from which the first video in the list should start playing when the playVideo() function is called. If you specify a startSeconds value and then call seekTo(), then the player plays from the time specified in the seekTo() call. If you cue a list and then call the playVideoAt() function, the player will start playing at the beginning of the specified video.
   */
  fun cuePlaylist(list: String, listType: String, index: Int, startSeconds: Float)

  /**
   * This function indicates whether the video player should continuously play a playlist or if it should stop playing after the last video in the playlist ends. The default behavior is that playlists do not loop.
   * This setting will persist even if you load or cue a different playlist, which means that if you load a playlist, call the setLoop function with a value of true, and then load a second playlist, the second playlist will also loop.
   * @param loop If the parameter value is true, then the video player will continuously play playlists. After playing the last video in a playlist, the video player will go back to the beginning of the playlist and play it again. If the parameter value is false, then playbacks will end after the video player plays the last video in a playlist.
   */
  fun setLoop(loop: Boolean)

  /**
   * This function indicates whether a playlist's videos should be shuffled so that they play back in an order different from the one that the playlist creator designated. If you shuffle a playlist after it has already started playing, the list will be reordered while the video that is playing continues to play. The next video that plays will then be selected based on the reordered list.
   * This setting will not persist if you load or cue a different playlist, which means that if you load a playlist, call the setShuffle function, and then load a second playlist, the second playlist will not be shuffled.
   * @param shuffle If the parameter value is true, then YouTube will shuffle the playlist order. If you instruct the function to shuffle a playlist that has already been shuffled, YouTube will shuffle the order again. If the parameter value is false, then YouTube will change the playlist order back to its original order.
   */
  fun setShuffle(shuffle: Boolean)

  /**
   * If player.nextVideo() is called while the last video in the playlist is being watched, and the playlist is set to play continuously (loop), then the player will load and play the first video in the list.
   * If player.nextVideo() is called while the last video in the playlist is being watched, and the playlist is not set to play continuously, then playback will end.
   */
  fun nextVideo()

  /**
   * If player.previousVideo() is called while the first video in the playlist is being watched, and the playlist is set to play continuously (loop), then the player will load and play the last video in the list.
   * If player.previousVideo() is called while the first video in the playlist is being watched, and the playlist is not set to play continuously, then the player will restart the first playlist video from the beginning.
   */
  fun previousVideo()

  /**
   * This function loads and plays the specified video in the playlist.
   * @param index The required index parameter specifies the index of the video that you want to play in the playlist. The parameter uses a zero-based index, so a value of 0 identifies the first video in the list. If you have shuffled the playlist, this function will play the video at the specified position in the shuffled playlist.
   */
  fun playVideoAt(index: Int)

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
