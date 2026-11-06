package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.composeFullscreenExample

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.pierfrancescosoffritti.androidyoutubeplayer.core.compose.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.compose.rememberYouTubePlayerState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.utils.VideoIdsProvider

/**
 * Compose fullscreen example. Toggles a fullscreen overlay attached to the host activity's decor
 * view in response to the iframe player's fullscreen button.
 */
class ComposeFullscreenExampleActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ComposeFullscreenPlayer()
    }
  }
}

@Composable
private fun ComposeFullscreenPlayer() {
  val context = LocalContext.current
  val activity = remember(context) { context.findActivity() }

  val options = remember(context) {
    IFramePlayerOptions.Builder(context)
      .controls(1)
      .fullscreen(1)
      .build()
  }

  val state = rememberYouTubePlayerState(playerOptions = options)
  val videoId = remember { VideoIdsProvider.getNextVideoId() }

  LaunchedEffect(state.player, videoId) {
    state.player?.loadVideo(videoId, 0f)
  }

  var fullscreenView by remember { mutableStateOf<View?>(null) }

  val fullscreenListener = remember(activity) {
    object : FullscreenListener {
      override fun onEnterFullscreen(view: View, exitFullscreen: () -> Unit) {
        val decor = activity?.window?.decorView as? ViewGroup ?: return
        decor.addView(
          view,
          ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
          ),
        )
        fullscreenView = view
      }

      override fun onExitFullscreen() {
        val decor = activity?.window?.decorView as? ViewGroup ?: return
        fullscreenView?.let { decor.removeView(it) }
        fullscreenView = null
      }
    }
  }

  // Scaffold applies status/navigation bar insets so the player isn't drawn under the system bars.
  // The fullscreen overlay is intentionally attached to the activity's decorView (outside the
  // Scaffold) so it covers the full screen, including the system bar area.
  Scaffold { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
    ) {
      YouTubePlayer(
        state = state,
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(16f / 9f),
        fullscreenListener = fullscreenListener,
      )
    }
  }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
  is Activity -> this
  is ContextWrapper -> baseContext.findActivity()
  else -> null
}
