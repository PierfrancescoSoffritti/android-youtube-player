package com.pierfrancescosoffritti.youtubeplayer;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

class PlayerControlsWrapper implements View.OnClickListener, YouTubePlayerFullScreenListener, YouTubePlayer.YouTubeListener, SeekBar.OnSeekBarChangeListener {
    // will use this view as an access point to the YouTubePlayer
    @NonNull private final YouTubePlayerView youTubePlayerView;

    // view responsible for intercepting clicks. Could have used controlsRoot view, but in this way I'm able to hide all the control at once by hiding controlsRoot
    @NonNull private final View panel;

    // view containing the controls
    @NonNull private final View controlsRoot;

    @NonNull private final TextView videoTitle;
    @NonNull private final TextView videoCurrentTime;
    @NonNull private final TextView videoDuration;

    @NonNull private final ProgressBar progressBar;
    @NonNull private final ImageView playButton;
    @NonNull private final ImageView youTubeButton;
    @NonNull private final ImageView fullScreenButton;

    @NonNull private final ImageView customActionLeft;
    @NonNull private final ImageView customActionRight;

    @NonNull private final SeekBar seekBar;

    private View.OnClickListener onFullScreenButtonListener;

    // view state
    private boolean isPlaying = false;
    private boolean isVisible = true;
    private boolean canFadeControls = false;

    private boolean hideUI = false;

    PlayerControlsWrapper(@NonNull YouTubePlayerView youTubePlayerView, @NonNull View controlsView) {
        this.youTubePlayerView = youTubePlayerView;

        panel = controlsView.findViewById(R.id.panel);

        controlsRoot = controlsView.findViewById(R.id.controls_root);

        videoTitle = (TextView) controlsView.findViewById(R.id.video_title);
        videoCurrentTime = (TextView) controlsView.findViewById(R.id.video_current_time);
        videoDuration = (TextView) controlsView.findViewById(R.id.video_duration);

        progressBar = (ProgressBar) controlsView.findViewById(R.id.progress);
        playButton = (ImageView) controlsView.findViewById(R.id.play_button);
        youTubeButton = (ImageView) controlsView.findViewById(R.id.youtube_button);
        fullScreenButton = (ImageView) controlsView.findViewById(R.id.fullscreen_button);

        customActionLeft = (ImageView) controlsView.findViewById(R.id.custom_action_left_button);
        customActionRight = (ImageView) controlsView.findViewById(R.id.custom_action_right_button);

        seekBar = (SeekBar) controlsView.findViewById(R.id.seek_bar);

        seekBar.setOnSeekBarChangeListener(this);
        panel.setOnClickListener(this);
        playButton.setOnClickListener(this);
        fullScreenButton.setOnClickListener(this);
    }

    void setOnFullScreenButtonListener(View.OnClickListener onFullScreenButtonListener) {
        this.onFullScreenButtonListener = onFullScreenButtonListener;
    }

    void setCustomActionRight(Drawable icon, View.OnClickListener clickListener) {
        customActionRight.setImageDrawable(icon);
        customActionRight.setOnClickListener(clickListener);

        if(clickListener != null)
            customActionRight.setVisibility(View.VISIBLE);
        else
            customActionRight.setVisibility(View.GONE);
    }

    void setCustomActionLeft(Drawable icon, View.OnClickListener clickListener) {
        customActionLeft.setImageDrawable(icon);
        customActionLeft.setOnClickListener(clickListener);

        if(clickListener != null)
            customActionLeft.setVisibility(View.VISIBLE);
        else
            customActionRight.setVisibility(View.GONE);
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
        if(onFullScreenButtonListener == null)
            youTubePlayerView.toggleFullScreen();
        else
            onFullScreenButtonListener.onClick(fullScreenButton);
    }

    private void onPlayButtonPressed() {
        updateViewPlaybackState(!isPlaying);

        if(isPlaying)
            youTubePlayerView.playVideo();
        else
            youTubePlayerView.pauseVideo();
    }

    private void updateViewPlaybackState(boolean playing) {
        isPlaying = playing;

        int img = playing ? R.drawable.ic_pause_36dp : R.drawable.ic_play_36dp;

        playButton.setImageResource(img);
    }

    private void toggleControlsVisibility() {
        final float finalAlpha = isVisible ? 0f : 1f;
        fadeControls(finalAlpha);
    }

    private void fadeControls(final float finalAlpha) {
        if(!canFadeControls || hideUI)
            return;

        isVisible = finalAlpha != 0f;

        // if the controls are shown and the player is playing they should automatically hide after a while.
        // if the controls are hidden then I have to remove the task
        if(finalAlpha == 1f && isPlaying)
            startFadeOutViewTimer();
        else
            handler.removeCallbacks(fadeOutRunnable);


        controlsRoot.animate()
                .alpha(finalAlpha)
                .setDuration(300)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        if (finalAlpha == 1f)
                            controlsRoot.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (finalAlpha == 0f)
                            controlsRoot.setVisibility(View.GONE);
                    }

