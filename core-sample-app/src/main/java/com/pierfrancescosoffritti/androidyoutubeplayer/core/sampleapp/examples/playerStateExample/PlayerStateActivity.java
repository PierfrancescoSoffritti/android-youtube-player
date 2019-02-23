package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.playerStateExample;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.TextView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.utils.VideoIdsProvider;
import com.pierfrancescosoffritti.aytplayersample.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlayerStateActivity extends AppCompatActivity {

    private List<Pair<Date, String>> playerStatesHistory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_status_example);

        initYouTubePlayerView();
    }

    private void initYouTubePlayerView() {
        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                addToList("READY", playerStatesHistory);

                setPlayNextVideoButtonClickListener(youTubePlayer);

                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer, getLifecycle(),
                        VideoIdsProvider.getNextVideoId(), 0f
                );
            }

            @Override
            public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                onNewState(state);
            }

            @Override
            public void onError(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError error) {
                addToList("ERROR: " +error.name(), playerStatesHistory);
            }
        });
    }

    private void onNewState(PlayerConstants.PlayerState newState) {
        TextView playerStatusTextView = findViewById(R.id.player_status_text_view);
        String playerState = playerStateToString(newState);

        addToList(playerState, playerStatesHistory);
        printStates(playerStatesHistory, playerStatusTextView);
    }

    private void addToList(String playerState, List<Pair<Date, String>> stateHistory) {
        if(stateHistory.size() >= 15)
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

    private void setPlayNextVideoButtonClickListener(final YouTubePlayer youTubePlayer) {
        Button playNextVideoButton = findViewById(R.id.next_video_button);

        playNextVideoButton.setOnClickListener(view ->
                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer, getLifecycle(),
                        VideoIdsProvider.getNextVideoId(),0f
                )
        );
    }
}
