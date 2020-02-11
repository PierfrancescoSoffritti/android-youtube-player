package com.pierfrancescosoffritti.androidyoutubeplayer.core.testActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        
        getLifecycle().addObserver(youTubePlayerView);
    }
}