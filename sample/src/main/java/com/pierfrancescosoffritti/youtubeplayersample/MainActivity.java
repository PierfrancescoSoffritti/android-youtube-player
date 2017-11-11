package com.pierfrancescosoffritti.youtubeplayersample;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    private FullScreenManager fullScreenManager;

    private Button nextVideo;

    private String[] videoIds = {"6JYIGclVQdw", "LvetJ9U_tVY"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nextVideo = findViewById(R.id.next_video_button);

        fullScreenManager = new FullScreenManager(this);

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        youTubePlayerView.initialize(new YouTubePlayerInitListener() {
            @Override
            public void onInitSuccess(final YouTubePlayer initializedYouTubePlayer) {

                initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady() {
                        initializedYouTubePlayer.loadVideo(videoIds[0], 0);
                    }

                });

                initFullScreenListener(initializedYouTubePlayer);
                initButtonClickListener(initializedYouTubePlayer);

            }
        }, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        youTubePlayerView.release();
    }

    private void initFullScreenListener(final YouTubePlayer youTubePlayer) {
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
    }

    private void initButtonClickListener(final YouTubePlayer youTubePlayer) {
        nextVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String videoId = videoIds[new Random().nextInt(videoIds.length)];
                youTubePlayer.loadVideo(videoId, 0);
            }
        });
    }
}
