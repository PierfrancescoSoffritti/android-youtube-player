package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options

import org.json.JSONException
import org.json.JSONObject

/**
 * Options used to configure the IFrame Player. All the options are listed here:
 * [IFrame player parameters](https://developers.google.com/youtube/player_parameters#Parameters)
 */
class IFramePlayerOptions private constructor(private val playerOptions: JSONObject) {

    companion object {
        val default = Builder().build()
    }

    override fun toString(): String {
        return playerOptions.toString()
    }

    internal fun getOrigin(): String {
        return playerOptions.getString(Builder.ORIGIN)
    }

    class Builder {
        companion object {
            private const val AUTO_PLAY = "autoplay"
            private const val CONTROLS = "controls"
            private const val ENABLE_JS_API = "enablejsapi"
            private const val FS = "fs"
            const val ORIGIN = "origin"
            private const val REL = "rel"
            private const val SHOW_INFO = "showinfo"
            private const val IV_LOAD_POLICY = "iv_load_policy"
            private const val MODEST_BRANDING = "modestbranding"
            private const val CC_LOAD_POLICY = "cc_load_policy"
            private const val CC_LANG_PREF = "cc_lang_pref"
        }

        private val builderOptions = JSONObject()

        init {
            addInt(AUTO_PLAY, 0)
            addInt(CONTROLS, 0)
            addInt(ENABLE_JS_API, 1)
            addInt(FS, 0)
            addString(ORIGIN, "https://www.youtube.com")
            addInt(REL, 0)
            addInt(SHOW_INFO, 0)
            addInt(IV_LOAD_POLICY, 3)
            addInt(MODEST_BRANDING, 1)
            addInt(CC_LOAD_POLICY, 0)
        }

        fun build(): IFramePlayerOptions {
            return IFramePlayerOptions(builderOptions)
        }

        /**
         * Controls whether the web-based UI of the IFrame player is used or not.
         * @param controls If set to 0: web UI is not used. If set to 1: web UI is used.
         */
        fun controls(controls: Int): Builder {
            addInt(CONTROLS, controls)
            return this
        }

        /**
         * Controls the related videos shown at the end of a video.
         * @param rel If set to 0: related videos will come from the same channel as the video that was just played. If set to 1: related videos will come from multiple channels.
         */
        fun rel(rel: Int): Builder {
            addInt(REL, rel)
            return this
        }

        /**
         * Controls video annotations.
         * @param ivLoadPolicy if set to 1: the player will show video annotations. If set to 3: they player won't show video annotations.
         */
        fun ivLoadPolicy(ivLoadPolicy: Int): Builder {
            addInt(IV_LOAD_POLICY, ivLoadPolicy)
            return this
        }

        /**
         *  This parameter specifies the default language that the player will use to display captions.
         *  If you use this parameter and also set the cc_load_policy parameter to 1, then the player
         *  will show captions in the specified language when the player loads.
         *  If you do not also set the cc_load_policy parameter, then captions will not display by default,
         *  but will display in the specified language if the user opts to turn captions on.
         *
         * @param languageCode ISO 639-1 two-letter language code
         */
        fun langPref(languageCode: String): Builder {
            addString(CC_LANG_PREF, languageCode)
            return this
        }


        /**
         * Controls video captions. It doesn't work with automatically generated captions.
         * @param ccLoadPolicy if set to 1: the player will show captions. If set to 0: the player won't show captions.
         */
        fun ccLoadPolicy(ccLoadPolicy: Int): Builder {
            addInt(CC_LOAD_POLICY, ccLoadPolicy)
            return this
        }

        /**
         * Controls domain as the origin parameter value.
         * @param origin your domain
         */
        fun origin(origin: String): Builder {
            addString(ORIGIN, origin)
            return this
        }

        private fun addString(key: String, value: String) {
            try {
                builderOptions.put(key, value)
            } catch (e: JSONException) {
                throw RuntimeException("Illegal JSON value $key: $value")
            }

        }

        private fun addInt(key: String, value: Int) {
            try {
                builderOptions.put(key, value)
            } catch (e: JSONException) {
                throw RuntimeException("Illegal JSON value $key: $value")
            }

        }
    }
}
