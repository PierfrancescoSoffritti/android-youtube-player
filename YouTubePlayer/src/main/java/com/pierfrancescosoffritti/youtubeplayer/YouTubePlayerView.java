package com.pierfrancescosoffritti.youtubeplayer;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.pierfrancescosoffritti.youtubeplayer.playerUtils.PlaybackResumer;
import com.pierfrancescosoffritti.youtubeplayer.ui.DefaultPlayerUIController;
import com.pierfrancescosoffritti.youtubeplayer.ui.PlayerUIController;
import com.pierfrancescosoffritti.youtubeplayer.utils.Callable;
import com.pierfrancescosoffritti.youtubeplayer.playerUtils.FullScreenHelper;
import com.pierfrancescosoffritti.youtubeplayer.utils.Utils;

public class YouTubePlayerView extends FrameLayout implements YouTubePlayerActions, NetworkReceiver.NetworkListener {

    @NonNull private final YouTubePlayer youTubePlayer;
    @NonNull private final DefaultPlayerUIController playerUIControls;

    @NonNull private final NetworkReceiver networkReceiver;
    @NonNull private final PlaybackResumer playbackResumer;
    @NonNull private final FullScreenHelper fullScreenHelper;
    @Nullable private Callable asyncInitialization;

    public YouTubePlayerView(Context context) {
        this(context, null);
    }

    public YouTubePlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YouTubePlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        youTubePlayer = new YouTubePlayer(context);
        addView(youTubePlayer, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        View playerControlsView = inflate(context, R.layout.player_controls, this);
        playerUIControls = new DefaultPlayerUIController(this, playerControlsView);

        playbackResumer = new PlaybackResumer(this);
        networkReceiver = new NetworkReceiver(this);
        fullScreenHelper = new FullScreenHelper();

        addFullScreenListener(playerUIControls);

        youTubePlayer.addListener(playerUIControls);
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

        asyncInitialization = new Callable() {
            @Override
            public void call() {
                youTubePlayer.initialize(youTubePlayerListener);
                asyncInitialization = null;
            }
        };

        if(Utils.isOnline(getContext()))
            asyncInitialization.call();
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
    }

    @Override
    public void cueVideo(String videoId, float startSeconds) {
        youTubePlayer.cueVideo(videoId, startSeconds);
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

    @NonNull
    public PlayerUIController getPlayerUIController() {
        return playerUIControls;
    }

    public void enterFullScreen() {
        fullScreenHelper.enterFullScreen(this);
    }

    public void exitFullScreen() {
        fullScreenHelper.exitFullScreen(this);
    }

    public boolean isFullScreen() {
        return fullScreenHelper.isFullScreen();
    }

    public void toggleFullScreen() {
        fullScreenHelper.toggleFullScreen(this);
    }

    public boolean addFullScreenListener(@NonNull YouTubePlayerFullScreenListener fullScreenListener) {
        return fullScreenHelper.addFullScreenListener(fullScreenListener);
    }

    public boolean removeFullScreenListener(@NonNull YouTubePlayerFullScreenListener fullScreenListener) {
        return fullScreenHelper.removeFullScreenListener(fullScreenListener);
    }
}
