package com.pierfrancescosoffritti.androidyoutubeplayer.player;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.pierfrancescosoffritti.androidyoutubeplayer.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.utils.FullScreenHelper;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.utils.PlaybackResumer;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.DefaultPlayerUIController;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.PlayerUIController;
import com.pierfrancescosoffritti.androidyoutubeplayer.utils.NetworkReceiver;
import com.pierfrancescosoffritti.androidyoutubeplayer.utils.SixteenNineRatioFrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class YouTubePlayerView extends SixteenNineRatioFrameLayout implements NetworkReceiver.NetworkListener, LifecycleObserver {

    @NonNull private final WebViewYouTubePlayer youTubePlayer;
    @Nullable private DefaultPlayerUIController defaultPlayerUIController;

    @NonNull private final NetworkReceiver networkReceiver;
    @NonNull private final PlaybackResumer playbackResumer;
    @NonNull private final FullScreenHelper fullScreenHelper;

    private boolean isInitialized = false;
    @Nullable private Runnable asyncInitialization;

    public YouTubePlayerView(Context context) {
        this(context, null);
    }

    public YouTubePlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YouTubePlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        youTubePlayer = new WebViewYouTubePlayer(context);
        addView(youTubePlayer, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        defaultPlayerUIController = new DefaultPlayerUIController(this, youTubePlayer);

        playbackResumer = new PlaybackResumer();
        networkReceiver = new NetworkReceiver(this);
        fullScreenHelper = new FullScreenHelper(this);

        fullScreenHelper.addFullScreenListener(defaultPlayerUIController);
        addYouTubePlayerListeners(youTubePlayer);
    }

    /**
     * Initialize the player. You must call this method before using the player.
     * @param youTubePlayerListener listener for player events
     * @param handleNetworkEvents if <b>true</b> a broadcast receiver will be registered.<br/>If <b>false</b> you should handle network events with your own broadcast receiver. See {@link YouTubePlayerView#onNetworkAvailable()} and {@link YouTubePlayerView#onNetworkUnavailable()}
     * @param playerOptions customizable options for the embedded video player, can be null.
     */
    public void initialize(@NonNull final YouTubePlayerListener youTubePlayerListener, boolean handleNetworkEvents, @Nullable final IFramePlayerOptions playerOptions) {
        if(handleNetworkEvents)
            getContext().registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        asyncInitialization = new Runnable() {
            @Override
            public void run() {
                youTubePlayer.initialize(new YouTubePlayerInitListener() {
                    @Override
                    public void onInitSuccess(@NonNull YouTubePlayer youTubePlayer) {
                        youTubePlayer.addListener(youTubePlayerListener);
                    }
                }, playerOptions);
            }
        };

//        if(Utils.isOnline(getContext()))
//            asyncInitialization.call();
    }

    /**
     * Initialize the player using the provided {@link IFramePlayerOptions} object.
     * @see YouTubePlayerView#initialize(YouTubePlayerListener, boolean, IFramePlayerOptions)
     */
    public void initialize(@NonNull final YouTubePlayerListener youTubePlayerListener, boolean handleNetworkEvents) {
        initialize(youTubePlayerListener, handleNetworkEvents, null);
    }

    /**
     * Initialize the player. Network events are automatically handled by the player.
     * @see YouTubePlayerView#initialize(YouTubePlayerListener, boolean, IFramePlayerOptions)
     */
    public void initialize(@NonNull final YouTubePlayerListener youTubePlayerListener) {
        initialize(youTubePlayerListener, true);
    }

    /**
     * Initialize a player using the web-base UI instead pf the native UI.
     * The default PlayerUIController will be removed and {@link YouTubePlayerView#getPlayerUIController()} will throw exception.
     * @see YouTubePlayerView#initialize(YouTubePlayerListener, boolean, IFramePlayerOptions)
     */
    public void initializeWithWebUI(@NonNull final YouTubePlayerListener youTubePlayerListener, boolean handleNetworkEvents) {
        IFramePlayerOptions iFramePlayerOptions = new IFramePlayerOptions.Builder().controls(1).build();
        inflateCustomPlayerUI(R.layout.ayp_empty_layout);
        initialize(youTubePlayerListener, handleNetworkEvents, iFramePlayerOptions);
    }

    /**
     * Calls {@link WebView#destroy()} on the player. And unregisters the broadcast receiver (for network events), if registered.
     * Call this method before destroying the host Fragment/Activity, or register this View as an observer of its host lifecycle
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void release() {
        removeView(youTubePlayer);
        youTubePlayer.removeAllViews();
        youTubePlayer.destroy();
        try {
            getContext().unregisterReceiver(networkReceiver);
        } catch (Exception ignore) {
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop() {
        youTubePlayer.pause();
    }

    @Override
    public void onNetworkAvailable() {
        if(!isInitialized) {
            asyncInitialization.run();
        } else {
            playbackResumer.resume(youTubePlayer);
        }
    }

    @Override
    public void onNetworkUnavailable() {
    }

    public void enableBackgroundPlayback(boolean enable) {
        youTubePlayer.backgroundPlaybackEnabled = enable;
    }

    @NonNull
    public PlayerUIController getPlayerUIController() {
        if(defaultPlayerUIController == null)
            throw new RuntimeException("You have inflated a custom player UI. You must manage it with your own controller.");

        return defaultPlayerUIController;
    }

    /**
     * Replaces the default UI of the player with a custom UI.<br/>
     * You will have to control the custom UI in your application,
     * the default controller obtained through {@link YouTubePlayerView#getPlayerUIController()} won't be available anymore.
     * @param customPlayerUILayoutID the ID of the layout defining the custom UI.
     * @return The inflated View
     */
    public View inflateCustomPlayerUI(@LayoutRes int customPlayerUILayoutID) {
        removeViews(1, this.getChildCount()-1);

        if (defaultPlayerUIController != null) {
            youTubePlayer.removeListener(defaultPlayerUIController);
            fullScreenHelper.removeFullScreenListener(defaultPlayerUIController);
        }

        defaultPlayerUIController = null;

        return View.inflate(getContext(), customPlayerUILayoutID, this);
    }

    public void enterFullScreen() {
        fullScreenHelper.enterFullScreen();
    }

    public void exitFullScreen() {
        fullScreenHelper.exitFullScreen();
    }

    public boolean isFullScreen() {
        return fullScreenHelper.isFullScreen();
    }

    public void toggleFullScreen() {
        fullScreenHelper.toggleFullScreen();
    }

    public boolean addFullScreenListener(@NonNull YouTubePlayerFullScreenListener fullScreenListener) {
        return fullScreenHelper.addFullScreenListener(fullScreenListener);
    }

    public boolean removeFullScreenListener(@NonNull YouTubePlayerFullScreenListener fullScreenListener) {
        return fullScreenHelper.removeFullScreenListener(fullScreenListener);
    }

    private void addYouTubePlayerListeners(final YouTubePlayer youTubePlayer) {
        if (defaultPlayerUIController != null) youTubePlayer.addListener(defaultPlayerUIController);
        youTubePlayer.addListener(playbackResumer);
        youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer1) {
                isInitialized = true;
            }
        });
    }
}
