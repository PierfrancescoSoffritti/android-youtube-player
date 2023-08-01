package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.playlistExample

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.aytplayersample.R

class PlaylistExampleActivity : AppCompatActivity() {
  private var youTubePlayerView: YouTubePlayerView? = null
  private var youTubePlayer: YouTubePlayer? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_playlist_example)

    youTubePlayerView = findViewById<YouTubePlayerView>(R.id.youtube_player_view).apply {
      val iFramePlayerOptions = IFramePlayerOptions.Builder()
        .controls(1)
        .listType("playlist")
        .list(PLAYLIST_ID)
        .build()

      lifecycle.addObserver(this)
      this.initialize(
        youtubePlayerListener,
        handleNetworkEvents = true,
        iFramePlayerOptions
      )
    }

    findViewById<Button>(R.id.next_video_button).setOnClickListener {
      youTubePlayer?.nextVideo()
    }

    findViewById<Button>(R.id.previous_video_button).setOnClickListener {
      youTubePlayer?.previousVideo()
    }

    findViewById<Button>(R.id.play_second_video_button).setOnClickListener {
      youTubePlayer?.playVideoAt(1)
    }

    findViewById<Button>(R.id.shuffle_button).setOnClickListener {
      youTubePlayer?.setShuffle(true)
    }

    findViewById<Button>(R.id.loop_button).setOnClickListener {
      youTubePlayer?.setLoop(true)
    }
  }

  companion object {
    private const val PLAYLIST_ID = "PLEpEmEcrrKJUhZkyIAgQ17Oxyd3fx_y1j"
  }

  private val youtubePlayerListener = object : AbstractYouTubePlayerListener() {
    override fun onReady(youTubePlayer: YouTubePlayer) {
      this@PlaylistExampleActivity.youTubePlayer = youTubePlayer
    }
  }
}