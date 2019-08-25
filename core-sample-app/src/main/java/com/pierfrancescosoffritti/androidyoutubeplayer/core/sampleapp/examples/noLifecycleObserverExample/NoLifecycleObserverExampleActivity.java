package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.noLifecycleObserverExample;

import android.os.Bundle;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.utils.VideoIdsProvider;
import com.pierfrancescosoffritti.aytplayersample.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class NoLifecycleObserverExampleActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_lifecycle_observer_example);

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        youTubePlayerView.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                YouTubePlayerUtils.loadOrCueVideo(youTubePlayer, getLifecycle(), VideoIdsProvider.getNextVideoId(), 0f);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        youTubePlayerView.getYouTubePlayerWhenReady(YouTubePlayer::pause);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }
}
