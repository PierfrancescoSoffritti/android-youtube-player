package com.pierfrancescosoffritti.androidyoutubeplayer.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

/**
 * Owns a single [YouTubePlayerView] across Compose recompositions.
 *
 * Instances must be created with [rememberYouTubePlayerState] and passed to the [YouTubePlayer]
 * composable. The underlying view is created once inside the enclosing `remember` scope and
 * released when the state leaves composition, so the internal network observer registered by the
 * player is registered at most once per state instance. This avoids the
 * `ConnectivityManager$TooManyRequestsException` that can occur when rapid recompositions recreate
 * the view and pile up `registerDefaultNetworkCallback` calls above the per-UID system limit.
 */
@Stable
class YouTubePlayerState internal constructor(
  internal val view: YouTubePlayerView,
) {
  /**
   * The underlying [YouTubePlayer] once it has become ready, or `null` before then. Reading this
   * property inside a composable triggers recomposition when the player becomes available, which
   * is useful for driving `LaunchedEffect(state.player) { ... }` patterns that load videos.
   */
  var player: YouTubePlayer? by mutableStateOf(null)
    internal set
}

/**
 * Creates and remembers a [YouTubePlayerState] whose [YouTubePlayerView] is initialized once when
 * entering composition and released on dispose.
 *
 * The returned state attaches the view to the current [androidx.lifecycle.LifecycleOwner] so that
 * the player reacts to `ON_RESUME` / `ON_STOP` events.
 *
 * @param handleNetworkEvents if `true` the player automatically reacts to network availability
 *   changes. Set this to `false` when multiple players coexist on screen to avoid registering
 *   several network observers for the same UID.
 * @param playerOptions customizable options for the embedded video player. Defaults to
 *   [IFramePlayerOptions.getDefault].
 * @param listener optional listener registered for the lifetime of the state. The listener is
 *   removed automatically on dispose.
 */
@Composable
fun rememberYouTubePlayerState(
  handleNetworkEvents: Boolean = true,
  playerOptions: IFramePlayerOptions? = null,
  listener: YouTubePlayerListener? = null,
): YouTubePlayerState {
  val context = LocalContext.current
  val lifecycle = LocalLifecycleOwner.current.lifecycle

  val state = remember {
    val view = YouTubePlayerView(context).apply {
      enableAutomaticInitialization = false
    }
    YouTubePlayerState(view)
  }

  DisposableEffect(state) {
    val options = playerOptions ?: IFramePlayerOptions.getDefault(context)

    val readyListener = object : AbstractYouTubePlayerListener() {
      override fun onReady(youTubePlayer: YouTubePlayer) {
        state.player = youTubePlayer
        state.view.removeYouTubePlayerListener(this)
      }
    }

    state.view.initialize(readyListener, handleNetworkEvents, options)
    if (listener != null) state.view.addYouTubePlayerListener(listener)
    lifecycle.addObserver(state.view)

    onDispose {
      lifecycle.removeObserver(state.view)
      if (listener != null) state.view.removeYouTubePlayerListener(listener)
      state.view.release()
      state.player = null
    }
  }

  return state
}
