package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.fullscreenExample

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.aytplayersample.R

class FullscreenExampleActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_fullscreen_example)

    val youTubePlayerView = findViewById<YouTubePlayerView>(R.id.youtube_player_view)
    val fullScreenViewContainer = findViewById<FrameLayout>(R.id.full_screen_view_container)

    val iFramePlayerOptions = IFramePlayerOptions.Builder()
      .controls(1) // enable full screen button
      .fullscreen(1)
      .build()

    // we need to initialize manually in order to pass IFramePlayerOptions to the player
    youTubePlayerView.enableAutomaticInitialization = false

    youTubePlayerView.addFullScreenListener(object : FullScreenListener {
      override fun onEnterFullScreen(fullScreenView: View, exitFullScreen: Function0<Unit>) {
        // the video will continue playing in fullScreenView
        youTubePlayerView.visibility = View.GONE
        fullScreenViewContainer.visibility = View.VISIBLE
        fullScreenViewContainer.addView(fullScreenView)

        // optionally request landscape orientation
        // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
      }

      override fun onExitFullScreen() {
        // the video will continue playing in the player
        youTubePlayerView.visibility = View.VISIBLE
        fullScreenViewContainer.visibility = View.GONE
        fullScreenViewContainer.removeAllViews()
      }
    })

    youTubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
      override fun onReady(youTubePlayer: YouTubePlayer) {
        super.onReady(youTubePlayer)
        youTubePlayer.loadVideo("7NK_JOkuSVY", 0f)

        val enterFullscreenButton = findViewById<Button>(R.id.enter_fullscreen_button)
        enterFullscreenButton.setOnClickListener {
          youTubePlayer.toggleFullscreen()
        }
      }
    }, iFramePlayerOptions)

    lifecycle.addObserver(youTubePlayerView)
  }
}
