package com.pierfrancescosoffritti.aytplayersample.examples.playerStatusExample;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import android.widget.Button;
import android.widget.TextView;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.aytplayersample.R;
import com.pierfrancescosoffritti.aytplayersample.utils.VideoIdsProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class PlayerStatusActivity extends AppCompatActivity {

    private List<Pair<Date, String>> playerStatesHistory = new ArrayList<>();

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
                    loadVideo(youTubePlayer, VideoIdsProvider.getNextVideoId());
                }

                @Override
                public void onStateChange(@NonNull PlayerConstants.PlayerState state) {
                    onNewState(state);
                }
            });

        }, true);
    }

    private void onNewState(PlayerConstants.PlayerState newState) {
        TextView playerStatusTextView = findViewById(R.id.player_status_text_view);
        String playerState = playerStateToString(newState);

        addToList(playerState, playerStatesHistory);
        printStates(playerStatesHistory, playerStatusTextView);
    }

    private void addToList(String playerState, List<Pair<Date, String>> stateHistory) {
        if(stateHistory.size() >= 10)
            stateHistory.remove(0);
        stateHistory.add(new Pair<>(new Date(), playerState));
    }

    private void printStates(List<Pair<Date, String>> states, TextView playerStatusTextView) {
        SimpleDateFormat dt = new SimpleDateFormat("MM-dd hh:mm:ss.SSS", Locale.US);

        StringBuilder sb = new StringBuilder();
        for(int i=0; i<states.size(); i++) {
            Pair<Date, String> pair = states.get(i);
            sb.append(dt.format(pair.first)).append(": ").append(pair.second);
            if(i != states.size()-1)
                sb.append("\n");
        }

        playerStatusTextView.setText(sb.toString());
    }

    private String playerStateToString(PlayerConstants.PlayerState state) {
        switch (state) {
            case UNKNOWN:
                return "UNKNOWN";
            case UNSTARTED:
                return "UNSTARTED";
            case ENDED:
                return "ENDED";
            case PLAYING:
                return "PLAYING";
            case PAUSED:
                return "PAUSED";
            case BUFFERING:
                    return "BUFFERING";
            case VIDEO_CUED:
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
            loadVideo(youTubePlayer, VideoIdsProvider.getNextVideoId());
        });
    }
}
