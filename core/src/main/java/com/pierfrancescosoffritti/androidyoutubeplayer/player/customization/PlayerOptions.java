package com.pierfrancescosoffritti.androidyoutubeplayer.player.customization;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * {@link WebViewYouTubePlayer} options that correspond to the options listed here:
 * https://developers.google.com/youtube/player_parameters
 */
public class PlayerOptions {

    private final JSONObject playerOptions;

    private PlayerOptions(JSONObject options) {
        playerOptions = options;
    }

    @Override
    public String toString() {
        return playerOptions.toString();
    }

    public static PlayerOptions getDefault() {
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
            addInt(MODEST_BRANDING, 0);
        }

        public Builder autoplay(int autoplay) {
            addInt(AUTOPLAY, autoplay);
            return this;
        }

        public Builder controls(int controls) {
            addInt(CONTROLS, controls);
            return this;
        }

        public Builder enableJSApi(int enableJSApi) {
            addInt(ENABLE_JS_API, enableJSApi);
            return this;
        }

        public Builder fs(int fs) {
            addInt(FS, fs);
            return this;
        }

        public Builder origin(String origin) {
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
        
        private void addString(String key, String value) {
            try {
                builderOptions.put(key, value);
            } catch (JSONException e) {
                throw new RuntimeException("Illegal JSON value");
            }
        }
        
        private void addInt(String key, int value) {
            try {
                builderOptions.put(key, value);
            } catch (JSONException e) {
                throw new RuntimeException("Illegal JSON value");
            }
        }


        public PlayerOptions build() {
            return new PlayerOptions(builderOptions);
        }
    }
}
