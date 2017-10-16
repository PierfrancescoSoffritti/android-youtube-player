package com.pierfrancescosoffritti.youtubeplayer.youTubePlayer;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class PlayerConstants {
    public static final class PlayerState {
        public final static int UNKNOWN = -10;
        public final static int UNSTARTED = -1;
        public final static int ENDED = 0;
        public final static int PLAYING = 1;
        public final static int PAUSED = 2;
        public final static int BUFFERING = 3;
        public final static int VIDEO_CUED = 5;

        @IntDef({UNKNOWN, UNSTARTED, ENDED, PLAYING, PAUSED, BUFFERING, VIDEO_CUED})
        @Retention(RetentionPolicy.SOURCE)
        public @interface State {
        }
    }

    public static final class PlaybackQuality {
        public final static int UNKNOWN = -10;
        public final static int SMALL = 0;
        public final static int MEDIUM = 1;
        public final static int LARGE = 2;
        public final static int HD720 = 3;
        public final static int HD1080 = 4;
        public final static int HIGH_RES = 5;
        public final static int DEFAULT = -1;

        @IntDef({UNKNOWN, SMALL, MEDIUM, LARGE, HD720, HD1080, HIGH_RES, DEFAULT})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Quality {
        }
    }

    public static final class PlayerError {
        public final static int UNKNOWN = -10;
        public final static int INVALID_PARAMETER_IN_REQUEST = 0;
        public final static int HTML_5_PLAYER = 1;
        public final static int VIDEO_NOT_FOUND = 2;
        public final static int VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER = 3;

        @IntDef({UNKNOWN, INVALID_PARAMETER_IN_REQUEST, HTML_5_PLAYER, VIDEO_NOT_FOUND, VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Error {
        }
    }// @param rate 0.25, 0.5, 1, 1.5, 2

    public static final class PlaybackRate {
        public final static String UNKNOWN = "-10";
        public final static String RATE_0_25 = "0.25";
        public final static String RATE_0_5 = "0.5";
        public final static String RATE_1 = "1";
        public final static String RATE_1_5 = "1.5";
        public final static String RATE_2 = "2";

        @StringDef({UNKNOWN, RATE_0_25, RATE_0_5, RATE_1, RATE_1_5, RATE_2})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Rate {
        }
    }
}