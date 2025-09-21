package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.fullscreenExample

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.utils.VideoIdsProvider
import com.pierfrancescosoffritti.aytplayersample.R

class FullscreenExampleActivity : AppCompatActivity() {

  private lateinit var youTubePlayer: YouTubePlayer

  private var isFullscreen = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_fullscreen_example)

    val youTubePlayerView = findViewById<YouTubePlayerView>(R.id.youtube_player_view)
    val fullscreenViewContainer = findViewById<FrameLayout>(R.id.full_screen_view_container)

    val iFramePlayerOptions = IFramePlayerOptions.Builder(applicationContext)
      .controls(1)
      .fullscreen(1) // enable full screen button
      .build()

    // we need to initialize manually in order to pass IFramePlayerOptions to the player
    youTubePlayerView.enableAutomaticInitialization = false

    youTubePlayerView.addFullscreenListener(object : FullscreenListener {
      override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
        isFullscreen = true

        // the video will continue playing in fullscreenView
        youTubePlayerView.visibility = View.GONE
        fullscreenViewContainer.visibility = View.VISIBLE
        fullscreenViewContainer.addView(fullscreenView)

        // optionally request landscape orientation
        // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
      }

      override fun onExitFullscreen() {
        isFullscreen = false

        // the video will continue playing in the player
        youTubePlayerView.visibility = View.VISIBLE
        fullscreenViewContainer.visibility = View.GONE
        fullscreenViewContainer.removeAllViews()
      }
    })

    youTubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
      override fun onReady(youTubePlayer: YouTubePlayer) {
        this@FullscreenExampleActivity.youTubePlayer = youTubePlayer
        youTubePlayer.loadVideo(VideoIdsProvider.getNextVideoId(), 0f)
      }
    }, iFramePlayerOptions)

    lifecycle.addObserver(youTubePlayerView)
  }
}
