package com.pierfrancescosoffritti.youtubeplayersample.customUIExample;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.youtubeplayersample.R;
import com.pierfrancescosoffritti.youtubeplayersample.utils.FullScreenManager;

import java.util.Random;

import javax.annotation.Nullable;

public class CustomUIActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    private FullScreenManager fullScreenManager;

    private @Nullable YouTubePlayer initializedYouTubePlayer;

    private Button nextVideoButton;

    private String[] videoIds = {"6JYIGclVQdw", "LvetJ9U_tVY"};

    // a list of videos not available in some countries, to test if they're handled gracefully.
    private String[] nonPlayableVideoIds = { "sop2V_MREEI" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fullScreenManager = new FullScreenManager(this);

        nextVideoButton = findViewById(R.id.next_video_button);
        youTubePlayerView = findViewById(R.id.youtube_player_view);

        this.getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.setPlayerUIControls(new CustomPlayerUIController(this), R.layout.custom_player_controls);

        youTubePlayerView.initialize(initializedYouTubePlayer -> {

            initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    CustomUIActivity.this.initializedYouTubePlayer = initializedYouTubePlayer;

                    initializedYouTubePlayer.loadVideo(videoIds[0], 0);
                }
            });

            addFullScreenListenerToPlayer(initializedYouTubePlayer);
            setNextVideoButtonClickListener(initializedYouTubePlayer);

        }, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // It's not necessary to call release if you register youTubePlayerView as a lifecycle observer of this Activity.
//        youTubePlayerView.release();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (initializedYouTubePlayer != null)
            initializedYouTubePlayer.pause();
    }

    @Override
    public void onBackPressed() {
        if (youTubePlayerView.isFullScreen())
            youTubePlayerView.exitFullScreen();
        else
            super.onBackPressed();
    }

    private void addFullScreenListenerToPlayer(final YouTubePlayer youTubePlayer) {
        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                fullScreenManager.enterFullScreen();
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                fullScreenManager.exitFullScreen();
            }
        });
    }

    private void setNextVideoButtonClickListener(final YouTubePlayer youTubePlayer) {
        nextVideoButton.setOnClickListener(view -> {
            String videoId = videoIds[new Random().nextInt(videoIds.length)];

            youTubePlayer.loadVideo(videoId, 0);
        });
    }
}
