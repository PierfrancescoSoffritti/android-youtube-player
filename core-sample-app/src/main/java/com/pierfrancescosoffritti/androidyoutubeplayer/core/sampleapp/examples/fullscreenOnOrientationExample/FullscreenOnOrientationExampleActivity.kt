package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.fullscreenOnOrientationExample

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.aytplayersample.R

class FullscreenOnOrientationExampleActivity: AppCompatActivity() {

    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var fullScreenViewContainer: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen_on_orientation_example)

        youTubePlayerView = findViewById(R.id.youtube_player_view)
        fullScreenViewContainer = findViewById(R.id.full_screen_view_container)

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
            }
        }, iFramePlayerOptions)

        lifecycle.addObserver(youTubePlayerView)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // if video is running in normal mode then toggle to full screen mode
            if (fullScreenViewContainer.visibility == View.GONE) {
                youTubePlayerView.toggleFullscreenMode()
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // if video is running in full screen mode then toggle to normal mode
            if (fullScreenViewContainer.visibility == View.VISIBLE) {
                youTubePlayerView.toggleFullscreenMode()
            }
        }
    }
}
