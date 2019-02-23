package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.examples.localPlayerExample

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.mediarouter.app.MediaRouteButton
import android.view.View
import com.google.android.gms.cast.framework.CastContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.ChromecastYouTubePlayerContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.infrastructure.ChromecastConnectionListener
import com.pierfrancescosoffritti.cyplayersample.R
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils.MediaRouteButtonUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.utils.PlayServicesUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.notifications.NotificationManager
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.notifications.PlaybackControllerBroadcastReceiver
import kotlinx.android.synthetic.main.local_and_cast_player_example.*


class LocalPlayerInitExampleActivity : AppCompatActivity(), YouTubePlayersManager.LocalYouTubePlayerInitListener, ChromecastConnectionListener {
    private val googlePlayServicesAvailabilityRequestCode = 1

    private lateinit var youTubePlayersManager: YouTubePlayersManager
    private lateinit var mediaRouteButton : MediaRouteButton

    private lateinit var notificationManager: NotificationManager
    private lateinit var playbackControllerBroadcastReceiver: PlaybackControllerBroadcastReceiver

    private var connectedToChromecast = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.local_and_cast_player_example)

        lifecycle.addObserver(youtube_player_view)

        notificationManager = NotificationManager(this, LocalPlayerInitExampleActivity::class.java)

        youTubePlayersManager = YouTubePlayersManager(this, youtube_player_view, chromecast_controls_root, notificationManager)
        mediaRouteButton = MediaRouteButtonUtils.initMediaRouteButton(this)

        registerBroadcastReceiver()

        // can't use CastContext until I'm sure the user has GooglePlayServices
        PlayServicesUtils.checkGooglePlayServicesAvailability(this, googlePlayServicesAvailabilityRequestCode, Runnable { initChromecast() })
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext.unregisterReceiver(playbackControllerBroadcastReceiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // can't use CastContext until I'm sure the user has GooglePlayServices
        if(requestCode == googlePlayServicesAvailabilityRequestCode)
            PlayServicesUtils.checkGooglePlayServicesAvailability(this, googlePlayServicesAvailabilityRequestCode, Runnable {initChromecast()})
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
        if(connectedToChromecast)
            return

        MediaRouteButtonUtils.addMediaRouteButtonToPlayerUi(
                mediaRouteButton, android.R.color.white,
                null, localPlayerUiMediaRouteButtonContainer
        )
    }

    private fun registerBroadcastReceiver() {
        playbackControllerBroadcastReceiver = PlaybackControllerBroadcastReceiver({ youTubePlayersManager.togglePlayback() })
        val filter = IntentFilter(PlaybackControllerBroadcastReceiver.TOGGLE_PLAYBACK)
        filter.addAction(PlaybackControllerBroadcastReceiver.STOP_CAST_SESSION)
        applicationContext.registerReceiver(playbackControllerBroadcastReceiver, filter)
    }

    private fun updateUi(connected: Boolean) {

        val disabledContainer = if(connected) localPlayerUiMediaRouteButtonContainer else chromecastPlayerUiMediaRouteButtonContainer
        val enabledContainer = if(connected) chromecastPlayerUiMediaRouteButtonContainer else localPlayerUiMediaRouteButtonContainer
        val mediaRouteButtonColor = if(connected) android.R.color.black else android.R.color.white

        // the media route button has a single instance.
        // therefore it has to be moved from the local YouTube player Ui to the chromecast YouTube player Ui, and vice versa.
        MediaRouteButtonUtils.addMediaRouteButtonToPlayerUi(
                mediaRouteButton, mediaRouteButtonColor,
                disabledContainer, enabledContainer
        )

        youtube_player_view.visibility = if(connected) View.GONE else View.VISIBLE
        chromecast_controls_root.visibility = if(connected) View.VISIBLE else View.GONE
    }

    private val chromecastPlayerUiMediaRouteButtonContainer = object: MediaRouteButtonContainer {
        override fun addMediaRouteButton(mediaRouteButton: MediaRouteButton) = youTubePlayersManager.chromecastUiController.addView(mediaRouteButton)
        override fun removeMediaRouteButton(mediaRouteButton: MediaRouteButton) = youTubePlayersManager.chromecastUiController.removeView(mediaRouteButton)
    }

    private val localPlayerUiMediaRouteButtonContainer = object: MediaRouteButtonContainer {
        override fun addMediaRouteButton(mediaRouteButton: MediaRouteButton) { youtube_player_view.getPlayerUiController().addView(mediaRouteButton) }
        override fun removeMediaRouteButton(mediaRouteButton: MediaRouteButton) { youtube_player_view.getPlayerUiController().removeView(mediaRouteButton) }
    }
}