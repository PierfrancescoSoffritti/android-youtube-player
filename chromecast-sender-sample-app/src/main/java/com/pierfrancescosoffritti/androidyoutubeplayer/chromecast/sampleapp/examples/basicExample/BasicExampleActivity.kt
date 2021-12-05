package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.examples.basicExample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.cast.framework.CastContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.ChromecastYouTubePlayerContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.infrastructure.ChromecastConnectionListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.cyplayersample.R
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils.MediaRouteButtonUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.utils.PlayServicesUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils.VideoIdsProvider
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.toFloat
import kotlinx.android.synthetic.main.activity_basic_example.*

class BasicExampleActivity : AppCompatActivity() {

    private val googlePlayServicesAvailabilityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_example)

        MediaRouteButtonUtils.initMediaRouteButton(media_route_button)

        // can't use CastContext until I'm sure the user has GooglePlayServices
        PlayServicesUtils.checkGooglePlayServicesAvailability(this, googlePlayServicesAvailabilityRequestCode, Runnable { initChromecast() })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // can't use CastContext until I'm sure the user has GooglePlayServices
        if(requestCode == googlePlayServicesAvailabilityRequestCode)
            PlayServicesUtils.checkGooglePlayServicesAvailability(this, googlePlayServicesAvailabilityRequestCode, Runnable { initChromecast() })
    }

    private fun initChromecast() {
        ChromecastYouTubePlayerContext(CastContext.getSharedInstance(this).sessionManager, SimpleChromecastConnectionListener())
    }

    inner class SimpleChromecastConnectionListener: ChromecastConnectionListener {
        override fun onChromecastConnecting() {
            Log.d(javaClass.simpleName, "onChromecastConnecting")
        }

        override fun onChromecastConnected(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {
            Log.d(javaClass.simpleName, "onChromecastConnected")

            initializeCastPlayer(chromecastYouTubePlayerContext)
        }

        override fun onChromecastDisconnected() {
            Log.d(javaClass.simpleName, "onChromecastDisconnected")
        }

        private fun initializeCastPlayer(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {
            chromecastYouTubePlayerContext.initialize(object: AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(VideoIdsProvider.getNextVideoId(), 0f)

                    initPlaybackSpeedButtons(youTubePlayer)
                }

                override fun onPlaybackRateChange(youTubePlayer: YouTubePlayer, playbackRate: PlayerConstants.PlaybackRate) {
                    val playbackSpeedTextView = findViewById<TextView>(R.id.playback_speed_text_view)
                    playbackSpeedTextView.text = "Playback speed: ${playbackRate.toFloat()}"
                }
            })
        }
    }

    fun initPlaybackSpeedButtons(youTubePlayer: YouTubePlayer) {
        val playbackSpeed_0_25 = findViewById<Button>(R.id.playback_speed_0_25)
        val playbackSpeed_1 = findViewById<Button>(R.id.playback_speed_1)
        val playbackSpeed_2 = findViewById<Button>(R.id.playback_speed_2)

        playbackSpeed_0_25.setOnClickListener { youTubePlayer.setPlaybackRate(PlayerConstants.PlaybackRate.RATE_0_25)  }
        playbackSpeed_1.setOnClickListener { youTubePlayer.setPlaybackRate(PlayerConstants.PlaybackRate.RATE_1) }
        playbackSpeed_2.setOnClickListener { youTubePlayer.setPlaybackRate(PlayerConstants.PlaybackRate.RATE_2) }
    }
}
