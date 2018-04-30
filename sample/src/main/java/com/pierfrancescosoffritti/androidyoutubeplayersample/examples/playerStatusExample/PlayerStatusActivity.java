package com.pierfrancescosoffritti.androidyoutubeplayersample.examples.playerStatusExample;

import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.pierfrancescosoffritti.androidyoutubeplayersample.R;
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

import java.util.Random;

public class PlayerStatusActivity extends AppCompatActivity {

    private String[] videoIds = {"6JYIGclVQdw", "LvetJ9U_tVY"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_status_example);

        initYouTubePlayerView();
    }

    private void initYouTubePlayerView() {
        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        youTubePlayerView.getPlayerUIController().showYouTubeButton(false);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.initialize(youTubePlayer -> {

            setPlayNextVideoButtonClickListener(youTubePlayer);

            youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    loadVideo(youTubePlayer, videoIds[0]);
                }

                @Override
                public void onStateChange(int state) {
                    TextView playerStatusTextView = findViewById(R.id.player_status_text_view);
                    String playerState = playerStateToString(state);
                    playerStatusTextView.setText(playerState);
                }
            });

        }, true);
    }

    private String playerStateToString(@PlayerConstants.PlayerState.State int state) {
        switch (state) {
            case PlayerConstants.PlayerState.UNKNOWN:
                return "UNKNOWN";
            case PlayerConstants.PlayerState.UNSTARTED:
                return "UNSTARTED";
            case PlayerConstants.PlayerState.ENDED:
                return "ENDED";
            case PlayerConstants.PlayerState.PLAYING:
                return "PLAYING";
            case PlayerConstants.PlayerState.PAUSED:
                return "PAUSED";
            case PlayerConstants.PlayerState.BUFFERING:
                    return "BUFFERING";
            case PlayerConstants.PlayerState.VIDEO_CUED:
                return "VIDEO_CUED";
            default:
                return "status unknown";
        }
    }

    private void loadVideo(YouTubePlayer youTubePlayer, String videoId) {
        if(getLifecycle().getCurrentState() == Lifecycle.State.RESUMED)
            youTubePlayer.loadVideo(videoId, 0);
        else
            youTubePlayer.cueVideo(videoId, 0);
    }

    private void setPlayNextVideoButtonClickListener(final YouTubePlayer youTubePlayer) {
        Button playNextVideoButton = findViewById(R.id.next_video_button);

        playNextVideoButton.setOnClickListener(view -> {
            String videoId = videoIds[new Random().nextInt(videoIds.length)];
            loadVideo(youTubePlayer, videoId);
        });
    }
}
