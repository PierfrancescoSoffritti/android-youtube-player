package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.completeExample

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlaybackRate
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.isMuted
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.utils.VideoIdsProvider
import com.pierfrancescosoffritti.aytplayersample.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CompleteExampleActivity : AppCompatActivity() {
  private lateinit var youTubePlayerView: YouTubePlayerView
  private var youTubePlayer: YouTubePlayer? = null
  private var isFullscreen = false

  private lateinit var fullscreenViewContainer: ViewGroup
  private lateinit var playerUiContainer: View

  private lateinit var playNextVideoButton: Button
  private lateinit var playbackSpeedTextView: TextView

  private lateinit var muteButton: Button
  private lateinit var muteTextView: TextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_complete_example)
    youTubePlayerView = findViewById(R.id.youtube_player_view)

    fullscreenViewContainer = findViewById(R.id.full_screen_view_container)
    playerUiContainer = findViewById(R.id.player_ui_container)
    playNextVideoButton = findViewById(R.id.next_video_button)
    playbackSpeedTextView = findViewById(R.id.playback_speed_text_view)
    muteButton = findViewById(R.id.mute_button)
    muteTextView = findViewById(R.id.mute_text_view)

    initYouTubePlayerView(youTubePlayerView)
  }

  private fun initYouTubePlayerView(youTubePlayerView: YouTubePlayerView) {
    // The player will automatically release itself when the activity is destroyed.
    // The player will automatically pause when the activity is stopped
    // If you don't add YouTubePlayerView as a lifecycle observer, you will have to release it manually.
    lifecycle.addObserver(youTubePlayerView)

    youTubePlayerView.addFullscreenListener(object : FullscreenListener {
      override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
        isFullscreen = true

        // the video will continue playing in fullscreenView
        playerUiContainer.visibility = View.GONE
        fullscreenViewContainer.visibility = View.VISIBLE
        fullscreenViewContainer.addView(fullscreenView)

        if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
          requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        }
      }

      override fun onExitFullscreen() {
        isFullscreen = false

        // the video will continue playing in the player
        playerUiContainer.visibility = View.VISIBLE
        fullscreenViewContainer.visibility = View.GONE
        fullscreenViewContainer.removeAllViews()

        if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_SENSOR) {
          requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
          requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        }
      }
    })

    val youTubePlayerListener = object : AbstractYouTubePlayerListener() {
      override fun onReady(youTubePlayer: YouTubePlayer) {
        this@CompleteExampleActivity.youTubePlayer = youTubePlayer
        youTubePlayer.loadOrCueVideo(lifecycle, VideoIdsProvider.getNextVideoId(), 0f)
        setPlayNextVideoButtonClickListener(youTubePlayer)
        setPlaybackSpeedButtonsClickListeners(youTubePlayer)
        setMuteButtonClickListener(youTubePlayer)
      }

      override fun onPlaybackRateChange(youTubePlayer: YouTubePlayer, playbackRate: PlaybackRate) {
        playbackSpeedTextView.text = "Playback speed: $playbackRate"
      }
    }

    val iFramePlayerOptions = IFramePlayerOptions.Builder(applicationContext)
      .controls(1)
      .fullscreen(1) // enable full screen button
      .build()

    youTubePlayerView.enableAutomaticInitialization = false
    youTubePlayerView.initialize(youTubePlayerListener, iFramePlayerOptions)
  }

  /**
   * Set a click listener on the "Play next video" button
   */
  private fun setPlayNextVideoButtonClickListener(youTubePlayer: YouTubePlayer) {
    playNextVideoButton.setOnClickListener {
      youTubePlayer.loadOrCueVideo(lifecycle, VideoIdsProvider.getNextVideoId(), 0f)
    }
  }

  /**
   * Set the click listeners for the "playback speed" buttons
   */
  private fun setPlaybackSpeedButtonsClickListeners(youTubePlayer: YouTubePlayer) {
    val playbackSpeed_0_25 = findViewById<Button>(R.id.playback_speed_0_25)
    val playbackSpeed_1 = findViewById<Button>(R.id.playback_speed_1)
    val playbackSpeed_2 = findViewById<Button>(R.id.playback_speed_2)
    playbackSpeed_0_25.setOnClickListener { youTubePlayer.setPlaybackRate(PlaybackRate.RATE_0_25) }
    playbackSpeed_1.setOnClickListener { youTubePlayer.setPlaybackRate(PlaybackRate.RATE_1) }
    playbackSpeed_2.setOnClickListener { youTubePlayer.setPlaybackRate(PlaybackRate.RATE_2) }
  }

  /**
   * Set the click listener for the "mute" button and launches a coroutine to update the mute status
   */
  private fun setMuteButtonClickListener(youTubePlayer: YouTubePlayer) {
    lifecycleScope.launch {
      while (isActive) {
        val isMute = youTubePlayer.isMuted()
        if (isMute) {
          muteTextView.text = "Muted"
        } else {
          muteTextView.text = "Unmuted"
        }
        delay(100)
      }
    }

    muteButton.setOnClickListener {
      lifecycleScope.launch {
        val isMute = youTubePlayer.isMuted()
        if (isMute) {
          youTubePlayer.unMute()
        } else {
          youTubePlayer.mute()
        }
      }
    }
  }
}