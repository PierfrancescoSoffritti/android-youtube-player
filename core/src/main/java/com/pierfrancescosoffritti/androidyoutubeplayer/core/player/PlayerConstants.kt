package com.pierfrancescosoffritti.androidyoutubeplayer.core.player

class PlayerConstants {

  enum class PlayerState {
    UNKNOWN, UNSTARTED, ENDED, PLAYING, PAUSED, BUFFERING, VIDEO_CUED
  }

  enum class PlaybackQuality {
    UNKNOWN, SMALL, MEDIUM, LARGE, HD720, HD1080, HIGH_RES, DEFAULT
  }

  enum class PlayerError {
    UNKNOWN, INVALID_PARAMETER_IN_REQUEST, HTML_5_PLAYER, VIDEO_NOT_FOUND, VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER
  }

  sealed class PlaybackRate(val rate: Float) {
    object UNKNOWN : PlaybackRate(0f)
    object RATE_0_25 : PlaybackRate(0.25f)
    object RATE_0_5 : PlaybackRate(0.5f)
    object RATE_0_75 : PlaybackRate(0.75f)
    object RATE_1 : PlaybackRate(1f)
    object RATE_1_25 : PlaybackRate(1.25f)
    object RATE_1_5 : PlaybackRate(1.5f)
    object RATE_1_75 : PlaybackRate(1.75f)
    object RATE_2 : PlaybackRate(2f)

    class Custom(rate: Float): PlaybackRate(rate)
  }
}

fun PlayerConstants.PlaybackRate.toFloat(): Float {
  return this.rate
}