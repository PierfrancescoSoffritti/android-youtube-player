package com.pierfrancescosoffritti.aytplayersample.examples.fragmentExample;


import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.aytplayersample.R;

import java.util.Random;

public class FragmentExampleFragment extends Fragment {

    private YouTubePlayerView youTubePlayerView;
    Button playNextVideoButton;

    private String[] videoIds = {"6JYIGclVQdw", "LvetJ9U_tVY"};

    public FragmentExampleFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_example, container, false);

        youTubePlayerView = view.findViewById(R.id.youtube_player_view);
        playNextVideoButton = view.findViewById(R.id.next_video_button);
        initYouTubePlayerView();

        return view;
    }

    private void initYouTubePlayerView() {
        youTubePlayerView.getPlayerUIController().showFullscreenButton(false);

        // The player will automatically release itself when the fragment is destroyed.
        // The player will automatically pause when the fragment is stopped
        // If you don't add YouTubePlayerView as a lifecycle observer, you will have to release it manually.
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.initialize(youTubePlayer -> {

            youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    loadVideo(youTubePlayer, videoIds[0]);
                }
            });

            setPlayNextVideoButtonClickListener(youTubePlayer);

        }, true);
    }

    private void loadVideo(YouTubePlayer youTubePlayer, String videoId) {
        if(getLifecycle().getCurrentState() == Lifecycle.State.RESUMED)
            youTubePlayer.loadVideo(videoId, 0);
        else
            youTubePlayer.cueVideo(videoId, 0);
    }

    private void setPlayNextVideoButtonClickListener(final YouTubePlayer youTubePlayer) {
        playNextVideoButton.setOnClickListener(view -> {
            String videoId = videoIds[new Random().nextInt(videoIds.length)];
            loadVideo(youTubePlayer, videoId);
        });
    }
}
