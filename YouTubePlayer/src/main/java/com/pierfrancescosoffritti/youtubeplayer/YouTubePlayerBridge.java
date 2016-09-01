package com.pierfrancescosoffritti.youtubeplayer;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.webkit.JavascriptInterface;

import java.util.HashSet;
import java.util.Set;

/**
 * WEB TO APP bridge
 */
public class YouTubePlayerBridge {

    @NonNull private final YouTubePlayer youTubePlayer;
    @NonNull private final Handler mainThreadHandler;

    public YouTubePlayerBridge(@NonNull YouTubePlayer youTubePlayer) {
        this.youTubePlayer = youTubePlayer;
        mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    @JavascriptInterface
    public void onReady() {
        for(YouTubePlayer.YouTubeListener listener : getListeners())
            listener.onReady(youTubePlayer);
    }

    @JavascriptInterface
    public void onStateChange(final String state) {
        System.out.println(state);
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayer.YouTubeListener listener : getListeners())
                    if ("UNSTARTED".equalsIgnoreCase(state))
                        listener.onStateChange(YouTubePlayer.State.UNSTARTED, youTubePlayer);
                    else if ("ENDED".equalsIgnoreCase(state))
                        listener.onStateChange(YouTubePlayer.State.ENDED, youTubePlayer);
                    else if ("PLAYING".equalsIgnoreCase(state))
                        listener.onStateChange(YouTubePlayer.State.PLAYING, youTubePlayer);
                    else if ("PAUSED".equalsIgnoreCase(state))
                        listener.onStateChange(YouTubePlayer.State.PAUSED, youTubePlayer);
                    else if ("BUFFERING".equalsIgnoreCase(state))
                        listener.onStateChange(YouTubePlayer.State.BUFFERING, youTubePlayer);
                    else if ("CUED".equalsIgnoreCase(state))
                        listener.onStateChange(YouTubePlayer.State.VIDEO_CUED, youTubePlayer);
            }
        });
    }

    @JavascriptInterface
    public void onPlaybackQualityChange(final String quality) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayer.YouTubeListener listener : getListeners())
                    if ("small".equalsIgnoreCase(quality))
                        listener.onPlaybackQualityChange(YouTubePlayer.PlaybackQuality.SMALL, youTubePlayer);
                    else if ("medium".equalsIgnoreCase(quality))
                        listener.onPlaybackQualityChange(YouTubePlayer.PlaybackQuality.MEDIUM, youTubePlayer);
                    else if ("large".equalsIgnoreCase(quality))
                        listener.onPlaybackQualityChange(YouTubePlayer.PlaybackQuality.LARGE, youTubePlayer);
                    else if ("hd720".equalsIgnoreCase(quality))
                        listener.onPlaybackQualityChange(YouTubePlayer.PlaybackQuality.HD720, youTubePlayer);
                    else if ("hd1080".equalsIgnoreCase(quality))
                        listener.onPlaybackQualityChange(YouTubePlayer.PlaybackQuality.HD1080, youTubePlayer);
                    else if ("highres".equalsIgnoreCase(quality))
                        listener.onPlaybackQualityChange(YouTubePlayer.PlaybackQuality.HIGH_RES, youTubePlayer);
                    else if ("default".equalsIgnoreCase(quality))
                        listener.onPlaybackQualityChange(YouTubePlayer.PlaybackQuality.DEFAULT, youTubePlayer);
            }
        });
    }

    /**
     *  The default playback rate is 1, which indicates that the video is playing at normal speed. Playback rates may include values like 0.25, 0.5, 1, 1.5, and 2.
     * @param rate 0.25, 0.5, 1, 1.5, 2
     */
    @JavascriptInterface
    public void onPlaybackRateChange(final String rate) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                double dRate = Double.parseDouble(rate);
                for(YouTubePlayer.YouTubeListener listener : getListeners())
                    listener.onPlaybackRateChange(dRate, youTubePlayer);
            }
        });
    }

    @JavascriptInterface
    public void onError(final String error) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayer.YouTubeListener listener : getListeners())
                    listener.onError(error, youTubePlayer);
            }
        });
    }

    @JavascriptInterface
    public void onApiChange() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayer.YouTubeListener listener : getListeners())
                    listener.onApiChange(youTubePlayer);
            }
        });
    }

    @JavascriptInterface
    public void currentSeconds(final String seconds) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                float dSeconds = Float.parseFloat(seconds);
                for(YouTubePlayer.YouTubeListener listener : getListeners())
                    listener.onCurrentSecond(dSeconds, youTubePlayer);
            }
        });
    }

    @JavascriptInterface
    public void duration(final String seconds) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                float duration = Float.parseFloat(seconds);
                for(YouTubePlayer.YouTubeListener listener : getListeners())
                    listener.onDuration(duration, youTubePlayer);
            }
        });
    }

    @JavascriptInterface
    public void onLog(final String message) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayer.YouTubeListener listener : getListeners())
                    listener.onLog(message, youTubePlayer);
            }
        });
    }

    private Set<YouTubePlayer.YouTubeListener> getListeners() {
        Set<YouTubePlayer.YouTubeListener> listeners = new HashSet<>();
        listeners.addAll(youTubePlayer.getListeners());

        return listeners;
    }
}
