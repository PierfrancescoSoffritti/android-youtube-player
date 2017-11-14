package com.pierfrancescosoffritti.youtubeplayer.player;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.pierfrancescosoffritti.youtubeplayer.R;
import com.pierfrancescosoffritti.youtubeplayer.utils.NetworkReceiver;
import com.pierfrancescosoffritti.youtubeplayer.player.playerUtils.PlaybackResumer;
import com.pierfrancescosoffritti.youtubeplayer.ui.DefaultPlayerUIController;
import com.pierfrancescosoffritti.youtubeplayer.ui.PlayerUIController;
import com.pierfrancescosoffritti.youtubeplayer.utils.Callable;
import com.pierfrancescosoffritti.youtubeplayer.player.playerUtils.FullScreenHelper;
import com.pierfrancescosoffritti.youtubeplayer.utils.Utils;

public class YouTubePlayerView extends FrameLayout implements NetworkReceiver.NetworkListener {

    @NonNull private final WebViewYouTubePlayer youTubePlayer;
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

        youTubePlayer = new WebViewYouTubePlayer(context);
        addView(youTubePlayer, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        View playerControlsView = inflate(context, R.layout.player_controls, this);
        playerUIControls = new DefaultPlayerUIController(this, youTubePlayer, playerControlsView);

        playbackResumer = new PlaybackResumer();
        networkReceiver = new NetworkReceiver(this);
        fullScreenHelper = new FullScreenHelper();

        addFullScreenListener(playerUIControls);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // if height == wrap content make the view 16:9
        if(getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            int sixteenNineHeight = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthMeasureSpec) * 9 / 16, View.MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, sixteenNineHeight);
        } else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Initialize the player
     * @param youTubePlayerInitListener lister for player init events
     * @param handleNetworkEvents if <b>true</b> a broadcast receiver will be registered.<br/>If <b>false</b> you should handle network events with your own broadcast receiver. See {@link YouTubePlayerView#onNetworkAvailable()} and {@link YouTubePlayerView#onNetworkUnavailable()}
     */
    public void initialize(@NonNull final YouTubePlayerInitListener youTubePlayerInitListener, boolean handleNetworkEvents) {
        if(handleNetworkEvents)
            getContext().registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        asyncInitialization = new Callable() {
            @Override
            public void call() {
                youTubePlayer.initialize(new YouTubePlayerInitListener() {
                    @Override
                    public void onInitSuccess(YouTubePlayer youTubePlayer) {
                        addYouTubePlayerInternalListeners(youTubePlayer);
                        youTubePlayerInitListener.onInitSuccess(youTubePlayer);
                    }
                });
            }
        };

        if(Utils.isOnline(getContext()))
            asyncInitialization.call();
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
            playbackResumer.resume(youTubePlayer);
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

    public void showMenu(View anchorView) {
        PopupWindow window = createPopupDisplay();
        window.showAsDropDown(anchorView, - getResources().getDimensionPixelSize(R.dimen._8dp) * 3, - getResources().getDimensionPixelSize(R.dimen._8dp) * 4);
    }

    public PopupWindow createPopupDisplay() {
        final PopupWindow popupWindow = new PopupWindow(this);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(inflater == null)
            return null;

        View view = inflater.inflate(R.layout.player_menu, null);

        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);

        return popupWindow;
    }

    private void addYouTubePlayerInternalListeners(YouTubePlayer youTubePlayer) {
        youTubePlayer.addListener(playerUIControls);
        youTubePlayer.addListener(playbackResumer);

        youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady() {
                asyncInitialization = null;
            }
        });
    }
}
