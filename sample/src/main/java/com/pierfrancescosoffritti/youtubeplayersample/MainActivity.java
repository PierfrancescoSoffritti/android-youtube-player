package com.pierfrancescosoffritti.youtubeplayersample;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pierfrancescosoffritti.youtubeplayer.youTubePlayer.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.youTubePlayer.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.youTubePlayer.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.youtubeplayer.youTubePlayer.YouTubePlayerView;

public class MainActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    @Nullable private YouTubePlayer youTubePlayer;
    private FullScreenManager fullScreenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fullScreenManager = new FullScreenManager(this);

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        youTubePlayerView.initialize(new YouTubePlayer.YouTubePlayerInitListener() {
            @Override
            public void onInitSuccess(final YouTubePlayer initializedYouTubePlayer) {

                youTubePlayer = initializedYouTubePlayer;

                initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady() {
                        initializedYouTubePlayer.loadVideo("6JYIGclVQdw", 0);
                    }

                });
            }
        }, true);

        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                fullScreenManager.enterFullScreen();

                youTubePlayerView.getPlayerUIController().setCustomAction1(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_pause_36dp), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(youTubePlayer != null) youTubePlayer.pause();
                    }
                });
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                fullScreenManager.exitFullScreen();

                youTubePlayerView.getPlayerUIController().showCustomAction1(false);
            }
        });

        findViewById(R.id.next_video_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(youTubePlayer != null) youTubePlayer.loadVideo("LvetJ9U_tVY", 0);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        youTubePlayerView.release();
    }
}
