package com.pierfrancescosoffritti.youtubeplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashSet;
import java.util.Set;

/**
 * WebView implementing the actual YouTube Player
 */
public class YouTubePlayer extends WebView implements YouTubePlayerActions {

    @NonNull private Set<YouTubePlayerListener> youTubePlayerListeners;
    @NonNull private final Handler mainThreadHandler;

    @Nullable private PlayerStateTracker playerStateTracker;

    protected YouTubePlayer(Context context) {
        this(context, null);
    }

    protected YouTubePlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    protected YouTubePlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mainThreadHandler = new Handler(Looper.getMainLooper());
        youTubePlayerListeners = new HashSet<>();
    }

    protected void initialize(@Nullable YouTubePlayerListener youTubePlayerListener) {
        if(youTubePlayerListener != null)
            this.youTubePlayerListeners.add(youTubePlayerListener);

        playerStateTracker = new PlayerStateTracker();
        youTubePlayerListeners.add(playerStateTracker);

        initWebView();
    }

    @Override
    public void loadVideo(final String videoId, final float startSeconds) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:loadVideo('" +videoId +"', " +startSeconds +")");
            }
        });
    }

    @Override
    public void cueVideo(final String videoId, final float startSeconds) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:cueVideo('" +videoId +"', " +startSeconds +")");
            }
        });
    }

    @Override
    public void play() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:playVideo()");
            }
        });
    }

    @Override
    public void pause() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:pauseVideo()");
            }
        });
    }

    @Override
    public void mute() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:mute()");
            }
        });
    }

    @Override
    public void unMute() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:unMute()");
            }
        });
    }

    @Override
    public void seekTo(final int time) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:seekTo(" +time +")");
            }
        });
    }

    @Override
    @PlayerState.State
    public int getCurrentState() {
        if(playerStateTracker == null)
            throw new RuntimeException("Player not initialized.");

        return playerStateTracker.currentState;
    }

    @Override
    public void destroy() {
        mainThreadHandler.removeCallbacksAndMessages(null);
        super.destroy();
    }

    @NonNull
    protected Set<YouTubePlayerListener> getListeners() {
        return youTubePlayerListeners;
    }

    protected boolean addListener(YouTubePlayerListener listener) {
        return youTubePlayerListeners.add(listener);
    }

    protected boolean removeListener(YouTubePlayerListener listener) {
        return youTubePlayerListeners.remove(listener);
    }

    private void initWebView() {
        WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setMediaPlaybackRequiresUserGesture(false);

        this.addJavascriptInterface(new YouTubePlayerBridge(this), "YouTubePlayerBridge");
        this.loadDataWithBaseURL("https://www.youtube.com", readYouTubePlayerHTMLFromFile(), "text/html", "utf-8", null);

        // apparently there's a bug in the ChromeClient
        this.setWebChromeClient(new WebChromeClient() {
            @Override
            public Bitmap getDefaultVideoPoster() {
                Bitmap result = null;
                try {
                    result = super.getDefaultVideoPoster();
                } catch (Exception ignore) {
                }

                if(result == null)
                    return Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
                else
                    return result;
            }
        });
    }

    private String readYouTubePlayerHTMLFromFile() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.youtube_player);

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String read;
            StringBuilder sb = new StringBuilder();

            while ( ( read = bufferedReader.readLine() ) != null )
                sb.append(read).append("\n");
            inputStream.close();

            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Can't parse HTML file containing the player.");
        }
    }

    private class PlayerStateTracker extends AbstractYouTubePlayerListener {
        @PlayerState.State private int currentState;

        @Override
        public void onStateChange(@YouTubePlayer.PlayerState.State int state) {
            this.currentState = state;
        }
    }

    public interface YouTubePlayerListener {
        void onReady();
        void onStateChange(@PlayerState.State int state);
        void onPlaybackQualityChange(@PlaybackQuality.Quality int playbackQuality);
        void onPlaybackRateChange(@YouTubePlayer.PlaybackRate.Rate String playbackRate);
        void onError(@PlayerError.Error int error);
        void onApiChange();
        void onCurrentSecond(float second);
        void onVideoDuration(float duration);
        void onMessage(String log);
        void onVideoTitle(String videoTitle);
        void onVideoId(String videoId);
    }

    public static class PlayerState {
        public final static int UNKNOWN = -10;
        public final static int UNSTARTED = -1;
        public final static int ENDED = 0;
        public final static int PLAYING = 1;
        public final static int PAUSED = 2;
        public final static int BUFFERING = 3;
        public final static int VIDEO_CUED = 5;

        @IntDef({UNKNOWN, UNSTARTED, ENDED, PLAYING, PAUSED, BUFFERING, VIDEO_CUED})
        @Retention(RetentionPolicy.SOURCE)
        public @interface State {}
    }

    public static class PlaybackQuality {
        public final static int UNKNOWN = -10;
        public final static int SMALL = 0;
        public final static int MEDIUM = 1;
        public final static int LARGE = 2;
        public final static int HD720 = 3;
        public final static int HD1080 = 4;
        public final static int HIGH_RES = 5;
        public final static int DEFAULT = -1;

        @IntDef({UNKNOWN, SMALL, MEDIUM, LARGE, HD720, HD1080, HIGH_RES, DEFAULT})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Quality {}
    }

    public static class PlayerError {
        public final static int UNKNOWN = -10;
        public final static int INVALID_PARAMETER_IN_REQUEST = 0;
        public final static int HTML_5_PLAYER = 1;
        public final static int VIDEO_NOT_FOUND = 2;
        public final static int VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER = 3;

        @IntDef({UNKNOWN, INVALID_PARAMETER_IN_REQUEST, HTML_5_PLAYER, VIDEO_NOT_FOUND, VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Error {}
    }

    // @param rate 0.25, 0.5, 1, 1.5, 2
    public static class PlaybackRate {
        public final static String UNKNOWN = "-10";
        public final static String RATE_0_25 = "0.25";
        public final static String RATE_0_5 = "0.5";
        public final static String RATE_1 = "1";
        public final static String RATE_1_5 = "1.5";
        public final static String RATE_2 = "2";

        @StringDef({ UNKNOWN, RATE_0_25, RATE_0_5, RATE_1, RATE_1_5, RATE_2})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Rate {}
    }
}
