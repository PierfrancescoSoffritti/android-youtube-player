package com.pierfrancescosoffritti.aytplayersample.examples.viewPagerExample;

import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.aytplayersample.R;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class ViewPagerFragment extends Fragment {
    private String videoId = "6JYIGclVQdw";
    private YouTubePlayer initializedYouTubePlayer;
    private PlayerConstants.PlayerState playerState;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_view_pager, container, false);

        YouTubePlayerView youTubePlayerView = view.findViewById(R.id.youtube_player_view);

        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.getPlayerUIController().showFullscreenButton(false);

        youTubePlayerView.initialize(initializedYouTubePlayer -> {
            initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    Context context = getContext();
                    LayerDrawable playPauseButton = (LayerDrawable) ContextCompat.getDrawable(
                            context, R.drawable.play_pause_button
                    );
                    View.OnClickListener onClickListener = v -> {
                        boolean playing = playerState == PlayerConstants.PlayerState.PLAYING;
                        String action = playing ? "Pause" : "Play";
                        if (playing) {
                            initializedYouTubePlayer.pause();
                        } else {
                            initializedYouTubePlayer.play();
                        }
                        Toast.makeText(context, String.format("%s button clicked.", action),
                                Toast.LENGTH_SHORT).show();
                    };
                    youTubePlayerView.getPlayerUIController()
                            .setCustomPlayPauseButton(playPauseButton, onClickListener);
                    initializedYouTubePlayer.cueVideo(videoId, 0);
                    ViewPagerFragment.this.initializedYouTubePlayer = initializedYouTubePlayer;
                }

                @Override
                public void onStateChange(@NonNull PlayerConstants.PlayerState state) {
                    playerState = state;
                }
            });
        }, true);

        return view;
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (!visible && initializedYouTubePlayer != null)
            initializedYouTubePlayer.pause();
    }
}
