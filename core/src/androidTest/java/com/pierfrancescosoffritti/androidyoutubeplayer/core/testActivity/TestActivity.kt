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

        lifecycle.addObserver(youTubePlayerView)
    }
}