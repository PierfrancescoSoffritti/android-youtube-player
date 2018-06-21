package com.pierfrancescosoffritti.aytplayersample.examples.liveVideoExample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pierfrancescosoffritti.aytplayersample.R;
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

public class LiveVideoActivity extends AppCompatActivity {

    private final String liveVideoId = "2ccaHpy5Ewo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_video);

        initYouTubePlayerView();
    }

    private void initYouTubePlayerView() {
        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        youTubePlayerView.getPlayerUIController().showFullscreenButton(true);
        youTubePlayerView.getPlayerUIController().enableLiveVideoUI(true);

        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.initialize(youTubePlayer -> {

            youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    youTubePlayer.loadVideo(liveVideoId,0f);
                }
            });
        }, true);
    }
}
