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
  const val PLAYLIST_ID = "PLAYLIST_ID"
  const val PLAYLIST_INDEX = "PLAYLIST_INDEX"
  const val PLAYLIST_TYPE = "PLAYLIST_TYPE"
  const val PLAYLIST_LENGTH = "PLAYLIST_LENGTH"
  const val VIDEO_LIST = "VIDEO_LIST"
  const val LOOP_STATUS = "LOOP_STATUS"
  const val SHUFFLE_STATUS = "SHUFFLE_STATUS"

  // sender to receiver
  const val LOAD = "LOAD"
  const val LOAD_PLAYLIST = "LOAD_PLAYLIST"
  const val LOAD_PLAYLIST_OBJ = "LOAD_PLAYLIST_OBJ"
  const val CUE = "CUE"
  const val CUE_PLAYLIST = "CUE_PLAYLIST"
  const val CUE_PLAYLIST_OBJ = "CUE_PLAYLIST_OBJ"
  const val PLAY = "PLAY"
  const val PAUSE = "PAUSE"
  const val SET_VOLUME = "SET_VOLUME"
  const val SEEK_TO = "SEEK_TO"
  const val MUTE = "MUTE"
  const val UNMUTE = "UNMUTE"
  const val SET_PLAYBACK_RATE = "SET_PLAYBACK_RATE"

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
    PLAYLIST_ID to PLAYLIST_ID,
    PLAYLIST_INDEX to PLAYLIST_INDEX,
    PLAYLIST_TYPE to PLAYLIST_TYPE,
    PLAYLIST_LENGTH to PLAYLIST_LENGTH,
    VIDEO_LIST to VIDEO_LIST,
    SHUFFLE_STATUS to SHUFFLE_STATUS,
    LOOP_STATUS to LOOP_STATUS,

    LOAD to LOAD,
    LOAD_PLAYLIST to LOAD_PLAYLIST,
    LOAD_PLAYLIST_OBJ to LOAD_PLAYLIST_OBJ,
    CUE to CUE,
    CUE_PLAYLIST to CUE_PLAYLIST,
    CUE_PLAYLIST_OBJ to CUE_PLAYLIST_OBJ,
    PLAY to PLAY,
    PAUSE to PAUSE,
    SET_VOLUME to SET_VOLUME,
    SEEK_TO to SEEK_TO,
    MUTE to MUTE,
    UNMUTE to UNMUTE,
    SET_PLAYBACK_RATE to SET_PLAYBACK_RATE,
  )
}