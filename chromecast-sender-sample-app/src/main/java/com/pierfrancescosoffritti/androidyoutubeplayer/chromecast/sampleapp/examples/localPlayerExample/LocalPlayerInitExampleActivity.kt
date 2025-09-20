package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.examples.localPlayerExample

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.mediarouter.app.MediaRouteButton
import com.google.android.gms.cast.framework.CastContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.ChromecastYouTubePlayerContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.infrastructure.ChromecastConnectionListener
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.utils.PlayServicesUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.notifications.NotificationManager
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.notifications.PlaybackControllerBroadcastReceiver
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils.MediaRouteButtonUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.cyplayersample.R


class LocalPlayerInitExampleActivity : AppCompatActivity(),
  YouTubePlayersManager.LocalYouTubePlayerInitListener, ChromecastConnectionListener {
  private val googlePlayServicesAvailabilityRequestCode = 1

  private lateinit var youTubePlayersManager: YouTubePlayersManager
  private lateinit var mediaRouteButton: MediaRouteButton

  private lateinit var notificationManager: NotificationManager
  private lateinit var playbackControllerBroadcastReceiver: PlaybackControllerBroadcastReceiver

  private var connectedToChromecast = false

  private lateinit var youtubePlayerView: YouTubePlayerView
  private lateinit var chromecastControlsRoot: ViewGroup
  private lateinit var mediaRouteButtonRoot: ViewGroup

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.local_and_cast_player_example)

    youtubePlayerView = findViewById(R.id.youtube_player_view)
    chromecastControlsRoot = findViewById(R.id.chromecast_controls_root)
    mediaRouteButtonRoot = findViewById(R.id.media_route_button_root)

    lifecycle.addObserver(youtubePlayerView)

    notificationManager = NotificationManager(this, LocalPlayerInitExampleActivity::class.java)

    youTubePlayersManager =
      YouTubePlayersManager(this, youtubePlayerView, chromecastControlsRoot, notificationManager)
    mediaRouteButton = MediaRouteButtonUtils.initMediaRouteButton(this)

    registerBroadcastReceiver()

    // can't use CastContext until I'm sure the user has GooglePlayServices
    PlayServicesUtils.checkGooglePlayServicesAvailability(
      this,
      googlePlayServicesAvailabilityRequestCode
    ) { initChromecast() }
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
        googlePlayServicesAvailabilityRequestCode
      ) { initChromecast() }
  }

  private fun initChromecast() {
    ChromecastYouTubePlayerContext(
      CastContext.getSharedInstance(this).sessionManager,
      this, playbackControllerBroadcastReceiver, youTubePlayersManager
    )
  }

  override fun onChromecastConnecting() {
  }

  override fun onChromecastConnected(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {
    connectedToChromecast = true

    updateUi(true)

    notificationManager.showNotification()
  }

  override fun onChromecastDisconnected() {
    connectedToChromecast = false

    updateUi(false)

    notificationManager.dismissNotification()
  }

  override fun onLocalYouTubePlayerInit() {
    if (connectedToChromecast)
      return

    MediaRouteButtonUtils.addMediaRouteButtonToPlayerUi(
      mediaRouteButton, android.R.color.black,
      null, localPlayerUiMediaRouteButtonContainer
    )
  }

  // TODO: fix
  @SuppressLint("UnspecifiedRegisterReceiverFlag")
  private fun registerBroadcastReceiver() {
    playbackControllerBroadcastReceiver = PlaybackControllerBroadcastReceiver {
      youTubePlayersManager.togglePlayback()
    }
    val filter = IntentFilter(PlaybackControllerBroadcastReceiver.TOGGLE_PLAYBACK)
    filter.addAction(PlaybackControllerBroadcastReceiver.STOP_CAST_SESSION)
    applicationContext.registerReceiver(playbackControllerBroadcastReceiver, filter)
  }

  private fun updateUi(connected: Boolean) {

    val disabledContainer = if (connected) localPlayerUiMediaRouteButtonContainer else chromecastPlayerUiMediaRouteButtonContainer
    val enabledContainer = if (connected) chromecastPlayerUiMediaRouteButtonContainer else localPlayerUiMediaRouteButtonContainer

    // the media route button has a single instance.
    // therefore it has to be moved from the local YouTube player Ui to the chromecast YouTube player Ui, and vice versa.
    MediaRouteButtonUtils.addMediaRouteButtonToPlayerUi(
      mediaRouteButton, android.R.color.black,
      disabledContainer, enabledContainer
    )

    youtubePlayerView.visibility = if (connected) View.GONE else View.VISIBLE
    chromecastControlsRoot.visibility = if (connected) View.VISIBLE else View.GONE
  }

  private val chromecastPlayerUiMediaRouteButtonContainer = object : MediaRouteButtonContainer {
    override fun addMediaRouteButton(mediaRouteButton: MediaRouteButton) = youTubePlayersManager.chromecastUiController.addView(mediaRouteButton)

    override fun removeMediaRouteButton(mediaRouteButton: MediaRouteButton) = youTubePlayersManager.chromecastUiController.removeView(mediaRouteButton)
  }

  private val localPlayerUiMediaRouteButtonContainer = object : MediaRouteButtonContainer {
    override fun addMediaRouteButton(mediaRouteButton: MediaRouteButton) {
      mediaRouteButtonRoot.addView(mediaRouteButton)
    }

    override fun removeMediaRouteButton(mediaRouteButton: MediaRouteButton) {
      mediaRouteButtonRoot.removeView(mediaRouteButton)
    }
  }
}