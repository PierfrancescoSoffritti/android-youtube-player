package com.pierfrancescosoffritti.youtubeplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

    public interface YouTubeListener {
        void onReady();
        void onStateChange(@State.YouTubePlayerState int state);
        void onPlaybackQualityChange(@PlaybackQuality.Quality int playbackQuality);
        void onPlaybackRateChange(double rate);
        void onError(@Error.PlayerError int error);
        void onApiChange();
        void onCurrentSecond(float second);
        void onVideoDuration(float duration);
        void onLog(String log);
        void onVideoTitle(String videoTitle);
        void onVideoId(String videoId);
    }

    public static class State {
        public final static int UNSTARTED = -1;
        public final static int ENDED = 0;
        public final static int PLAYING = 1;
        public final static int PAUSED = 2;
        public final static int BUFFERING = 3;
        public final static int VIDEO_CUED = 5;

        @IntDef({UNSTARTED, ENDED, PLAYING, PAUSED, BUFFERING, VIDEO_CUED})
        @Retention(RetentionPolicy.SOURCE)
        public @interface YouTubePlayerState {}
    }

    public static class PlaybackQuality {
        public final static int SMALL = 0;
        public final static int MEDIUM = 1;
        public final static int LARGE = 2;
        public final static int HD720 = 3;
        public final static int HD1080 = 4;
        public final static int HIGH_RES = 5;
        public final static int DEFAULT = -1;

        @IntDef({SMALL, MEDIUM, LARGE, HD720, HD1080, HIGH_RES, DEFAULT})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Quality {}
    }

    public static class Error {
        public final static int INVALID_PARAMENTER_IN_REQUEST = 0;
        public final static int HTML_5_PLAYER = 1;
        public final static int VIDEO_NOT_FOUND = 2;
        public final static int VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER = 3;

        @IntDef({INVALID_PARAMENTER_IN_REQUEST, HTML_5_PLAYER, VIDEO_NOT_FOUND, VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER})
        @Retention(RetentionPolicy.SOURCE)
        public @interface PlayerError {}
    }
}
