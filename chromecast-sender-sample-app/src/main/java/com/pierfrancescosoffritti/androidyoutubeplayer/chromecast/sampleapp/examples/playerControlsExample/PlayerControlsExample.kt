package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.examples.playerControlsExample

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.mediarouter.app.MediaRouteButton
import com.google.android.gms.cast.framework.CastContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.ChromecastYouTubePlayerContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.infrastructure.ChromecastConnectionListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.cyplayersample.R
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils.MediaRouteButtonUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.utils.PlayServicesUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils.VideoIdsProvider
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.ui.SimpleChromeCastUiController

@SuppressLint("SetTextI18n")
class PlayerControlsExample : AppCompatActivity() {

    private val googlePlayServicesAvailabilityRequestCode = 1

    private lateinit var mediaRouteButton: MediaRouteButton
    private lateinit var chromecastControlsRoot: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_controls_example)

        mediaRouteButton = findViewById(R.id.media_route_button)
        chromecastControlsRoot = findViewById(R.id.chromecast_controls_root)

        MediaRouteButtonUtils.initMediaRouteButton(mediaRouteButton)

        // can't use CastContext until I'm sure the user has GooglePlayServices
        PlayServicesUtils.checkGooglePlayServicesAvailability(this, googlePlayServicesAvailabilityRequestCode, Runnable { initChromeCast() })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // can't use CastContext until I'm sure the user has GooglePlayServices
        if(requestCode == googlePlayServicesAvailabilityRequestCode)
            PlayServicesUtils.checkGooglePlayServicesAvailability(this, googlePlayServicesAvailabilityRequestCode, Runnable { initChromeCast() })
    }

    private fun initChromeCast() {
        ChromecastYouTubePlayerContext(CastContext.getSharedInstance(this).sessionManager, SimpleChromeCastConnectionListener())
    }

    inner class SimpleChromeCastConnectionListener : ChromecastConnectionListener {

        private val chromeCastUiController = SimpleChromeCastUiController(chromecastControlsRoot)
        private val chromeCastConnectionStatusTextView = chromecastControlsRoot.findViewById<TextView>(R.id.chromecast_connection_status)!!
        private val playerStatusTextView = chromecastControlsRoot.findViewById<TextView>(R.id.player_status)!!

        override fun onChromecastConnecting() {
            Log.d(javaClass.simpleName, "onChromecastConnecting")
            chromeCastConnectionStatusTextView.text = "connecting to chromecast..."
            chromeCastUiController.showProgressBar()
        }

        override fun onChromecastConnected(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {
            Log.d(javaClass.simpleName, "onChromecastConnected")
            chromeCastConnectionStatusTextView.text = "connected to chromecast"

            initializeCastPlayer(chromecastYouTubePlayerContext)
        }

        override fun onChromecastDisconnected() {
            Log.d(javaClass.simpleName, "onChromecastDisconnected")
            chromeCastConnectionStatusTextView.text = "not connected to chromecast"
            chromeCastUiController.resetUi()
            playerStatusTextView.text = ""
        }

        private fun initializeCastPlayer(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {

            chromecastYouTubePlayerContext.initialize(object: AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    chromeCastUiController.youTubePlayer = youTubePlayer

                    youTubePlayer.addListener(chromeCastUiController)
                    youTubePlayer.loadVideo(VideoIdsProvider.getNextVideoId(), 0f)

                    chromecastControlsRoot
                            .findViewById<Button>(R.id.next_video_button)
                            .setOnClickListener { youTubePlayer.loadVideo(VideoIdsProvider.getNextVideoId(), 0f) }
                }

                override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                    when(state) {
                        PlayerConstants.PlayerState.UNSTARTED -> playerStatusTextView.text = "UNSTARTED"
                        PlayerConstants.PlayerState.BUFFERING -> playerStatusTextView.text = "BUFFERING"
                        PlayerConstants.PlayerState.ENDED -> playerStatusTextView.text = "ENDED"
                        PlayerConstants.PlayerState.PAUSED -> playerStatusTextView.text = "PAUSED"
                        PlayerConstants.PlayerState.PLAYING -> playerStatusTextView.text = "PLAYING"
                        PlayerConstants.PlayerState.UNKNOWN -> playerStatusTextView.text = "UNKNOWN"
                        PlayerConstants.PlayerState.VIDEO_CUED -> playerStatusTextView.text = "VIDEO_CUED"
                        else -> Log.d(javaClass.simpleName, "unknown state")
                    }
                }
            })
        }
    }
}
