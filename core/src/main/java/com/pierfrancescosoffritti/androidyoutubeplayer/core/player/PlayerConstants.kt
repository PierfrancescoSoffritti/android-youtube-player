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

    enum class PlaybackRate {
        UNKNOWN, RATE_0_25, RATE_0_5, RATE_1, RATE_1_5, RATE_2
    }
}