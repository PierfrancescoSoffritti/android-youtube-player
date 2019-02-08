package com.pierfrancescosoffritti.androidyoutubeplayer.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pierfrancescosoffritti.androidyoutubeplayer.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.menu.YouTubePlayerMenu;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.menu.defaultMenu.DefaultYouTubePlayerMenu;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.views.FadingFrameLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.views.YouTubePlayerSeekBar;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.views.YouTubePlayerSeekBarListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class DefaultPlayerUIController implements PlayerUIController, YouTubePlayerListener, YouTubePlayerFullScreenListener, View.OnClickListener, YouTubePlayerSeekBarListener {
    @NonNull private final YouTubePlayerView youTubePlayerView;
    @NonNull private final YouTubePlayer youTubePlayer;

    @NonNull private YouTubePlayerMenu youTubePlayerMenu;

    /**
     * View used for for intercepting clicks and for drawing a black background.
     * Could have used controlsContainer, but in this way I'm able to hide all the control at once by hiding controlsContainer
     */
    private View panel;

    private FadingFrameLayout controlsContainer;
    private LinearLayout extraViewsContainer;

    private TextView videoTitle;
    private TextView liveVideoIndicator;

    private ProgressBar progressBar;
    private ImageView menuButton;
    private ImageView playPauseButton;
    private ImageView youTubeButton;
    private ImageView fullScreenButton;

    private ImageView customActionLeft;
    private ImageView customActionRight;

    private YouTubePlayerSeekBar youtubePlayerSeekBar;

    @Nullable private View.OnClickListener onFullScreenButtonListener;
    @Nullable private View.OnClickListener onMenuButtonClickListener;

    private boolean isPlaying = false;

    private boolean isPlayPauseButtonEnabled = true;
    private boolean isCustomActionLeftEnabled = false;
    private boolean isCustomActionRightEnabled = false;

    public DefaultPlayerUIController(@NonNull YouTubePlayerView youTubePlayerView, @NonNull YouTubePlayer youTubePlayer) {
        this.youTubePlayerView = youTubePlayerView;
        this.youTubePlayer = youTubePlayer;

        View defaultPlayerUI = View.inflate(youTubePlayerView.getContext(), R.layout.ayp_default_player_ui, youTubePlayerView);
        initViews(defaultPlayerUI);

        youTubePlayerMenu = new DefaultYouTubePlayerMenu(youTubePlayerView.getContext());
    }

    private void initViews(View controlsView) {
        panel = controlsView.findViewById(R.id.panel);

        controlsContainer = controlsView.findViewById(R.id.controls_container);
        extraViewsContainer = controlsView.findViewById(R.id.extra_views_container);

        videoTitle = controlsView.findViewById(R.id.video_title);
        liveVideoIndicator = controlsView.findViewById(R.id.live_video_indicator);

        progressBar = controlsView.findViewById(R.id.progress);
        menuButton = controlsView.findViewById(R.id.menu_button);
        playPauseButton = controlsView.findViewById(R.id.play_pause_button);
        youTubeButton = controlsView.findViewById(R.id.youtube_button);
        fullScreenButton = controlsView.findViewById(R.id.fullscreen_button);

        customActionLeft = controlsView.findViewById(R.id.custom_action_left_button);
        customActionRight = controlsView.findViewById(R.id.custom_action_right_button);

        youtubePlayerSeekBar = controlsView.findViewById(R.id.youtube_player_seekbar);

        youTubePlayer.addListener(youtubePlayerSeekBar);
        youTubePlayer.addListener(controlsContainer);

        youtubePlayerSeekBar.setYoutubePlayerSeekBarListener(this);
        panel.setOnClickListener(this);
        playPauseButton.setOnClickListener(this);
        menuButton.setOnClickListener(this);
        fullScreenButton.setOnClickListener(this);
    }

    @Override
    public void showVideoTitle(boolean show) {
        videoTitle.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setVideoTitle(@NonNull String title) {
        videoTitle.setText(title);
    }

    @Override
    public void showUI(boolean show) {
        controlsContainer.setDisabled(!show);
        controlsContainer.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showPlayPauseButton(boolean show) {
        playPauseButton.setVisibility(show ? View.VISIBLE : View.GONE);

        isPlayPauseButtonEnabled = show;
    }

    @Override
    public void enableLiveVideoUI(boolean enable) {
        youtubePlayerSeekBar.setVisibility(enable ? View.INVISIBLE : View.VISIBLE);
        liveVideoIndicator.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    /**
     * Set custom action to the left of the Play/Pause button
     */
    @Override
    public void setCustomAction1(@NonNull Drawable icon, View.OnClickListener clickListener) {
        customActionLeft.setImageDrawable(icon);
        customActionLeft.setOnClickListener(clickListener);
        showCustomAction1(true);
    }

    /**
     * Set custom action to the right of the Play/Pause button
     */
    @Override
    public void setCustomAction2(@NonNull Drawable icon, View.OnClickListener clickListener) {
        customActionRight.setImageDrawable(icon);
        customActionRight.setOnClickListener(clickListener);
        showCustomAction2(true);
    }

    public void showCustomAction1(boolean show) {
        isCustomActionLeftEnabled = show;
        customActionLeft.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void showCustomAction2(boolean show) {
        isCustomActionRightEnabled = show;
        customActionRight.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMenuButton(boolean show) {
        menuButton.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setMenuButtonClickListener(@NonNull View.OnClickListener customMenuButtonClickListener) {
        this.onMenuButtonClickListener = customMenuButtonClickListener;
    }

    @Override
    public void showCurrentTime(boolean show) {
        youtubePlayerSeekBar.getVideoCurrentTimeTextView().setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showDuration(boolean show) {
        youtubePlayerSeekBar.getVideoDurationTextView().setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showSeekBar(boolean show) {
        youtubePlayerSeekBar.getSeekBar().setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showBufferingProgress(boolean show) {
        youtubePlayerSeekBar.setShowBufferingProgress(show);
    }

    @Override
    public void showYouTubeButton(boolean show) {
        youTubeButton.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void addView(@NonNull View view) {
        extraViewsContainer.addView(view, 0);
    }

    @Override
    public void removeView(@NonNull View view) {
        extraViewsContainer.removeView(view);
    }

    @NonNull
    @Override
    public YouTubePlayerMenu getMenu() {
        return youTubePlayerMenu;
    }

    @Override
    public void setMenu(@NonNull YouTubePlayerMenu youTubePlayerMenu) {
        this.youTubePlayerMenu = youTubePlayerMenu;
    }

    @Override
    public void showFullscreenButton(boolean show) {
        fullScreenButton.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setFullScreenButtonClickListener(@NonNull View.OnClickListener customFullScreenButtonClickListener) {
        this.onFullScreenButtonListener = customFullScreenButtonClickListener;
    }

    @Override
    public void onClick(View view) {
        if(view == panel)
            controlsContainer.toggleVisibility();
        else if(view == playPauseButton)
            onPlayButtonPressed();
        else if(view == fullScreenButton)
            onFullScreenButtonPressed();
        else if(view == menuButton)
            onMenuButtonPressed();
    }

    private void onMenuButtonPressed() {
        if(onMenuButtonClickListener == null)
            youTubePlayerMenu.show(menuButton);
        else
            onMenuButtonClickListener.onClick(menuButton);
    }

    private void onFullScreenButtonPressed() {
        if(onFullScreenButtonListener == null)
            youTubePlayerView.toggleFullScreen();
        else
            onFullScreenButtonListener.onClick(fullScreenButton);
    }

    private void onPlayButtonPressed() {
        if(isPlaying)
            youTubePlayer.pause();
        else
            youTubePlayer.play();
    }

    @Override
    public void onYouTubePlayerEnterFullScreen() {
        fullScreenButton.setImageResource(R.drawable.ayp_ic_fullscreen_exit_24dp);
    }

    @Override
    public void onYouTubePlayerExitFullScreen() {
        fullScreenButton.setImageResource(R.drawable.ayp_ic_fullscreen_24dp);
    }

    private void updateState(PlayerConstants.PlayerState state) {
        switch (state) {
            case ENDED:
                isPlaying = false;
                break;
            case PAUSED:
                isPlaying = false;
                break;
            case PLAYING:
                isPlaying = true;
                break;
            case UNSTARTED:
                break;
            default:
                break;
        }

        updatePlayPauseButtonIcon(!isPlaying);
    }

    private void updatePlayPauseButtonIcon(boolean playing) {
        playPauseButton.setImageResource(playing ? R.drawable.ayp_ic_pause_36dp : R.drawable.ayp_ic_play_36dp);
    }

    @Override
    public void seekTo(float time) {
        youTubePlayer.seekTo(time);
    }

    // YouTubePlayer callbacks

    @Override
    public void onStateChange(@NonNull PlayerConstants.PlayerState state) {
        updateState(state);

        if(state == PlayerConstants.PlayerState.PLAYING || state == PlayerConstants.PlayerState.PAUSED || state == PlayerConstants.PlayerState.VIDEO_CUED) {
            panel.setBackgroundColor(ContextCompat.getColor(youTubePlayerView.getContext(), android.R.color.transparent));
            progressBar.setVisibility(View.GONE);

            if(isPlayPauseButtonEnabled) playPauseButton.setVisibility(View.VISIBLE);
            if(isCustomActionLeftEnabled) customActionLeft.setVisibility(View.VISIBLE);
            if(isCustomActionRightEnabled) customActionRight.setVisibility(View.VISIBLE);

            updatePlayPauseButtonIcon(state == PlayerConstants.PlayerState.PLAYING);

        } else {
            updatePlayPauseButtonIcon(false);

            if(state == PlayerConstants.PlayerState.BUFFERING) {
                panel.setBackgroundColor(ContextCompat.getColor(panel.getContext(), android.R.color.transparent));
                if(isPlayPauseButtonEnabled) playPauseButton.setVisibility(View.INVISIBLE);

                customActionLeft.setVisibility(View.GONE);
                customActionRight.setVisibility(View.GONE);
            }

            if(state == PlayerConstants.PlayerState.UNSTARTED) {
                progressBar.setVisibility(View.GONE);
                if(isPlayPauseButtonEnabled) playPauseButton.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onVideoId(@NonNull final String videoId) {
        youTubeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId + "#t=" + youtubePlayerSeekBar.getSeekBar().getProgress()));
                youTubeButton.getContext().startActivity(intent);
            }
        });
    }

    @Override public void onReady() { }
    @Override public void onPlaybackQualityChange(@NonNull PlayerConstants.PlaybackQuality playbackQuality) { }
    @Override public void onPlaybackRateChange(@NonNull PlayerConstants.PlaybackRate rate) { }
    @Override public void onError(@NonNull PlayerConstants.PlayerError error) { }
    @Override public void onApiChange() { }
    @Override public void onCurrentSecond(float second) { }
    @Override public void onVideoDuration(float duration) { }
    @Override public void onVideoLoadedFraction(float loadedFraction) { }
}
