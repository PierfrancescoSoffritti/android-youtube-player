package com.pierfrancescosoffritti.androidyoutubeplayer.player;

public final class PlayerConstants {

    public enum PlayerState {
        UNKNOWN, UNSTARTED, ENDED, PLAYING, PAUSED, BUFFERING, VIDEO_CUED
    }

    public enum PlaybackQuality {
        UNKNOWN, SMALL, MEDIUM, LARGE, HD720, HD1080, HIGH_RES, DEFAULT
    }

    public enum PlayerError {
        UNKNOWN, INVALID_PARAMETER_IN_REQUEST, HTML_5_PLAYER, VIDEO_NOT_FOUND, VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER
    }

    public enum PlaybackRate {
        UNKNOWN, RATE_0_25, RATE_0_5, RATE_1, RATE_1_5, RATE_2
    }
}