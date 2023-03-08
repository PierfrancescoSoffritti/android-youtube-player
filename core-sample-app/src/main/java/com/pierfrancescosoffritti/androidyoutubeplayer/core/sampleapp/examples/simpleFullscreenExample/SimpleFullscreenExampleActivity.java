package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.simpleFullscreenExample;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.aytplayersample.R;
import org.jetbrains.annotations.Nullable;

public class SimpleFullscreenExampleActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    private FrameLayout customView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_fullscreen_example);

        IFramePlayerOptions iFramePlayerOptions = new IFramePlayerOptions.Builder()
                .controls(1)
                .fullScreen(1)
                .build();

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        customView = findViewById(R.id.customView);

        youTubePlayerView.setEnableAutomaticInitialization(false);
        youTubePlayerView.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onShowCustomView(@Nullable View view, @Nullable WebChromeClient.CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                // for display full screen landscape view
                youTubePlayerView.setVisibility(View.GONE);
                customView.setVisibility(View.VISIBLE);
                customView.addView(view);
                if (!youTubePlayerView.isFullScreen()) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                youTubePlayerView.setVisibility(View.VISIBLE);
                customView.setVisibility(View.GONE);
                if (youTubePlayerView.isFullScreen()) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }

            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                youTubePlayer.loadVideo("S0Q4gqBUs7c", 0F);
            }
        }, iFramePlayerOptions);

        getLifecycle().addObserver(youTubePlayerView);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            youTubePlayerView.enterFullScreen();
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            youTubePlayerView.exitFullScreen();
        }
    }
}
