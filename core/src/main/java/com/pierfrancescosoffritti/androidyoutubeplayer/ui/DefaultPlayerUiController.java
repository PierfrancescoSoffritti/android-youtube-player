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
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.views.LegacyYouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.menu.YouTubePlayerMenu;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.menu.defaultMenu.DefaultYouTubePlayerMenu;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.utils.FadeViewHelper;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.views.YouTubePlayerSeekBar;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.views.YouTubePlayerSeekBarListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class DefaultPlayerUiController implements PlayerUiController, YouTubePlayerListener, YouTubePlayerFullScreenListener, View.OnClickListener, YouTubePlayerSeekBarListener {
    @NonNull private final LegacyYouTubePlayerView youTubePlayerView;
    @NonNull private final YouTubePlayer youTubePlayer;

    @NonNull private YouTubePlayerMenu youTubePlayerMenu;

    /**
     * View used for for intercepting clicks and for drawing a black background.
     * Could have used controlsContainer, but in this way I'm able to hide all the control at once by hiding controlsContainer
     */
    private View panel;

    private View controlsContainer;
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

    private FadeViewHelper fadeControlsContainer;

    private boolean isPlaying = false;

    private boolean isPlayPauseButtonEnabled = true;
    private boolean isCustomActionLeftEnabled = false;
    private boolean isCustomActionRightEnabled = false;

    public DefaultPlayerUiController(@NonNull LegacyYouTubePlayerView youTubePlayerView, @NonNull YouTubePlayer youTubePlayer) {
        this.youTubePlayerView = youTubePlayerView;
        this.youTubePlayer = youTubePlayer;

        View defaultPlayerUi = View.inflate(youTubePlayerView.getContext(), R.layout.ayp_default_player_ui, youTubePlayerView);
        initViews(defaultPlayerUi);

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

        fadeControlsContainer = new FadeViewHelper(controlsContainer);

        youTubePlayer.addListener(youtubePlayerSeekBar);
        youTubePlayer.addListener(fadeControlsContainer);

        youtubePlayerSeekBar.setYoutubePlayerSeekBarListener(this);
        panel.setOnClickListener(this);
        playPauseButton.setOnClickListener(this);
        menuButton.setOnClickListener(this);
        fullScreenButton.setOnClickListener(this);
    }

    @Override
    public PlayerUiController showVideoTitle(boolean show) {
        videoTitle.setVisibility(show ? View.VISIBLE : View.GONE);
        return this;
    }

    @Override
    public PlayerUiController setVideoTitle(@NonNull String title) {
        videoTitle.setText(title);
        return this;
    }

    @Override
    public PlayerUiController showUi(boolean show) {
        fadeControlsContainer.setDisabled(!show);
        controlsContainer.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        return this;
    }

    @Override
    public PlayerUiController showPlayPauseButton(boolean show) {
        playPauseButton.setVisibility(show ? View.VISIBLE : View.GONE);

        isPlayPauseButtonEnabled = show;
        return this;
    }

    @Override
    public PlayerUiController enableLiveVideoUi(boolean enable) {
        youtubePlayerSeekBar.setVisibility(enable ? View.INVISIBLE : View.VISIBLE);
        liveVideoIndicator.setVisibility(enable ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * Set custom action to the left of the Play/Pause button
     */
    @Override
    public PlayerUiController setCustomAction1(@NonNull Drawable icon, View.OnClickListener clickListener) {
        customActionLeft.setImageDrawable(icon);
        customActionLeft.setOnClickListener(clickListener);
        showCustomAction1(true);
        return this;
    }

    /**
     * Set custom action to the right of the Play/Pause button
     */
    @Override
    public PlayerUiController setCustomAction2(@NonNull Drawable icon, View.OnClickListener clickListener) {
        customActionRight.setImageDrawable(icon);
        customActionRight.setOnClickListener(clickListener);
        showCustomAction2(true);
        return this;
    }

    public PlayerUiController showCustomAction1(boolean show) {
        isCustomActionLeftEnabled = show;
        customActionLeft.setVisibility(show ? View.VISIBLE : View.GONE);
        return this;
    }

    public PlayerUiController showCustomAction2(boolean show) {
        isCustomActionRightEnabled = show;
        customActionRight.setVisibility(show ? View.VISIBLE : View.GONE);
        return this;
    }

    @Override
    public PlayerUiController showMenuButton(boolean show) {
        menuButton.setVisibility(show ? View.VISIBLE : View.GONE);
        return this;
    }

    @Override
    public PlayerUiController setMenuButtonClickListener(@NonNull View.OnClickListener customMenuButtonClickListener) {
        this.onMenuButtonClickListener = customMenuButtonClickListener;
        return this;
    }

    @Override
    public PlayerUiController showCurrentTime(boolean show) {
        youtubePlayerSeekBar.getVideoCurrentTimeTextView().setVisibility(show ? View.VISIBLE : View.GONE);
        return this;
    }

    @Override
    public PlayerUiController showDuration(boolean show) {
        youtubePlayerSeekBar.getVideoDurationTextView().setVisibility(show ? View.VISIBLE : View.GONE);
        return this;
    }

    @Override
    public PlayerUiController showSeekBar(boolean show) {
        youtubePlayerSeekBar.getSeekBar().setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        return this;
    }

    @Override
    public PlayerUiController showBufferingProgress(boolean show) {
        youtubePlayerSeekBar.setShowBufferingProgress(show);
        return this;
    }

    @Override
    public PlayerUiController showYouTubeButton(boolean show) {
        youTubeButton.setVisibility(show ? View.VISIBLE : View.GONE);
        return this;
    }

    @Override
    public PlayerUiController addView(@NonNull View view) {
        extraViewsContainer.addView(view, 0);
        return this;
    }

    @Override
    public PlayerUiController removeView(@NonNull View view) {
        extraViewsContainer.removeView(view);
        return this;
    }

    @NonNull
    @Override
    public YouTubePlayerMenu getMenu() {
        return youTubePlayerMenu;
    }

    @Override
    public PlayerUiController setMenu(@NonNull YouTubePlayerMenu youTubePlayerMenu) {
        this.youTubePlayerMenu = youTubePlayerMenu;
        return this;
    }

    @Override
    public PlayerUiController showFullscreenButton(boolean show) {
        fullScreenButton.setVisibility(show ? View.VISIBLE : View.GONE);
        return this;
    }

    @Override
    public PlayerUiController setFullScreenButtonClickListener(@NonNull View.OnClickListener customFullScreenButtonClickListener) {
        this.onFullScreenButtonListener = customFullScreenButtonClickListener;
        return this;
    }

    @Override
    public void onClick(View view) {
        if(view == panel)
            fadeControlsContainer.toggleVisibility();
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
    public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
        updateState(state);

        if(state == PlayerConstants.PlayerState.PLAYING || state == PlayerConstants.PlayerState.PAUSED || state == PlayerConstants.PlayerState.VIDEO_CUED) {
            panel.setBackgroundColor(ContextCompat.getColor(panel.getContext(), android.R.color.transparent));
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
    public void onVideoId(@NonNull YouTubePlayer youTubePlayer, @NonNull final String videoId) {
        youTubeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId + "#t=" + youtubePlayerSeekBar.getSeekBar().getProgress()));
                youTubeButton.getContext().startActivity(intent);
            }
        });
    }

    @Override public void onReady(@NonNull YouTubePlayer youTubePlayer) { }
    @Override public void onPlaybackQualityChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlaybackQuality playbackQuality) { }
    @Override public void onPlaybackRateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlaybackRate rate) { }
    @Override public void onError(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError error) { }
    @Override public void onApiChange(@NonNull YouTubePlayer youTubePlayer) { }
    @Override public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float second) { }
    @Override public void onVideoDuration(@NonNull YouTubePlayer youTubePlayer, float duration) { }
    @Override public void onVideoLoadedFraction(@NonNull YouTubePlayer youTubePlayer, float loadedFraction) { }
}
