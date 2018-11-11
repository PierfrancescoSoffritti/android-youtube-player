package com.pierfrancescosoffritti.aytplayersample.examples.customUIExample;

import android.os.Bundle;
import android.view.View;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.aytplayersample.R;

import androidx.appcompat.app.AppCompatActivity;

public class CustomUIActivity extends AppCompatActivity {

    private String[] videoIds = {"6JYIGclVQdw", "LvetJ9U_tVY"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_ui_example);

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        View customPlayerUI = youTubePlayerView.inflateCustomPlayerUI(R.layout.custom_player_ui);

        youTubePlayerView.initialize(youTubePlayer -> {

            CustomPlayerUIController customPlayerUIController = new CustomPlayerUIController(this, customPlayerUI, youTubePlayer, youTubePlayerView);
            youTubePlayer.addListener(customPlayerUIController);
            youTubePlayerView.addFullScreenListener(customPlayerUIController);

            youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    youTubePlayer.loadVideo(videoIds[0], 0);
                }
            });

        }, true);
    }
}
