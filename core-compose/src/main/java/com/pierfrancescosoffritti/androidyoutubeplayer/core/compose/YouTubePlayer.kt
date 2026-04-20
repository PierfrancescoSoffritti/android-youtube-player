package com.pierfrancescosoffritti.androidyoutubeplayer.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener

/**
 * Compose-native entry point for the YouTube player.
 *
 * Renders the [YouTubePlayerView] owned by [state]. Because the view lives inside the
 * [rememberYouTubePlayerState] scope rather than being recreated on every recomposition, the
 * player's internal network observer is registered only once per state instance — which is the
 * Compose-safe way to avoid `ConnectivityManager$TooManyRequestsException`.
 *
 * @param state the [YouTubePlayerState] returned by [rememberYouTubePlayerState].
 * @param modifier a [Modifier] applied to the player view.
 * @param fullscreenListener optional listener registered for the lifetime of this composable.
 */
@Composable
fun YouTubePlayer(
  state: YouTubePlayerState,
  modifier: Modifier = Modifier,
  fullscreenListener: FullscreenListener? = null,
) {
  AndroidView(
    modifier = modifier,
    factory = {
      if (fullscreenListener != null) state.view.addFullscreenListener(fullscreenListener)
      state.view
    },
    onRelease = {
      if (fullscreenListener != null) state.view.removeFullscreenListener(fullscreenListener)
    },
  )
}
