package com.pierfrancescosoffritti.androidyoutubeplayer.player.utils

import android.annotation.SuppressLint

import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.player.utils.YouTubePlayerUtils.loadOrCueVideo

/**
 * Class responsible for resuming the playback state in case of network problems.
 * eg: player is playing -> network goes out -> player stops -> network comes back -> player resumes playback automatically.
 */
internal class PlaybackResumer : AbstractYouTubePlayerListener() {

    private var isPlaying = false
    private var error: PlayerConstants.PlayerError? = null

    private var currentVideoId: String? = null
    private var currentSecond: Float = 0f

    fun resume(youTubePlayer: YouTubePlayer, canLoad: Boolean) {
        currentVideoId?.let { videoId ->
            if (isPlaying && error == PlayerConstants.PlayerError.HTML_5_PLAYER)
                youTubePlayer.loadOrCueVideo(canLoad, videoId, currentSecond)
            else if (!isPlaying && error == PlayerConstants.PlayerError.HTML_5_PLAYER)
                youTubePlayer.cueVideo(videoId, currentSecond)
        }

        error = null
    }

    override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
        when (state) {
            PlayerConstants.PlayerState.ENDED -> {
                isPlaying = false
                return
            }
            PlayerConstants.PlayerState.PAUSED -> {
                isPlaying = false
                return
            }
            PlayerConstants.PlayerState.PLAYING -> {
                isPlaying = true
                return
            }
            else -> { }
        }
    }

    override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
        if (error == PlayerConstants.PlayerError.HTML_5_PLAYER)
            this.error = error
    }

    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
        currentSecond = second
    }

    override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {
        currentVideoId = videoId
    }
}
