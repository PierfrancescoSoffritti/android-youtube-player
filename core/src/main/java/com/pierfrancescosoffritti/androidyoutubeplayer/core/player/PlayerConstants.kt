package com.pierfrancescosoffritti.androidyoutubeplayer.core.player

class PlayerConstants {

  enum class PlayerState {
    UNKNOWN, UNSTARTED, ENDED, PLAYING, PAUSED, BUFFERING, VIDEO_CUED
  }

  enum class PlaybackQuality {
    UNKNOWN, SMALL, MEDIUM, LARGE, HD720, HD1080, HIGH_RES, DEFAULT
  }

  enum class PlayerError {
    UNKNOWN,
    INVALID_PARAMETER_IN_REQUEST,
    HTML_5_PLAYER,
    VIDEO_NOT_FOUND,
    VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER,
    REQUEST_MISSING_HTTP_REFERER
  }

  enum class PlaybackRate {
    UNKNOWN, RATE_0_25, RATE_0_5, RATE_0_75, RATE_1, RATE_1_25, RATE_1_5, RATE_1_75, RATE_2
  }
}

fun PlayerConstants.PlaybackRate.toFloat(): Float {
  return when (this) {
    PlayerConstants.PlaybackRate.UNKNOWN -> 1f
    PlayerConstants.PlaybackRate.RATE_0_25 -> 0.25f
    PlayerConstants.PlaybackRate.RATE_0_5 -> 0.5f
    PlayerConstants.PlaybackRate.RATE_0_75 -> 0.75f
    PlayerConstants.PlaybackRate.RATE_1 -> 1f
    PlayerConstants.PlaybackRate.RATE_1_25 -> 1.25f
    PlayerConstants.PlaybackRate.RATE_1_5 -> 1.5f
    PlayerConstants.PlaybackRate.RATE_1_75 -> 1.75f
    PlayerConstants.PlaybackRate.RATE_2 -> 2f
  }
}