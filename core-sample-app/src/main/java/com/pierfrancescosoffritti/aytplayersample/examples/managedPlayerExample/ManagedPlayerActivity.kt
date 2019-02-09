package com.pierfrancescosoffritti.aytplayersample.examples.managedPlayerExample

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.aytplayersample.R
import com.pierfrancescosoffritti.aytplayersample.utils.FullScreenHelper
import com.pierfrancescosoffritti.aytplayersample.utils.VideoIdsProvider
import kotlinx.android.synthetic.main.activity_managed_player_example.*

class ManagedPlayerActivity : AppCompatActivity() {

    private val fullScreenHelper = FullScreenHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_managed_player_example)

        Log.d(javaClass.simpleName, "started")

        lifecycle.addObserver(youtube_player_view)

        addFullScreenListenerToPlayer()

        youtube_player_view.addListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                next_video_button.setOnClickListener { youTubePlayer.loadVideo(VideoIdsProvider.getNextVideoId(), 0f) }
            }

            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                if(state == PlayerConstants.PlayerState.ENDED)
                    youTubePlayer.loadVideo(VideoIdsProvider.getNextVideoId(), 0f)
            }
        })
    }

    private fun addFullScreenListenerToPlayer() {
        youtube_player_view.addFullScreenListener(object : YouTubePlayerFullScreenListener {
            override fun onYouTubePlayerEnterFullScreen() {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                fullScreenHelper.enterFullScreen()
            }

            override fun onYouTubePlayerExitFullScreen() {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                fullScreenHelper.exitFullScreen()
            }
        })
    }
}
