package com.pierfrancescosoffritti.androidyoutubeplayer.player;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;

/**
 * {@link WebViewYouTubePlayer} options that correspond to the options listed here:
 * <a href="https://developers.google.com/youtube/player_parameters">IFrame player parameters</a>
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

        public Builder autoplay(int autoplay) {
            addInt(AUTOPLAY, autoplay);
            return this;
        }

        public Builder controls(int controls) {
            addInt(CONTROLS, controls);
            return this;
        }

        public Builder origin(@NonNull String origin) {
            addString(ORIGIN, origin);
            return this;
        }

        public Builder rel(int rel) {
            addInt(REL, rel);
            return this;
        }

        public Builder showInfo(int showInfo) {
            addInt(SHOW_INFO, showInfo);
            return this;
        }

        public Builder ivLoadPolicy(int ivLoadPolicy) {
            addInt(IV_LOAD_POLICY, ivLoadPolicy);
            return this;
        }

        public Builder modestBranding(int modestBranding) {
            addInt(MODEST_BRANDING, modestBranding);
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
