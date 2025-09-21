package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.examples.notificationExample

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.mediarouter.app.MediaRouteButton
import com.google.android.gms.cast.framework.CastContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.ChromecastYouTubePlayerContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.infrastructure.ChromecastConnectionListener
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.utils.PlayServicesUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.notifications.NotificationManager
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.notifications.PlaybackControllerBroadcastReceiver
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils.MediaRouteButtonUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils.VideoIdsProvider
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.cyplayersample.R

/**
 * Simple example showing how to build a notification to control the cast player.
 * In a real application both notification and playback should be managed in a service.
 */
class NotificationExampleActivity : AppCompatActivity() {

  private val googlePlayServicesAvailabilityRequestCode = 1
  private val playbackControllerBroadcastReceiver = PlaybackControllerBroadcastReceiver()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_notification_example)

    registerBroadcastReceiver()

    val mediaRouteButton = findViewById<MediaRouteButton>(R.id.media_route_button)
    MediaRouteButtonUtils.initMediaRouteButton(mediaRouteButton)

    // can't use CastContext until I'm sure the user has GooglePlayServices
    PlayServicesUtils.checkGooglePlayServicesAvailability(
      this,
      googlePlayServicesAvailabilityRequestCode,
      Runnable { initChromecast() })
  }

  override fun onDestroy() {
    super.onDestroy()
    applicationContext.unregisterReceiver(playbackControllerBroadcastReceiver)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    // can't use CastContext until I'm sure the user has GooglePlayServices
    if (requestCode == googlePlayServicesAvailabilityRequestCode)
      PlayServicesUtils.checkGooglePlayServicesAvailability(
        this,
        googlePlayServicesAvailabilityRequestCode,
        Runnable { initChromecast() })
  }

  // TODO: fix
  @SuppressLint("UnspecifiedRegisterReceiverFlag")
  private fun registerBroadcastReceiver() {
    val filter = IntentFilter(PlaybackControllerBroadcastReceiver.TOGGLE_PLAYBACK)
    filter.addAction(PlaybackControllerBroadcastReceiver.STOP_CAST_SESSION)
    applicationContext.registerReceiver(playbackControllerBroadcastReceiver, filter)
  }

  private fun initChromecast() {
    val chromecastConnectionListener = SimpleChromecastConnectionListener()
    ChromecastYouTubePlayerContext(
      CastContext.getSharedInstance(this).sessionManager,
      chromecastConnectionListener,
      playbackControllerBroadcastReceiver
    )
  }

  inner class SimpleChromecastConnectionListener : ChromecastConnectionListener {

    private val notificationManager =
      NotificationManager(applicationContext, NotificationExampleActivity::class.java)

    override fun onChromecastConnecting() {
      Log.d(javaClass.simpleName, "onChromecastConnecting")
    }

    override fun onChromecastConnected(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {
      Log.d(javaClass.simpleName, "onChromecastConnected")

      initializeCastPlayer(chromecastYouTubePlayerContext)
      notificationManager.showNotification()
    }

    override fun onChromecastDisconnected() {
      Log.d(javaClass.simpleName, "onChromecastDisconnected")
      notificationManager.dismissNotification()
    }

    private fun initializeCastPlayer(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {
      chromecastYouTubePlayerContext.initialize(object : AbstractYouTubePlayerListener() {
        override fun onReady(youTubePlayer: YouTubePlayer) {
          val playerStateTracker = YouTubePlayerTracker()

          initBroadcastReceiver(youTubePlayer, playerStateTracker)

          youTubePlayer.addListener(notificationManager)
          youTubePlayer.addListener(playerStateTracker)

          youTubePlayer.loadVideo(VideoIdsProvider.getNextVideoId(), 0f)
        }
      })
    }

    private fun initBroadcastReceiver(
      youTubePlayer: YouTubePlayer,
      playerTracker: YouTubePlayerTracker
    ) {
      playbackControllerBroadcastReceiver.togglePlayback = {
        if (playerTracker.state == PlayerConstants.PlayerState.PLAYING)
          youTubePlayer.pause()
        else
          youTubePlayer.play()
      }
    }
  }
}
