package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.youtube

import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.utils.JSONUtils

/**
 * Set of constants used for sender-receiver communication. The sender will send the constants to the receiver when a connection is initiated.
 */
internal object ChromecastCommunicationConstants {
  // receiver to sender
  const val INIT_COMMUNICATION_CONSTANTS = "INIT_COMMUNICATION_CONSTANTS"

  const val IFRAME_API_READY = "IFRAME_API_READY"
  const val READY = "READY"
  const val STATE_CHANGED = "STATE_CHANGED"
  const val PLAYBACK_QUALITY_CHANGED = "PLAYBACK_QUALITY_CHANGED"
  const val PLAYBACK_RATE_CHANGED = "PLAYBACK_RATE_CHANGED"
  const val ERROR = "ERROR"
  const val API_CHANGED = "API_CHANGED"
  const val VIDEO_CURRENT_TIME = "VIDEO_CURRENT_TIME"
  const val VIDEO_DURATION = "VIDEO_DURATION"
  const val VIDEO_ID = "VIDEO_ID"

  // sender to receiver
  const val LOAD = "LOAD"
  const val CUE = "CUE"
  const val PLAY = "PLAY"
  const val PAUSE = "PAUSE"
  const val SET_VOLUME = "SET_VOLUME"
  const val SEEK_TO = "SEEK_TO"
  const val MUTE = "MUTE"
  const val UNMUTE = "UNMUTE"
  const val SET_PLAYBACK_RATE = "SET_PLAYBACK_RATE"
  const val PLAY_NEXT_VIDEO = "PLAY_NEXT_VIDEO"
  const val PLAY_PREVIOUS_VIDEO = "PLAY_PREVIOUS_VIDEO"
  const val PLAY_VIDEO_AT = "PLAY_VIDEO_AT"
  const val SET_LOOP = "SET_LOOP"
  const val SET_SHUFFLE = "SET_SHUFFLE"

  fun asJson() = JSONUtils.buildFlatJson(
    IFRAME_API_READY to IFRAME_API_READY,
    READY to READY,
    STATE_CHANGED to STATE_CHANGED,
    PLAYBACK_QUALITY_CHANGED to PLAYBACK_QUALITY_CHANGED,
    PLAYBACK_RATE_CHANGED to PLAYBACK_RATE_CHANGED,
    ERROR to ERROR,
    API_CHANGED to API_CHANGED,
    VIDEO_CURRENT_TIME to VIDEO_CURRENT_TIME,
    VIDEO_DURATION to VIDEO_DURATION,
    VIDEO_ID to VIDEO_ID,

    LOAD to LOAD,
    CUE to CUE,
    PLAY to PLAY,
    PAUSE to PAUSE,
    SET_VOLUME to SET_VOLUME,
    SEEK_TO to SEEK_TO,
    MUTE to MUTE,
    UNMUTE to UNMUTE,
    SET_PLAYBACK_RATE to SET_PLAYBACK_RATE,
  )
}