package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.noLifecycleObserverExample;

import android.os.Bundle;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.aytplayersample.R;

import androidx.appcompat.app.AppCompatActivity;

public class NoLifecycleObserverExampleActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_example);

        youTubePlayerView = findViewById(R.id.youtube_player_view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }
}
