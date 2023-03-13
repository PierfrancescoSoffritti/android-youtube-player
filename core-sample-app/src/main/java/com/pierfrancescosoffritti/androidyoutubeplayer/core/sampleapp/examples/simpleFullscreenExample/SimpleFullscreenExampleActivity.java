package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.simpleFullscreenExample;

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

public class SimpleFullscreenExampleActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    private FrameLayout fullScreenViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_fullscreen_example);

        IFramePlayerOptions iFramePlayerOptions = new IFramePlayerOptions.Builder()
                .controls(1)
                .fullscreen(1)
                .build();

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        fullScreenViewContainer = findViewById(R.id.full_screen_view_container);

        youTubePlayerView.setEnableAutomaticInitialization(false);

        youTubePlayerView.addFullScreenListener(new FullScreenListener() {
            @Override
            public void onEnterFullScreen(@NonNull View fullScreenView, @NonNull Function0<Unit> exitFullScreen) {
                // for display full screen landscape view
                youTubePlayerView.setVisibility(View.GONE);
                fullScreenViewContainer.setVisibility(View.VISIBLE);
                fullScreenViewContainer.addView(fullScreenView);

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

            @Override
            public void onExitFullScreen() {
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
