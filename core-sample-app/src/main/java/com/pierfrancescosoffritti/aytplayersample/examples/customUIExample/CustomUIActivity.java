package com.pierfrancescosoffritti.aytplayersample.examples.customUIExample;

import android.os.Bundle;
import android.view.View;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.aytplayersample.R;
import com.pierfrancescosoffritti.aytplayersample.utils.VideoIdsProvider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class CustomUIActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_ui_example);

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        View customPlayerUI = youTubePlayerView.inflateCustomPlayerUI(R.layout.custom_player_ui);

        youTubePlayerView.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                CustomPlayerUIController customPlayerUIController = new CustomPlayerUIController(CustomUIActivity.this, customPlayerUI, youTubePlayer, youTubePlayerView);
                youTubePlayer.addListener(customPlayerUIController);
                youTubePlayerView.addFullScreenListener(customPlayerUIController);

                youTubePlayer.loadVideo(VideoIdsProvider.getNextVideoId(), 0);
            }
        }, true);
    }
}
