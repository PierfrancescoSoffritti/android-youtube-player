package com.pierfrancescosoffritti.youtubeplayer;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
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

public class YouTubePlayer extends WebView {
    @NonNull private Set<YouTubeListener> youTubeListeners;
    @NonNull private final YouTubeListener innerYouTubeListener;

    @NonNull private YouTubePlayerParams params = new YouTubePlayerParams();

    public YouTubePlayer(Context context) {
        this(context, null);
    }

    public YouTubePlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YouTubePlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        youTubeListeners = new HashSet<>();
        innerYouTubeListener = new InnerYouTubePlayerListener();
    }

    public void initialize(String videoId, @Nullable YouTubeListener youTubeListener) {
        if(youTubeListener != null)
            this.youTubeListeners.add(youTubeListener);

        WebSettings set = this.getSettings();
        set.setJavaScriptEnabled(true);
        set.setCacheMode(WebSettings.LOAD_NO_CACHE);
        this.addJavascriptInterface(new YouTubePlayerBridge(this), "YouTubePlayerBridge");
        this.loadDataWithBaseURL("http://www.youtube.com", getVideoHTML(videoId), "text/html", "utf-8", null);
        this.setWebChromeClient(new WebChromeClient());
    }

    private String getVideoHTML(String videoId) {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.player);

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String read;
            StringBuilder sb = new StringBuilder("");

            while ((read = bufferedReader.readLine()) != null)
                sb.append(read).append("\n");

            inputStream.close();

            String html = sb.toString();
            html = html
                    .replace("[VIDEO_ID]", videoId)
                    .replace("[BG_COLOR]", "#000000")
                    .replace("[AUTO_PLAY]", String.valueOf(params.getAutoplay()))
                    .replace("[AUTO_HIDE]", String.valueOf(params.getAutohide()))
                    .replace("[REL]", String.valueOf(params.getRel()))
                    .replace("[SHOW_INFO]", String.valueOf(params.getShowinfo()))
                    .replace("[ENABLE_JS_API]", String.valueOf(params.getEnablejsapi()))
                    .replace("[DISABLE_KB]", String.valueOf(params.getDisablekb()))
                    .replace("[CC_LANG_PREF]", String.valueOf(params.getCc_lang_pref()))
                    .replace("[CONTROLS]", String.valueOf(params.getControls()));
            return html;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);

        if(params.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);

            this.getLayoutParams().height = (int) (displayMetrics.widthPixels * 0.5625);
        }
    }

    public void adjustHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.getLayoutParams().height = (int) (displayMetrics.widthPixels * 0.5625);
    }

    @NonNull
    public Set<YouTubeListener> getYouTubeListeners() {
        return youTubeListeners;
    }

    @NonNull
    protected YouTubeListener getInnerYouTubeListener() {
        return innerYouTubeListener;
    }

    public interface YouTubeListener {
        void onReady(@NonNull YouTubePlayer youTubePlayer);

        void onStateChange(@State.YouTubePlayerState int state, @NonNull YouTubePlayer youTubePlayer);

        void onPlaybackQualityChange(@PlaybackQuality.Quality int playbackQuality, @NonNull YouTubePlayer youTubePlayer);

        void onPlaybackRateChange(double rate, @NonNull YouTubePlayer youTubePlayer);

        void onError(String arg, @NonNull YouTubePlayer youTubePlayer);

        void onApiChange(String arg, @NonNull YouTubePlayer youTubePlayer);

        void onCurrentSecond(double second, @NonNull YouTubePlayer youTubePlayer);

        void onDuration(double duration, @NonNull YouTubePlayer youTubePlayer);

        void onLog(String log, @NonNull YouTubePlayer youTubePlayer);
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
}
