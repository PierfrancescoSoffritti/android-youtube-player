package com.pierfrancescosoffritti.aytplayersample.examples.chromecastExample.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pierfrancescosoffritti.aytplayersample.R;
import com.pierfrancescosoffritti.youtubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.utils.Utils;

import javax.annotation.Nullable;

/**
 * Class used to control a simple UI for the cast player.
 */
public class SimpleChromecastUIController extends AbstractYouTubePlayerListener implements SeekBar.OnSeekBarChangeListener {

    private final View controls_view;

    @Nullable private YouTubePlayer youTubePlayer;

    private boolean isPlaying = false;

    private final View progressBar;
    private final ImageView playPauseButton;
    private final TextView currentTimeTextView;
    private final TextView totalTimeTextView;
    private final SeekBar seekBar;
    private final ImageView youTubeButton;

    private final FrameLayout newViewsContainer;

    public SimpleChromecastUIController(View controls_view){
        this.controls_view = controls_view;

        progressBar = controls_view.findViewById(R.id.progress_bar);
        playPauseButton = controls_view.findViewById(R.id.play_pause_button);
        currentTimeTextView = controls_view.findViewById(R.id.current_time_text_view);
        totalTimeTextView = controls_view.findViewById(R.id.total_time_text_view);
        seekBar = controls_view.findViewById(R.id.seek_bar);
        youTubeButton = controls_view.findViewById(R.id.youtube_button);
        newViewsContainer = controls_view.findViewById(R.id.cast_button_container);

        seekBar.setOnSeekBarChangeListener(this);
        playPauseButton.setOnClickListener( view -> onPlayButtonPressed() );
    }

    public void setYouTubePlayer(@Nullable YouTubePlayer youTubePlayer) {
        this.youTubePlayer = youTubePlayer;
    }

    public void onStateChange(int state) {
        newSeekBarProgress = -1;

        updateControlsState(state);

        if (state == PlayerConstants.PlayerState.PLAYING || state == PlayerConstants.PlayerState.PAUSED || state == PlayerConstants.PlayerState.VIDEO_CUED || state == PlayerConstants.PlayerState.UNSTARTED) {
            progressBar.setVisibility(View.INVISIBLE);
            playPauseButton.setVisibility(View.VISIBLE);

        } else if(state == PlayerConstants.PlayerState.BUFFERING) {
            progressBar.setVisibility(View.VISIBLE);
            playPauseButton.setVisibility(View.INVISIBLE);
        }

        boolean playing = state == PlayerConstants.PlayerState.PLAYING;
        updatePlayPauseButtonIcon(playing);
    }

    @Override
    public void onVideoDuration(float duration) {
        totalTimeTextView.setText(Utils.formatTime(duration));
        seekBar.setMax((int) duration);
    }

    @Override
    public void onCurrentSecond(float currentSecond) {
        if (seekBarTouchStarted)
            return;

        // ignore if the current time is older than what the user selected with the SeekBar
        if (newSeekBarProgress > 0 && !Utils.formatTime(currentSecond).equals(Utils.formatTime(newSeekBarProgress)))
            return;

        newSeekBarProgress = -1;
        seekBar.setProgress((int) currentSecond);
    }

    @Override
    public void onVideoLoadedFraction(float loadedFraction) {
        seekBar.setSecondaryProgress((int) loadedFraction);
    }

    @Override
    public void onVideoId(@NonNull String videoId) {
        youTubeButton.setOnClickListener( view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$videoId"));
            controls_view.getContext().startActivity(intent);
        });
    }

    public void addView(View view) {
        newViewsContainer.addView(view);
    }

    public void removeView(View view) {
        newViewsContainer.removeView(view);
    }

    private void updateControlsState(int state) {
        switch (state) {
            case PlayerConstants.PlayerState.ENDED: isPlaying = false; break;
            case PlayerConstants.PlayerState.PAUSED: isPlaying = false; break;
            case PlayerConstants.PlayerState.PLAYING: isPlaying = true; break;
            case PlayerConstants.PlayerState.BUFFERING: isPlaying = false; break;
            case PlayerConstants.PlayerState.UNSTARTED: resetUI();
        }

        updatePlayPauseButtonIcon(!isPlaying);
    }

    public void resetUI() {
        seekBar.setProgress(0);
        seekBar.setMax(0);
        playPauseButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        currentTimeTextView.post( () -> currentTimeTextView.setText(""));
        totalTimeTextView.post( () -> totalTimeTextView.setText(""));
    }

    private void updatePlayPauseButtonIcon(boolean playing) {
        int img = playing ? R.drawable.ic_pause_36dp : R.drawable.ic_play_36dp;
        playPauseButton.setImageResource(img);
    }

    private void onPlayButtonPressed() {
        if(youTubePlayer == null)
            return;

        if (isPlaying)
            youTubePlayer.pause();
        else
            youTubePlayer.play();
    }

    // -- SeekBar, this code will be refactored

    private boolean seekBarTouchStarted = false;
    // I need this variable because onCurrentSecond gets called every 100 mill, so without the proper checks on this variable in onCurrentSeconds the seek bar glitches when touched.
    private int newSeekBarProgress = -1;

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        currentTimeTextView.setText(Utils.formatTime(i));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        seekBarTouchStarted = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (isPlaying)
            newSeekBarProgress = seekBar.getProgress();

        if(youTubePlayer != null)
            youTubePlayer.seekTo(seekBar.getProgress());
        seekBarTouchStarted = false;
    }
}
