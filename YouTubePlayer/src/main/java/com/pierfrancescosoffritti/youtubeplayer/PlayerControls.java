package com.pierfrancescosoffritti.youtubeplayer;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlayerControls implements View.OnClickListener, YouTubePlayerFullScreenListener, YouTubePlayer.YouTubeListener, SeekBar.OnSeekBarChangeListener {
    @NonNull private final YouTubePlayerView youTubePlayerView;
    @NonNull private final View controlsView;

    @NonNull private final View panel;

    @NonNull private final View controlsRoot;

    @NonNull private final TextView videoTitle;
    @NonNull private final TextView videoCurrentTime;
    @NonNull private final TextView videoDuration;

    @NonNull private final View progress;
    @NonNull private final ImageView playButton;
    @NonNull private final ImageView youTubeButton;
    @NonNull private final ImageView fullScreenButton;

    @NonNull private final SeekBar seekBar;

    @NonNull private final View dropShadowTop;
    @NonNull private final View dropShadowBottom;

    private boolean isPlaying;
    private boolean isVisible;

    public PlayerControls(@NonNull YouTubePlayerView youTubePlayerView, @NonNull View controlsView) {
        isPlaying = false;
        isVisible = true;

        this.youTubePlayerView = youTubePlayerView;
        this.controlsView = controlsView;

        panel = controlsView.findViewById(R.id.panel);

        controlsRoot = controlsView.findViewById(R.id.controls_root);

        videoTitle = (TextView) controlsView.findViewById(R.id.video_title);
        videoCurrentTime = (TextView) controlsView.findViewById(R.id.video_current_time);
        videoDuration = (TextView) controlsView.findViewById(R.id.video_duration);

        progress = controlsView.findViewById(R.id.progress);
        playButton = (ImageView) controlsView.findViewById(R.id.play_button);
        youTubeButton = (ImageView) controlsView.findViewById(R.id.youtube_button);
        fullScreenButton = (ImageView) controlsView.findViewById(R.id.fullscreen_button);

        seekBar = (SeekBar) controlsView.findViewById(R.id.seek_bar);

        dropShadowTop = controlsView.findViewById(R.id.drop_shadow_top);
        dropShadowBottom = controlsView.findViewById(R.id.drop_shadow_bottom);

        seekBar.setOnSeekBarChangeListener(this);
        panel.setOnClickListener(this);
        playButton.setOnClickListener(this);
        fullScreenButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == panel)
            toggleControlsVisibility();
        else if(view == playButton)
            onPlayButtonPressed();
        else if(view == fullScreenButton)
            onFullScreenPressed();

    }

    private void onFullScreenPressed() {
        youTubePlayerView.toggleFullScreen();
    }

    private void onPlayButtonPressed() {
        updateViewState(!isPlaying);

        if(isPlaying)
            youTubePlayerView.playVideo();
        else
            youTubePlayerView.pauseVideo();
    }

    private void updateViewState(boolean playing) {
        isPlaying = playing;

        int img = playing ? R.drawable.ic_pause_36dp : R.drawable.ic_play_36dp;

        playButton.setImageResource(img);
    }

    private void toggleControlsVisibility() {
        final float finalAlpha = isVisible ? 0f : 1f;
        fadeControls(finalAlpha);
    }

    private void fadeControls(final float finalAlpha) {
        isVisible = finalAlpha != 0f;
        controlsRoot.animate().alpha(finalAlpha).setDuration(300).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if(finalAlpha == 1f)
                    controlsRoot.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(finalAlpha == 0f)
                    controlsRoot.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).start();
    }

    @Override
    public void onYouTubePlayerEnterFullScreen() {
        fullScreenButton.setImageResource(R.drawable.ic_fullscreen_exit_24dp);
    }

    @Override
    public void onYouTubePlayerExitFullScreen() {
        fullScreenButton.setImageResource(R.drawable.ic_fullscreen_24dp);
    }

    @Override
    public void onReady(@NonNull YouTubePlayer youTubePlayer) {

    }

    @Override
    public void onStateChange(@YouTubePlayer.State.YouTubePlayerState int state, @NonNull YouTubePlayer youTubePlayer) {
        if(state == YouTubePlayer.State.PLAYING || state == YouTubePlayer.State.PAUSED) {
            progress.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);

            boolean playing = state == YouTubePlayer.State.PLAYING;
            updateViewState(playing);
        } else {
            updateViewState(false);
            fadeControls(1f);

            if(state == YouTubePlayer.State.BUFFERING) {
                playButton.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onPlaybackQualityChange(@YouTubePlayer.PlaybackQuality.Quality int playbackQuality, @NonNull YouTubePlayer youTubePlayer) {

    }

    @Override
    public void onPlaybackRateChange(double rate, @NonNull YouTubePlayer youTubePlayer) {

    }

    @Override
    public void onError(String arg, @NonNull YouTubePlayer youTubePlayer) {

    }

    @Override
    public void onApiChange(@NonNull YouTubePlayer youTubePlayer) {

    }

    @Override
    public void onCurrentSecond(float second, @NonNull YouTubePlayer youTubePlayer) {
        if(seekBarTouchStarted)
            return;
        if(newProgress > 0 && !Utils.formatTime(second).equals(Utils.formatTime(newProgress)))
            return;

        newProgress = -1;
        seekBar.setProgress((int) second);
    }

    @Override
    public void onDuration(float duration, @NonNull YouTubePlayer youTubePlayer) {
        videoDuration.setText(Utils.formatTime(duration));
        seekBar.setMax((int) duration);
    }

    @Override
    public void onLog(String log, @NonNull YouTubePlayer youTubePlayer) {

    }

    private boolean seekBarTouchStarted = false;
    private int newProgress = -1;

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        videoCurrentTime.setText(Utils.formatTime(i));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        seekBarTouchStarted = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(isPlaying)
            newProgress = seekBar.getProgress();

        youTubePlayerView.seekTo(seekBar.getProgress());
        seekBarTouchStarted = false;
    }
}
