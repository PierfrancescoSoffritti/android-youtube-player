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
        val youTubePlayerViewN = findViewById<YouTubePlayerView>(R.id.youtube_player_view_native)
        val youTubePlayerViewUsingInitializer1 = findViewById<YouTubePlayerView>(R.id.youtube_player_view_manual_initialize_1)
        val youTubePlayerViewUsingInitializer2 = findViewById<YouTubePlayerView>(R.id.youtube_player_view_manual_initialize_2)
        val youTubePlayerViewUsingInitializer3 = findViewById<YouTubePlayerView>(R.id.youtube_player_view_manual_initialize_3)
        val youTubePlayerViewUsingInitializeWithNativeUi = findViewById<YouTubePlayerView>(R.id.youtube_player_view_manual_initialize_native)

        lifecycle.addObserver(youTubePlayerView)
        lifecycle.addObserver(youTubePlayerViewN)
        lifecycle.addObserver(youTubePlayerViewUsingInitializer1)
        lifecycle.addObserver(youTubePlayerViewUsingInitializer2)
        lifecycle.addObserver(youTubePlayerViewUsingInitializer3)
        lifecycle.addObserver(youTubePlayerViewUsingInitializeWithNativeUi)

    }
}