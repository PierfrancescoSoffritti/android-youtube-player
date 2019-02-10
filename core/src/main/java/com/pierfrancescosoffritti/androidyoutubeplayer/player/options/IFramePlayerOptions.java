package com.pierfrancescosoffritti.androidyoutubeplayer.player.options;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;

/**
 * Options used to configure the IFrame Player. All the options are listed here:
 * <a href="https://developers.google.com/youtube/player_parameters#Parameters">IFrame player parameters</a>
 */
public class IFramePlayerOptions {

    private final JSONObject playerOptions;

    private IFramePlayerOptions(JSONObject options) {
        playerOptions = options;
    }

    @Override
    public String toString() {
        return playerOptions.toString();
    }

    public static IFramePlayerOptions getDefault() {
        return new Builder().build();
    }

    public static class Builder {

        private final JSONObject builderOptions = new JSONObject();

        private static final String AUTOPLAY = "autoplay";
        private static final String CONTROLS = "controls";
        private static final String ENABLE_JS_API = "enablejsapi";
        private static final String FS = "fs";
        private static final String ORIGIN = "origin";
        private static final String REL = "rel";
        private static final String SHOW_INFO = "showinfo";
        private static final String IV_LOAD_POLICY = "iv_load_policy";
        private static final String MODEST_BRANDING = "modestbranding";

        public Builder() {
            addInt(AUTOPLAY, 0);
            addInt(CONTROLS, 0);
            addInt(ENABLE_JS_API, 1);
            addInt(FS, 0);
            addString(ORIGIN, "https://www.youtube.com");
            addInt(REL, 0);
            addInt(SHOW_INFO, 0);
            addInt(IV_LOAD_POLICY, 3);
            addInt(MODEST_BRANDING, 1);
        }

        /**
         * Controls whether the web-based Ui of the IFrame player is used or not.
         * @param controls If set to 0: web Ui is not used. If set to 1: web Ui is used.
         */
        public Builder controls(int controls) {
            addInt(CONTROLS, controls);
            return this;
        }

        /**
         * Controls the related videos shown at the end of a video.
         * @param rel If set to 0, related videos will come from the same channel as the video that was just played. If set to 1, related videos will come from multiple channels.
         */
        public Builder rel(int rel) {
            addInt(REL, rel);
            return this;
        }

        /**
         * Controls video annotations.
         * @param ivLoadPolicy if set to 1: causes video annotations to be shown by default. If set to 3 causes video annotations to not be shown by default.
         */
        public Builder ivLoadPolicy(int ivLoadPolicy) {
            addInt(IV_LOAD_POLICY, ivLoadPolicy);
            return this;
        }
        
        private void addString(@NonNull String key, @NonNull String value) {
            try {
                builderOptions.put(key, value);
            } catch (JSONException e) {
                throw new RuntimeException("Illegal JSON value " +key +": " +value);
            }
        }
        
        private void addInt(@NonNull String key, int value) {
            try {
                builderOptions.put(key, value);
            } catch (JSONException e) {
                throw new RuntimeException("Illegal JSON value " +key +": " +value);
            }
        }

        public IFramePlayerOptions build() {
            return new IFramePlayerOptions(builderOptions);
        }
    }
}
