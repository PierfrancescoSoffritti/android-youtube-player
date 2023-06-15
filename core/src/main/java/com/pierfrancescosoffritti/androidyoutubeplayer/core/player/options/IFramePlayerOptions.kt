package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options

import org.json.JSONException
import org.json.JSONObject

/**
 * Options used to configure the IFrame Player. All the options are listed here:
 * [IFrame player parameters](https://developers.google.com/youtube/player_parameters#Parameters)
 */
class IFramePlayerOptions private constructor(private val playerOptions: JSONObject) {

  companion object {
    val default = Builder().controls(1).build()
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
      private const val MUTE = "mute"
      private const val CONTROLS = "controls"
      private const val ENABLE_JS_API = "enablejsapi"
      private const val FS = "fs"
      internal const val ORIGIN = "origin"
      private const val REL = "rel"
      private const val SHOW_INFO = "showinfo"
      private const val IV_LOAD_POLICY = "iv_load_policy"
      private const val MODEST_BRANDING = "modestbranding"
      private const val CC_LOAD_POLICY = "cc_load_policy"
      private const val CC_LANG_PREF = "cc_lang_pref"
      private const val LIST = "list"
      private const val LIST_TYPE = "listType"
    }

    private val builderOptions = JSONObject()

    init {
      addInt(AUTO_PLAY, 0)
      addInt(MUTE, 0)
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
     * Controls if the video is played automatically after the player is initialized.
     * @param autoplay if set to 1: the player will start automatically. If set to 0: the player will not start automatically
     */
    fun autoplay(controls: Int): Builder {
      addInt(AUTO_PLAY, controls)
      return this
    }

    /**
     * Controls if the player will be initialized mute or not.
     * @param mute if set to 1: the player will start muted and without acquiring Audio Focus. If set to 0: the player will acquire Audio Focus
     */
    fun mute(controls: Int): Builder {
      addInt(MUTE, controls)
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
     * This parameter specifies the domain from which the player is running.
     * Since the player in this library is not running from a website there should be no reason to change this.
     * Using "https://www.youtube.com" (the default value) is recommended as some functions from the IFrame Player are only available
     * when the player is running on a trusted domain.
     * @param origin your domain.
     */
    fun origin(origin: String): Builder {
      addString(ORIGIN, origin)
      return this
    }

    /**
     * 	The list parameter, in conjunction with the [listType] parameter, identifies the content that will load in the player.
     * 	If the [listType] parameter value is "playlist", then the [list] parameter value specifies a YouTube playlist ID.
     * 	@param list The playlist ID to be played.
     * 	You need to prepend the playlist ID with the letters PL, for example:
     * 	if playlist id is 1234, you should pass PL1234.
     */
    fun list(list: String): Builder {
      addString(LIST, list)
      return this
    }

    /**
     * Controls if the player is playing from video IDs or from playlist IDs.
     * If set to "playlist", you should use the "list" parameter to set the playlist ID
     * to be played.
     * See original documentation for more info: https://developers.google.com/youtube/player_parameters#Selecting_Content_to_Play
     * @param listType Set to "playlist" to play playlists. Then pass the playlist id to the [list] parameter.
     */
    fun listType(listType: String): Builder {
      addString(LIST_TYPE, listType)
      return this
    }

    /**
     * Setting this parameter to 0 prevents the fullscreen button from displaying in the player.
     * See original documentation for more info: https://developers.google.com/youtube/player_parameters#Parameters
     * @param fs if set to 1: the player fullscreen button will be show. If set to 0: the player fullscreen button will not be shown.
     */
    fun fullscreen(fs: Int): Builder {
      addInt(FS, fs)
      return this
    }

    /**
     * Controls if the YouTube logo will be displayed in the control bar or not.
     * @param modestBranding If set to 1: the YouTube logo will not be displayed in the control bar.
     * If set to 0: the YouTube logo will be displayed in the control bar.
     */
    fun modestBranding(modestBranding: Int): Builder {
      addInt(MODEST_BRANDING, modestBranding)
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
