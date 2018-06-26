package com.pierfrancescosoffritti.cyplayersample.examples.localPlayerExample

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.MediaRouteButton
import android.view.View
import com.google.android.gms.cast.framework.CastContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.ChromecastYouTubePlayerContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.infrastructure.ChromecastConnectionListener
import com.pierfrancescosoffritti.cyplayersample.R
import com.pierfrancescosoffritti.cyplayersample.utils.MediaRouteButtonUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.utils.PlayServicesUtils
import com.pierfrancescosoffritti.cyplayersample.notifications.NotificationManager
import com.pierfrancescosoffritti.cyplayersample.notifications.PlaybackControllerBroadcastReceiver
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
        PlayServicesUtils.checkGooglePlayServicesAvailability(this, googlePlayServicesAvailabilityRequestCode) { initChromecast() }
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext.unregisterReceiver(playbackControllerBroadcastReceiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // can't use CastContext until I'm sure the user has GooglePlayServices
        if(requestCode == googlePlayServicesAvailabilityRequestCode)
            PlayServicesUtils.checkGooglePlayServicesAvailability(this, googlePlayServicesAvailabilityRequestCode) {initChromecast()}
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

        updateUI(true)

        notificationManager.showNotification()
    }

    override fun onChromecastDisconnected() {
        connectedToChromecast = false

        updateUI(false)

        notificationManager.dismissNotification()
    }

    override fun onLocalYouTubePlayerInit() {
        if(connectedToChromecast)
            return

        MediaRouteButtonUtils.addMediaRouteButtonToPlayerUI(
                mediaRouteButton, android.R.color.white,
                null, localPlayerUIMediaRouteButtonContainer
        )
    }

    private fun registerBroadcastReceiver() {
        playbackControllerBroadcastReceiver = PlaybackControllerBroadcastReceiver({ youTubePlayersManager.togglePlayback() })
        val filter = IntentFilter(PlaybackControllerBroadcastReceiver.TOGGLE_PLAYBACK)
        filter.addAction(PlaybackControllerBroadcastReceiver.STOP_CAST_SESSION)
        applicationContext.registerReceiver(playbackControllerBroadcastReceiver, filter)
    }

    private fun updateUI(connected: Boolean) {

        val disabledContainer = if(connected) localPlayerUIMediaRouteButtonContainer else chromecastPlayerUIMediaRouteButtonContainer
        val enabledContainer = if(connected) chromecastPlayerUIMediaRouteButtonContainer else localPlayerUIMediaRouteButtonContainer
        val mediaRouteButtonColor = if(connected) android.R.color.black else android.R.color.white

        // the media route button has a single instance.
        // therefore it has to be moved from the local YouTube player UI to the chromecast YouTube player UI, and vice versa.
        MediaRouteButtonUtils.addMediaRouteButtonToPlayerUI(
                mediaRouteButton, mediaRouteButtonColor,
                disabledContainer, enabledContainer
        )

        youtube_player_view.visibility = if(connected) View.GONE else View.VISIBLE
        chromecast_controls_root.visibility = if(connected) View.VISIBLE else View.GONE
    }

    private val chromecastPlayerUIMediaRouteButtonContainer = object: MediaRouteButtonContainer {
        override fun addMediaRouteButton(mediaRouteButton: MediaRouteButton) = youTubePlayersManager.chromecastUIController.addView(mediaRouteButton)
        override fun removeMediaRouteButton(mediaRouteButton: MediaRouteButton) = youTubePlayersManager.chromecastUIController.removeView(mediaRouteButton)
    }

    private val localPlayerUIMediaRouteButtonContainer = object: MediaRouteButtonContainer {
        override fun addMediaRouteButton(mediaRouteButton: MediaRouteButton) = youtube_player_view.playerUIController.addView(mediaRouteButton)
        override fun removeMediaRouteButton(mediaRouteButton: MediaRouteButton) = youtube_player_view.playerUIController.removeView(mediaRouteButton)
    }
}