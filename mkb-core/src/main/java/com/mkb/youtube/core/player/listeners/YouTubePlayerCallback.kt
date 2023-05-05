package com.mkb.youtube.core.player.listeners

import com.mkb.youtube.core.player.YouTubePlayer

interface YouTubePlayerCallback {
  fun onYouTubePlayer(youTubePlayer: YouTubePlayer)
}