package com.pierfrancescosoffritti.youtubeplayer;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlayerControls implements View.OnClickListener {
    @NonNull private final View controlsView;

    @NonNull private final View panel;

    @NonNull private final View contrsolRoot;

    @NonNull private final TextView videoTitle;
    @NonNull private final TextView videoCurrentTime;
    @NonNull private final TextView videoDuration;

    @NonNull private final ImageView playButton;
    @NonNull private final ImageView youTubeButton;
    @NonNull private final ImageView fullScreenButton;

    @NonNull private final SeekBar seekBar;

    @NonNull private final View dropShadowTop;
    @NonNull private final View dropShadowBottom;

    private boolean isPlaying;
    private boolean isVisible;

    public PlayerControls(@NonNull View controlsView) {
        isPlaying = false;
        isVisible = true;

        this.controlsView = controlsView;

        panel = controlsView.findViewById(R.id.panel);

        contrsolRoot = controlsView.findViewById(R.id.controls_root);

        videoTitle = (TextView) controlsView.findViewById(R.id.video_title);
        videoCurrentTime = (TextView) controlsView.findViewById(R.id.video_current_time);
        videoDuration = (TextView) controlsView.findViewById(R.id.video_duration);

        playButton = (ImageView) controlsView.findViewById(R.id.play_button);
        youTubeButton = (ImageView) controlsView.findViewById(R.id.youtube_button);
        fullScreenButton = (ImageView) controlsView.findViewById(R.id.fullscreen_button);

        seekBar = (SeekBar) controlsView.findViewById(R.id.seek_bar);

        dropShadowTop = controlsView.findViewById(R.id.drop_shadow_top);
        dropShadowBottom = controlsView.findViewById(R.id.drop_shadow_bottom);

        panel.setOnClickListener(this);
        playButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == panel) {
            fadeControls();
        } else if(view == playButton) {
            onPlayButtonPressed();
        }
    }

    private void onPlayButtonPressed() {
        isPlaying = !isPlaying;

        int img = isPlaying ? R.drawable.ic_play_36dp : R.drawable.ic_pause_36dp;

        playButton.setImageResource(img);
    }

    private void fadeControls() {
        float finalAlpha = isVisible ? 0f : 1f;
        isVisible = !isVisible;
        contrsolRoot.animate().alpha(finalAlpha).setDuration(300).start();
    }
}
