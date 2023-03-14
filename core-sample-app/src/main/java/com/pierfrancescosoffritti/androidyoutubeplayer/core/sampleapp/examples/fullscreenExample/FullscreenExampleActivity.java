package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.fullscreenExample;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.aytplayersample.R;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class FullscreenExampleActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    private FrameLayout fullScreenViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_example);

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        fullScreenViewContainer = findViewById(R.id.full_screen_view_container);

        IFramePlayerOptions iFramePlayerOptions = new IFramePlayerOptions.Builder()
                .controls(1)
                // enable full screen button
                .fullscreen(1)
                .build();

        // we need to initialize manually in order to pass IFramePlayerOptions to the player
        youTubePlayerView.setEnableAutomaticInitialization(false);

        youTubePlayerView.addFullScreenListener(new FullScreenListener() {
            @Override
            public void onEnterFullScreen(@NonNull View fullScreenView, @NonNull Function0<Unit> exitFullScreen) {
                // the video will continue playing in fullScreenView
                youTubePlayerView.setVisibility(View.GONE);
                fullScreenViewContainer.setVisibility(View.VISIBLE);
                fullScreenViewContainer.addView(fullScreenView);

                // optionally request landscape orientation
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

            @Override
            public void onExitFullScreen() {
                // the video will continue playing in the player
                youTubePlayerView.setVisibility(View.VISIBLE);
                fullScreenViewContainer.setVisibility(View.GONE);
                fullScreenViewContainer.removeAllViews();

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
        });

        youTubePlayerView.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                youTubePlayer.loadVideo("S0Q4gqBUs7c", 0F);
            }
        }, iFramePlayerOptions);

        getLifecycle().addObserver(youTubePlayerView);
    }
}
