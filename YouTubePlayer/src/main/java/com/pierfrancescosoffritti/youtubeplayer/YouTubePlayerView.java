package com.pierfrancescosoffritti.youtubeplayer;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

public class YouTubePlayerView extends FrameLayout implements YouTubePlayerActions, NetworkReceiver.NetworkListener {

    @NonNull private final NetworkReceiver networkReceiver;

    @NonNull private final YouTubePlayer youTubePlayer;
    @NonNull private final PlayerControlsWrapper playerControlsWrapper;
    @NonNull private final PlaybackResumer playbackResumer;
    @NonNull private final FullScreenHandler fullScreenHandler;
    @Nullable private Callable asyncInitialization;

    public YouTubePlayerView(Context context) {
        this(context, null);
    }

    public YouTubePlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YouTubePlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        fullScreenHandler = new FullScreenHandler();

        youTubePlayer = new YouTubePlayer(context);
        addView(youTubePlayer, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        View playerControls = inflate(context, R.layout.player_controls, this);
        playerControlsWrapper = new PlayerControlsWrapper(this, playerControls);

        playbackResumer = new PlaybackResumer(this);

        networkReceiver = new NetworkReceiver(this);

        addFullScreenListener(playerControlsWrapper);

        youTubePlayer.addListener(playerControlsWrapper);
        youTubePlayer.addListener(playbackResumer);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // if wrap content make the view 16:9
        if(getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            int sixteenNineHeight = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthMeasureSpec) * 9 / 16, View.MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, sixteenNineHeight);
        } else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Initialize the player
     * @param youTubePlayerListener lister for player events
     * @param handleNetworkEvents if <b>true</b> a broadcast receiver will be registered.<br/>If <b>false</b> you should handle network events with your own broadcast receiver. See {@link YouTubePlayerView#onNetworkAvailable()} and {@link YouTubePlayerView#onNetworkUnavailable()}
     */
    public void initialize(@Nullable final YouTubePlayer.YouTubePlayerListener youTubePlayerListener, boolean handleNetworkEvents) {
        if(handleNetworkEvents)
            getContext().registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        if(!Utils.isOnline(getContext())) {
            Log.e("YouTubePlayerView", "Can't initialize because device is not connected to the internet.");

            asyncInitialization = new Callable() {
                @Override
                public void call() {
                    Log.d("YouTubePlayerView", "Network available. Initializing player.");
                    youTubePlayer.initialize(youTubePlayerListener);
                    asyncInitialization = null;
                }
            };

        } else
            youTubePlayer.initialize(youTubePlayerListener);
    }

    public void addYouTubePlayerListener(YouTubePlayer.YouTubePlayerListener youTubePlayerListener) {
        youTubePlayer.addListener(youTubePlayerListener);
    }

    public void removeYouTubePlayerListener(YouTubePlayer.YouTubePlayerListener youTubePlayerListener) {
        youTubePlayer.removeListener(youTubePlayerListener);
    }

    @Override
    public void loadVideo(String videoId, float startSecond) {
        youTubePlayer.loadVideo(videoId, startSecond);
        playerControlsWrapper.reset();
    }

    @Override
    public void cueVideo(String videoId, float startSeconds) {
        youTubePlayer.cueVideo(videoId, startSeconds);
        playerControlsWrapper.reset();
    }

    @Override
    public void play() {
        youTubePlayer.play();
    }

    @Override
    public void pause() {
        youTubePlayer.pause();
    }

    @Override
    public void mute() {
        youTubePlayer.mute();
    }

    @Override
    public void unMute() {
        youTubePlayer.unMute();
    }

    @Override
    public void seekTo(int time) {
        youTubePlayer.seekTo(time);
    }

    @Override
    @YouTubePlayer.PlayerState.State
    public int getCurrentState() {
        return youTubePlayer.getCurrentState();
    }

    /**
     * Calls {@link WebView#destroy()} on the player. And unregisters the broadcast receiver (for network events), if registered.
     * Call this method before destroying the host Fragment/Activity
     */
    public void release() {
        youTubePlayer.destroy();
        try {
            getContext().unregisterReceiver(networkReceiver);
        } catch (Exception ignore) {
        }
    }

    @Override
    public void onNetworkAvailable() {
        if(asyncInitialization != null)
            asyncInitialization.call();
        else
            playbackResumer.resume();
    }

    @Override
    public void onNetworkUnavailable() {
    }

    public void showTitle(boolean show) {
        playerControlsWrapper.showTitle(show);
    }

    public void setCustomActionRight(Drawable icon, View.OnClickListener clickListener) {
        playerControlsWrapper.setCustomActionRight(icon, clickListener);
    }

    public void setCustomActionLeft(Drawable icon, View.OnClickListener clickListener) {
        playerControlsWrapper.setCustomActionLeft(icon, clickListener);
    }

    public void showFullScreenButton(boolean show) {
        playerControlsWrapper.showFullscreenButton(show);
    }

    public void hideUI(boolean hide) {
        playerControlsWrapper.hideUI(hide);
    }

    public void setFullScreenButtonListener(OnClickListener listener) {
        playerControlsWrapper.setOnFullScreenButtonListener(listener);
    }

    public void enterFullScreen() {
        fullScreenHandler.enterFullScreen(this);
    }

    public void exitFullScreen() {
        fullScreenHandler.exitFullScreen(this);
    }

    public boolean isFullScreen() {
        return fullScreenHandler.isFullScreen();
    }

    public void toggleFullScreen() {
        fullScreenHandler.toggleFullScreen(this);
    }

    public boolean addFullScreenListener(@NonNull YouTubePlayerFullScreenListener fullScreenListener) {
        return fullScreenHandler.addFullScreenListener(fullScreenListener);
    }

    public boolean removeFullScreenListener(@NonNull YouTubePlayerFullScreenListener fullScreenListener) {
        return fullScreenHandler.removeFullScreenListener(fullScreenListener);
    }
}
