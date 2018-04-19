package com.pierfrancescosoffritti.youtubeplayer.player;

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

        /**
         * UNKNOWN = -10 <br/>
         * UNSTARTED = -1 <br/>
         * ENDED = 0 <br/>
         * PLAYING = 1 <br/>
         * PAUSED = 2 <br/>
         * BUFFERING = 3 <br/>
         * VIDEO_CUED = 5
         */
        @IntDef({UNKNOWN, UNSTARTED, ENDED, PLAYING, PAUSED, BUFFERING, VIDEO_CUED})
        @Retention(RetentionPolicy.SOURCE)
        public @interface State {
        }
    }

    public static final class PlaybackQuality {
        public final static String UNKNOWN = "unknown";
        public final static String SMALL = "small";
        public final static String MEDIUM = "medium";
        public final static String LARGE = "large";
        public final static String HD720 = "hd720";
        public final static String HD1080 = "hd1080";
        public final static String HIGH_RES = "highres";
        public final static String DEFAULT = "default";

        /**
         * UNKNOWN = "unknown" <br/>
         * SMALL = "small" <br/>
         * MEDIUM = "medium" <br/>
         * LARGE = "large" <br/>
         * HD720 = "hd720" <br/>
         * HD1080 = "hd1080" <br/>
         * HIGH_RES = "highres" <br/>
         * DEFAULT = "default"
         */
        @StringDef({UNKNOWN, SMALL, MEDIUM, LARGE, HD720, HD1080, HIGH_RES, DEFAULT})
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

        /**
         * UNKNOWN = -10 <br/>
         * INVALID_PARAMETER_IN_REQUEST = 0 <br/>
         * HTML_5_PLAYER = 1 <br/>
         * VIDEO_NOT_FOUND = 2 <br/>
         * VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER = 3 <br/>
         */
        @IntDef({UNKNOWN, INVALID_PARAMETER_IN_REQUEST, HTML_5_PLAYER, VIDEO_NOT_FOUND, VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Error {
        }
    }

    public static final class PlaybackRate {
        public final static String UNKNOWN = "-10";
        public final static String RATE_0_25 = "0.25";
        public final static String RATE_0_5 = "0.5";
        public final static String RATE_1 = "1";
        public final static String RATE_1_5 = "1.5";
        public final static String RATE_2 = "2";

        /**
         * UNKNOWN = "-10" <br/>
         * RATE_0_25 = "0.25" <br/>
         * RATE_0_5 = "0.5" <br/>
         * RATE_1 = "1" <br/>
         * RATE_1_5 = "1.5" <br/>
         * RATE_2 = "2"
         */
        @StringDef({UNKNOWN, RATE_0_25, RATE_0_5, RATE_1, RATE_1_5, RATE_2})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Rate {
        }
    }
}