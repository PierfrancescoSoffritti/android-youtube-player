package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.lazyColumnExample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.pierfrancescosoffritti.androidyoutubeplayer.core.compose.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.compose.rememberYouTubePlayerState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions

/**
 * Scrolls a long list of [YouTubePlayer]s inside a `LazyColumn`. This exercises the exact scenario
 * that used to produce `ConnectivityManager$TooManyRequestsException`: as items enter and leave
 * composition the player view is created and released repeatedly, which would otherwise pile up
 * `registerDefaultNetworkCallback` calls above the per-UID system limit.
 *
 * Each item opts into `handleNetworkEvents = true` and autoplays on bind so that every scroll
 * triggers fresh network traffic — both `registerDefaultNetworkCallback` from the player and the
 * YouTube iframe's own video fetch — producing the worst-case load for the network observer
 * guards.
 */
class LazyColumnExampleActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val items = VIDEO_IDS.mapIndexed { index, id -> VideoItem(index, id) }
    setContent {
      LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items = items, key = { it.index }) { item ->
          VideoRow(item)
        }
      }
    }
  }
}

private data class VideoItem(val index: Int, val videoId: String)

// 30 distinct public video IDs used only by this stress-test screen. Kept local to avoid bloating
// the shared VideoIdsProvider.
private val VIDEO_IDS = listOf(
  "-e_3Cg9GZFU", "LvetJ9U_tVY", "S0Q4gqBUs7c", "kqSdQq5bklE", "n365C9NbbC4",
  "aqz-KE-bpKQ", "dQw4w9WgXcQ", "9bZkp7q19f0", "JGwWNGJdvx8", "kJQP7kiw5Fk",
  "CevxZvSJLk8", "hT_nvWreIhg", "RgKAFK5djSk", "OPf0YbXqDm0", "lp-EO5I60KA",
  "YykjpeuMNEk", "papuvlVeZg8", "nfWlot6h_JM", "hLQl3WQQoQ0", "60ItHLz5WEA",
  "fJ9rUzIMcZQ", "fLexgOxsZu0", "pRpeEdMmmQ0", "y6120QOlsfU", "QtXby3twMmI",
  "09R8_2nJtjg", "ktvTqknDobU", "uelHwf8o7_U", "YQHsXMglC9A", "7wtfhZwyrcc",
)

@Composable
private fun VideoRow(item: VideoItem) {
  val context = LocalContext.current
  // Autoplay needs mute=1 on mobile browsers, otherwise the iframe blocks playback without a user
  // gesture. ivLoadPolicy(3) hides annotations so the test is purely about playback traffic.
  val options = remember(context) {
    IFramePlayerOptions.Builder(context)
      .controls(0)
      .autoplay(1)
      .mute(1)
      .ivLoadPolicy(3)
      .build()
  }
  val state = rememberYouTubePlayerState(
    handleNetworkEvents = true,
    playerOptions = options,
  )

  LaunchedEffect(state.player, item.videoId) {
    state.player?.loadVideo(item.videoId, 0f)
  }

  Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
    Text(
      text = "Item #${item.index} — ${item.videoId}",
      style = MaterialTheme.typography.titleSmall,
    )
    YouTubePlayer(
      state = state,
      modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f),
    )
  }
}
