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
public class YouTubePlayer extends WebView {

    @NonNull private Set<YouTubeListener> youTubeListeners;
    @NonNull private final Handler mainThreadHandler;

    protected YouTubePlayer(Context context) {
        this(context, null);
    }

    protected YouTubePlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    protected YouTubePlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mainThreadHandler = new Handler(Looper.getMainLooper());
        youTubeListeners = new HashSet<>();
    }

    protected void initialize(@Nullable YouTubeListener youTubeListener) {
        if(youTubeListener != null)
            this.youTubeListeners.add(youTubeListener);

        WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setMediaPlaybackRequiresUserGesture(false);
        this.addJavascriptInterface(new YouTubePlayerBridge(this), "YouTubePlayerBridge");
        this.loadDataWithBaseURL("https://www.youtube.com", getVideoPlayerHTML(), "text/html", "utf-8", null);

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

    private String getVideoPlayerHTML() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.player);

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String read;
            StringBuilder sb = new StringBuilder("");

            while ((read = bufferedReader.readLine()) != null)
                sb.append(read).append("\n");

            inputStream.close();

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * This function loads and plays the specified video.
     * @param videoId
     * @param startSeconds the time from which the video should start playing
     */
    protected void loadVideo(final String videoId, final float startSeconds) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:loadVideo('" +videoId +"', " +startSeconds +")");
            }
        });
    }

    /**
     * This function loads the specified video's thumbnail and prepares the player to play the video.
     * @param videoId
     * @param startSeconds the time from which the video should start playing
     */
    protected void cueVideo(final String videoId, final float startSeconds) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:cueVideo('" +videoId +"', " +startSeconds +")");
            }
        });
    }

    protected void play() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:playVideo()");
            }
        });
    }

    protected void pause() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:pauseVideo()");
            }
        });
    }

    protected void mute() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:mute()");
            }
        });
    }

    protected void unMute() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:unMute()");
            }
        });
    }

    protected void seekTo(final int time) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:seekTo(" +time +")");
            }
        });
    }

    @NonNull
    protected Set<YouTubeListener> getListeners() {
        return youTubeListeners;
    }

    protected boolean addListener(YouTubeListener listener) {
        return youTubeListeners.add(listener);
    }

    protected boolean removeListener(YouTubeListener listener) {
        return youTubeListeners.remove(listener);
    }

    @Override
    public void destroy() {
        mainThreadHandler.removeCallbacksAndMessages(null);
        super.destroy();
    }

    public interface YouTubeListener {
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
        public final static int UNKNOWN = -1;
        public final static int UNSTARTED = 0;
        public final static int ENDED = 1;
        public final static int PLAYING = 2;
        public final static int PAUSED = 3;
        public final static int BUFFERING = 4;
        public final static int VIDEO_CUED = 5;

        @IntDef({UNKNOWN, UNSTARTED, ENDED, PLAYING, PAUSED, BUFFERING, VIDEO_CUED})
        @Retention(RetentionPolicy.SOURCE)
        public @interface State {}
    }

    public static class PlaybackQuality {
        public final static int UNKNOWN = -1;
        public final static int SMALL = 0;
        public final static int MEDIUM = 1;
        public final static int LARGE = 2;
        public final static int HD720 = 3;
        public final static int HD1080 = 4;
        public final static int HIGH_RES = 5;
        public final static int DEFAULT = 6;

        @IntDef({UNKNOWN, SMALL, MEDIUM, LARGE, HD720, HD1080, HIGH_RES, DEFAULT})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Quality {}
    }

    public static class PlayerError {
        public final static int UNKNOWN = -1;
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
        public final static String UNKNOWN = "-1";
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
