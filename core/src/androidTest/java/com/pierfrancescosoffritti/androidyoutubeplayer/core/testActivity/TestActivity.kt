package com.pierfrancescosoffritti.androidyoutubeplayer.core.testActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.test.R

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_activity)

        val youTubePlayerView = findViewById<YouTubePlayerView>(R.id.youtube_player_view)
        val youTubePlayerViewNative = findViewById<YouTubePlayerView>(R.id.youtube_player_view_native_ui)
        val youTubePlayerViewInitializeWithPlayerListener = findViewById<YouTubePlayerView>(R.id.youtube_player_view_manual_initialize_player_listener)
        val youTubePlayerViewInitializeWithPlayerListenerAndHandleNetworkEvents = findViewById<YouTubePlayerView>(R.id.youtube_player_view_manual_initialize_player_listener_and_handle_network_events)
        val youTubePlayerViewInitializeWithPlayerListenerAndHandleNetworkEventsAndIframeOptions = findViewById<YouTubePlayerView>(R.id.youtube_player_view_manual_initialize_player_listener_and_handle_network_events_and_iframe_options)
        val youTubePlayerViewUsingInitializeWithNativeUi = findViewById<YouTubePlayerView>(R.id.youtube_player_view_manual_initialize_native)

        lifecycle.addObserver(youTubePlayerView)
        lifecycle.addObserver(youTubePlayerViewNative)
        lifecycle.addObserver(youTubePlayerViewInitializeWithPlayerListener)
        lifecycle.addObserver(youTubePlayerViewInitializeWithPlayerListenerAndHandleNetworkEvents)
        lifecycle.addObserver(youTubePlayerViewInitializeWithPlayerListenerAndHandleNetworkEventsAndIframeOptions)
        lifecycle.addObserver(youTubePlayerViewUsingInitializeWithNativeUi)

    }
}