                    @Override public void onAnimationCancel(Animator animator) { }
                    @Override public void onAnimationRepeat(Animator animator) { }
                }).start();
    }

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable fadeOutRunnable = new Runnable() {
        @Override
        public void run() {
            fadeControls(0f);
        }
    };

    private void startFadeOutViewTimer() {
        handler.postDelayed(fadeOutRunnable, 3000);
    }


    // fullscreen callbacks

    @Override
    public void onYouTubePlayerEnterFullScreen() {
        fullScreenButton.setImageResource(R.drawable.ic_fullscreen_exit_24dp);
    }

    @Override
    public void onYouTubePlayerExitFullScreen() {
        fullScreenButton.setImageResource(R.drawable.ic_fullscreen_24dp);
    }

    // YouTubePlayer callbacks

    @Override
    public void onStateChange(@YouTubePlayer.State.YouTubePlayerState int state) {
        newSeekBarProgress = -1;

        if(state == YouTubePlayer.State.PLAYING || state == YouTubePlayer.State.PAUSED || state == YouTubePlayer.State.VIDEO_CUED) {
            panel.setBackgroundColor(ContextCompat.getColor(youTubePlayerView.getContext(), android.R.color.transparent));
            progressBar.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);

            if(customActionLeft.hasOnClickListeners())
                customActionLeft.setVisibility(View.VISIBLE);
            else
                customActionLeft.setVisibility(View.GONE);

            if(customActionRight.hasOnClickListeners())
                customActionRight.setVisibility(View.VISIBLE);
            else
                customActionRight.setVisibility(View.GONE);

            canFadeControls = true;
            boolean playing = state == YouTubePlayer.State.PLAYING;
            updateViewPlaybackState(playing);

            if(playing)
                startFadeOutViewTimer();
            else
                handler.removeCallbacks(fadeOutRunnable);

        } else {
            updateViewPlaybackState(false);
            fadeControls(1f);

            if(state == YouTubePlayer.State.BUFFERING) {
                playButton.setVisibility(View.INVISIBLE);

                customActionLeft.setVisibility(View.GONE);
                customActionRight.setVisibility(View.GONE);

                progressBar.setVisibility(View.VISIBLE);
                canFadeControls = false;
            }

            if(state == YouTubePlayer.State.UNSTARTED) {
                panel.setBackgroundColor(ContextCompat.getColor(youTubePlayerView.getContext(), android.R.color.black));
                canFadeControls = false;

                progressBar.setVisibility(View.GONE);
                playButton.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onCurrentSecond(float second) {
        // ignore if the user is currently moving the SeekBar
        if(seekBarTouchStarted)
            return;
        // ignore if the current time is older than what the user selected with the SeekBar
        if(newSeekBarProgress > 0 && !Utils.formatTime(second).equals(Utils.formatTime(newSeekBarProgress)))
            return;

        newSeekBarProgress = -1;

        seekBar.setProgress((int) second);
    }

    @Override
    public void onVideoDuration(float duration) {
        videoDuration.setText(Utils.formatTime(duration));
        seekBar.setMax((int) duration);
    }

    @Override
    public void onVideoTitle(String title) {
        videoTitle.setText(title);
    }

    @Override
    public void onVideoId(final String videoId) {
        youTubeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId));
                controlsRoot.getContext().startActivity(intent);
            }
        });
    }

    @Override public void onReady() { }
    @Override public void onLog(String log) { }
    @Override public void onPlaybackQualityChange(@YouTubePlayer.PlaybackQuality.Quality int playbackQuality) { }
    @Override public void onPlaybackRateChange(double rate) { }
    @Override public void onError(@YouTubePlayer.Error.PlayerError int error) { }
    @Override public void onApiChange() { }

    // SeekBar callbacks

    private boolean seekBarTouchStarted = false;
    // I need this variable because onCurrentSecond gets called every 100 mils, so without the proper checks on this variable in onCurrentSeconds the seek bar glitches when touched.
    private int newSeekBarProgress = -1;

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
            newSeekBarProgress = seekBar.getProgress();

        youTubePlayerView.seekTo(seekBar.getProgress());
        seekBarTouchStarted = false;
    }

    void onNewVideo() {
        seekBar.setProgress(0);
        seekBar.setMax(0);

        videoDuration.post(new Runnable() {
            @Override
            public void run() {
                videoDuration.setText("");
            }
        });

        videoTitle.post(new Runnable() {
            @Override
            public void run() {
                videoTitle.setText("");
            }
        });

        youTubeButton.setOnClickListener(null);
    }

    public void showTitle(boolean show) {
        videoTitle.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    void hideUI(boolean hide) {
        if(hide)
            controlsRoot.setVisibility(View.INVISIBLE);
        else
            controlsRoot.setVisibility(View.VISIBLE);
        hideUI = hide;
    }

    void showFullscreenButton(boolean show) {
        int visibility = show ? View.VISIBLE : View.INVISIBLE;
        fullScreenButton.setVisibility(visibility);
    }
}
