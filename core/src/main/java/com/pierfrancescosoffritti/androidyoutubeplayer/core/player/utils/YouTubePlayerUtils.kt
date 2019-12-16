@file:JvmName("YouTubePlayerUtils")

package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils

import androidx.lifecycle.Lifecycle
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer

/**
 * Calls [YouTubePlayer.cueVideo] or [YouTubePlayer.loadVideo] depending on which one is more appropriate.
 * If it can't decide, calls [YouTubePlayer.cueVideo] by default.
 *
 * In most cases you want to avoid calling [YouTubePlayer.loadVideo] if the Activity/Fragment is not in the foreground.
 * This function automates these checks for you.
 * @param lifecycle the lifecycle of the Activity or Fragment containing the YouTubePlayerView.
 * @param videoId id of the video.
 * @param startSeconds the time from which the video should start playing.
 */
@JvmOverloads
fun YouTubePlayer.loadOrCueVideo(
    lifecycle: Lifecycle,
    videoId: String,
    startSeconds: Float,
    suggestedQuality: PlayerConstants.PlaybackQuality = PlayerConstants.PlaybackQuality.DEFAULT
) {
    loadOrCueVideo(lifecycle.currentState == Lifecycle.State.RESUMED, videoId, startSeconds, suggestedQuality)
}


@JvmSynthetic
@JvmOverloads
internal fun YouTubePlayer.loadOrCueVideo(
    canLoad: Boolean,
    videoId: String,
    startSeconds: Float,
    suggestedQuality: PlayerConstants.PlaybackQuality = PlayerConstants.PlaybackQuality.DEFAULT
) {
    if (canLoad)
        loadVideo(videoId, startSeconds, suggestedQuality)
    else
        cueVideo(videoId, startSeconds, suggestedQuality)
}