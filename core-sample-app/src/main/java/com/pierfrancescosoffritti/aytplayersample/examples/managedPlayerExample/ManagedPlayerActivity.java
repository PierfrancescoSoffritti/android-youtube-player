package com.pierfrancescosoffritti.aytplayersample.examples.managedPlayerExample;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.ManagedYouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.aytplayersample.R;
import com.pierfrancescosoffritti.aytplayersample.utils.FullScreenHelper;
import com.pierfrancescosoffritti.aytplayersample.utils.VideoIdsProvider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ManagedPlayerActivity extends AppCompatActivity {

    private FullScreenHelper fullScreenHelper = new FullScreenHelper(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managed_player_example);

        ManagedYouTubePlayerView managedYouTubePlayerView = findViewById(R.id.youtube_player_view);

        addFullScreenListenerToPlayer(managedYouTubePlayerView);
        getLifecycle().addObserver(managedYouTubePlayerView);

        managedYouTubePlayerView.addListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                findViewById(R.id.next_video_button).setOnClickListener(view ->
                                youTubePlayer.loadVideo(VideoIdsProvider.getNextVideoId(), 0f)
                );

                youTubePlayer.loadVideo(VideoIdsProvider.getNextVideoId(), 0f);
            }

            @Override
            public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                if(state == PlayerConstants.PlayerState.ENDED)
                    youTubePlayer.loadVideo(VideoIdsProvider.getNextVideoId(), 0f);
            }
        });
    }

    private void addFullScreenListenerToPlayer(ManagedYouTubePlayerView managedYouTubePlayerView) {
        managedYouTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                fullScreenHelper.enterFullScreen();
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                fullScreenHelper.exitFullScreen();
            }
        });
    }
}
