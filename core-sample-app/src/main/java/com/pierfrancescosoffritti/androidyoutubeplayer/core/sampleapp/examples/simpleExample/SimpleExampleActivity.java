package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.simpleExample;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.aytplayersample.R;

public class SimpleExampleActivity extends AppCompatActivity implements YouTubePlayerView.OnTouchEvent {

    final private String TAG = SimpleExampleActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_example);

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        youTubePlayerView.setOnTouchListener(this);
        getLifecycle().addObserver(youTubePlayerView);

    }

    @Override
    public void onTouchReceived() {
        Log.e(TAG, "onTouchReceived: ONCLick Received");
    }
}
