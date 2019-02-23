package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.fragmentExample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.utils.VideoIdsProvider;
import com.pierfrancescosoffritti.aytplayersample.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FragmentExampleFragment extends Fragment {

    private YouTubePlayerView youTubePlayerView;
    private Button playNextVideoButton;

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
        // The player will automatically release itself when the fragment is destroyed.
        // The player will automatically pause when the fragment is stopped
        // If you don't add YouTubePlayerView as a lifecycle observer, you will have to release it manually.
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                setPlayNextVideoButtonClickListener(youTubePlayer);

                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer, getLifecycle(),
                        VideoIdsProvider.getNextVideoId(),0f
                );
            }
        });
    }

    private void setPlayNextVideoButtonClickListener(final YouTubePlayer youTubePlayer) {
        playNextVideoButton.setOnClickListener(view ->
            YouTubePlayerUtils.loadOrCueVideo(
                    youTubePlayer, getLifecycle(),
                    VideoIdsProvider.getNextVideoId(),0f
            )
        );
    }
}
