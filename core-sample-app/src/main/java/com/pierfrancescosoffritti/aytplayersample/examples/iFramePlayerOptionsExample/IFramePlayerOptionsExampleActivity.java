package com.pierfrancescosoffritti.aytplayersample.examples.iFramePlayerOptionsExample;

import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.aytplayersample.R;

import java.util.Random;

public class IFramePlayerOptionsExampleActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;

    private String[] videoIds = {"6JYIGclVQdw", "LvetJ9U_tVY"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iframe_player_options_example);

        youTubePlayerView = findViewById(R.id.youtube_player_view);

        initYouTubePlayerView();
    }

    private void initYouTubePlayerView() {
        youTubePlayerView.inflateCustomPlayerUI(R.layout.empty_layout);

        IFramePlayerOptions iFramePlayerOptions = new IFramePlayerOptions.Builder()
                .controls(1)
                .build();

        // The player will automatically release itself when the activity is destroyed.
        // The player will automatically pause when the activity is stopped
        // If you don't add YouTubePlayerView as a lifecycle observer, you will have to release it manually.
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.initialize(youTubePlayer -> {

            youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    loadVideo(youTubePlayer, videoIds[0]);
                }
            });

            setPlayNextVideoButtonClickListener(youTubePlayer);

        }, true, iFramePlayerOptions);
    }

    private void loadVideo(YouTubePlayer youTubePlayer, String videoId) {
        if(getLifecycle().getCurrentState() == Lifecycle.State.RESUMED)
            youTubePlayer.loadVideo(videoId, 0);
        else
            youTubePlayer.cueVideo(videoId, 0);
    }
    /**
     * Set a click listener on the "Play next video" button
     */
    private void setPlayNextVideoButtonClickListener(final YouTubePlayer youTubePlayer) {
        Button playNextVideoButton = findViewById(R.id.next_video_button);

        playNextVideoButton.setOnClickListener(view -> {
            String videoId = videoIds[new Random().nextInt(videoIds.length)];
            loadVideo(youTubePlayer, videoId);
        });
    }
}
